package group.project;

import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.*;
import javafx.stage.Stage;
import javafx.application.*;
import javafx.event.*;
import javafx.fxml.*;
import java.io.*;
import java.nio.file.Paths;
import java.net.URL;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import java.security.GeneralSecurityException;

import org.bytedeco.javacv.*;
import org.bytedeco.javacpp.*;
import org.bytedeco.opencv.opencv_core.*;
import org.bytedeco.opencv.opencv_objdetect.*;
import org.bytedeco.opencv.opencv_face.*;
import org.bytedeco.opencv.opencv_videoio.VideoCapture;
import org.bytedeco.opencv.opencv_dnn.*;
import static org.bytedeco.opencv.global.opencv_imgcodecs.*;
import static org.bytedeco.opencv.global.opencv_highgui.*;
import static org.bytedeco.opencv.global.opencv_imgproc.*;
import static org.bytedeco.opencv.global.opencv_face.*;
import static org.bytedeco.opencv.global.opencv_core.*;
import static org.bytedeco.opencv.global.opencv_objdetect.*;
import static org.bytedeco.opencv.global.opencv_dnn.*;

/**
 * A class to handle the Camera Window control
 *
 * @author petra
 */
public class StartCameraController implements Initializable {

    /**
     * The place to display the image of current frame taken by webcam
     */
    @FXML
    private ImageView currFrame;

    /**
     * The button to start capturing
     */
    @FXML
    private Button startBtn;

    /**
     * The FaceRecognizer to be used to recognize the face in current frame
     */
    private FaceRecognizer faceRecognizer;

    /**
     * The Google Vision API to be used to get the emotions of the face in
     * current frame
     */
    private final GoogleVisionInterface gVision;

    /**
     * The OpenCV library to capture using a webcam
     */
    private final VideoCapture capture = new VideoCapture();

    /**
     * A scheduler to update the video frame
     */
    private final ScheduledExecutorService timer = Executors.newSingleThreadScheduledExecutor();

    /**
     * An FXML loader for loading Visit Reason selection Window
     */
    private final FXMLLoader fxmlLoaderInfo;

    /**
     * An FXML loader for loading the Alert window when current student is not
     * recognized
     */
    private final FXMLLoader fxmlLoaderAlert;

    /**
     * The root element of the Visit Reason selection window
     */
    private final Parent dashboard;

    /**
     * The root element of the alert window
     */
    private final Parent alert;

    /**
     * The scene to display the visit reason selection window
     */
    private Scene dashboardScene;

    /**
     * The scene to display the alert window
     */
    private Scene alertScene;

    /**
     * The counter of the frame update to schedule the face detection process
     */
    private static int counter = 0;

    /**
     * The counter of how many times a face is not recognized
     */
    private static int checkedCount = 0;

    /**
     * The maximum number of times a face is not recognized before it marks
     * current student as not identified
     */
    private static final int ALLOWED_CHECKED_COUNT = 4; // 6 seconds

    /**
     * The path for the temporary file
     */
    private static final String TEMP_PATH = "./tmp.jpg";

    /**
     * The constructor of this controller
     *
     * @throws IOException The exception when either the fxml files or the
     * google credentials cannot be loaded/found
     * @throws GeneralSecurityException security exceptions
     */
    public StartCameraController() throws IOException, GeneralSecurityException {

        // Load Google Vision Credential
        gVision = new GoogleVisionInterface();

        // choose reason category
        fxmlLoaderInfo = new FXMLLoader(getClass().getClassLoader().getResource("CategoryList.fxml"));
        dashboard = (Parent) fxmlLoaderInfo.load();

        // alert student not found
        fxmlLoaderAlert = new FXMLLoader(getClass().getClassLoader().getResource("Alert1.fxml"));
        alert = (Parent) fxmlLoaderAlert.load();

    }

    /**
     * A method that will be called by JavaFX when this controller is
     * initialized
     *
     * @param location location
     * @param resources resources
     */
    public void initialize(URL location, ResourceBundle resources) {
        // set blank image as the video placeholder
        Image newFrame = new Image(getClass().getClassLoader().getResource("blank.png").toExternalForm());
        currFrame.setImage(newFrame);
        currFrame.setFitWidth(newFrame.getWidth());
        currFrame.setFitHeight(newFrame.getHeight());
        currFrame.setX((892 - newFrame.getWidth()) / 2);
        currFrame.setY(150 + (583 - newFrame.getHeight()) / 2);
    }

    /**
     * A method to be called by the StartController to pass the face recognizer
     * loaded
     *
     * @param faceRecognizer The FaceRecognizer object
     */
    public void setFaceRecognizer(FaceRecognizer faceRecognizer) {
        this.faceRecognizer = faceRecognizer;
    }

    /**
     * A method to be used in handling start detecting button. This starts the
     * camera and detect+recognize face every a few frame updates
     *
     * @param event The start button event
     * @throws IOException Exception when the webcam cannot capture image
     */
    @FXML
    protected void startDetecting(ActionEvent event) throws IOException {
        // open webcam
        capture.open(0);

        // 1000/40 = 25 FPS
        // update video frame 25 times per second
        timer.scheduleAtFixedRate(() -> {
            Mat frame = new Mat();
            RectVector faces = new RectVector();

            // check if the capture is open
            if (capture.isOpened()) {
                try {
                    // read the current frame
                    capture.read(frame);

                    if (!frame.empty()) {
                        faceRecognizer.setImage(frame);
                        faceRecognizer.preprocessAndDetectFaces(frame, faces);
                    }
                } catch (Exception e) {
                    // log the (full) error
                    System.err.println("Exception during the image elaboration: " + e);
                }
            }

            // Get all faces boundaries
            Rect[] facesArray = faces.get();

            // Copy the original frame to be saved
            Mat frameOri = new Mat();
            frame.copyTo(frameOri);

            // draw all face boundaries
            for (int i = 0; i < facesArray.length; i++) {
                rectangle(frame, facesArray[i].tl(), facesArray[i].br(), new Scalar(0, 255, 0, 1), 3, FILLED, 0);
            }

            ++counter;

            // Re-Verify face every 38 Frame / 25 FPS = 1.5 seconds
            if (facesArray.length > 0 && counter >= 38) {
                counter = 0;

                faceRecognizer.calculateEmbedding(facesArray);

                // Compare with student embeddings in the database (+ get emotions)
                HashMap<String, String> emotions = new HashMap<String, String>();
                Student s = getBestMatchedStudentWithEmotion(emotions);

                if (emotions.size() > 0 && s != null) {
                    // Student Recognized

                    // Dont need webcam anymore
                    capture.release();

                    // reset checked counter
                    checkedCount = 0;

                    // Change view to choose reason
                    Platform.runLater(() -> {
                        try {
                            timer.shutdown();
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.exit(1);
                        }
                        CategoryListController categoryListController = fxmlLoaderInfo.getController();

                        // pass parameters
                        categoryListController.setStudentID(s.getId());
                        categoryListController.saveEmotions(emotions);

                        // update view
                        dashboardScene = new Scene(dashboard);
                        ((Stage) startBtn.getScene().getWindow()).setScene(dashboardScene);

                    });
                } else if (++checkedCount >= ALLOWED_CHECKED_COUNT) {
                    System.out.println("Not Identified");
                    // stop webcam
                    capture.release();
                    // reset checked counter
                    checkedCount = 0;

                    // Mat embedding (face feature) to byte[]
                    Mat embedding = faceRecognizer.getCurrentEmbedding();
                    byte[] feature = new byte[(int) (embedding.total() * embedding.elemSize())];
                    embedding.data().get(feature);

                    // Change view to alert						
                    Platform.runLater(() -> {
                        try {
                            timer.shutdown();
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.exit(1);
                        }

                        AlertController alertController = fxmlLoaderAlert.getController();

                        // pass parameters
                        alertController.setFace(feature);
                        alertController.setImage(frameOri);

                        // update view
                        alertScene = new Scene(alert);
                        ((Stage) startBtn.getScene().getWindow()).setScene(alertScene);

                    });

                }
            }

            // update current video frame
            byte[] toBeShown = new byte[(int) frame.total() * frame.channels()];
            imencode(".png", frame, toBeShown);
            Image newFrame = new Image(new ByteArrayInputStream(toBeShown));
            Platform.runLater(() -> {
                currFrame.setFitWidth(newFrame.getWidth());
                currFrame.setFitHeight(newFrame.getHeight());
                currFrame.setX((892 - newFrame.getWidth()) / 2);
                currFrame.setY(150 + (583 - newFrame.getHeight()) / 2);
                currFrame.imageProperty().set(newFrame);
            });
        }, 0, 40, TimeUnit.MILLISECONDS);  // run this method every 40 ms

    }

    /**
     * A method to get the best matched students from the database with current
     * frame
     *
     * @param emos A HashMap that will be filled with the emotions of the
     * student if the student is recognized
     * @return The Student object recognized
     */
    private Student getBestMatchedStudentWithEmotion(HashMap<String, String> emos) {
        double bestCossim = 0;
        double bestIdx = 0;
        Student bestStudent = null;

        // Get all students in the DB
        for (Student s : DAO.getAllStudents()) {
            // get embedding of current student from the db
            Mat storedEmbedding = new Mat(1, 128, CV_32F);
            storedEmbedding.data().put(s.getFeature());

            // compare
            double[] comparison = faceRecognizer.compare(storedEmbedding);
            if (comparison != null && comparison[0] > bestCossim) {
                // update best cosine similarity & best matched student
                bestCossim = comparison[0];
                bestIdx = comparison[1];
                bestStudent = s;
            }
        }

        if (bestStudent != null) {
            // Save the student cropped face to a temp file
            imwrite(TEMP_PATH, faceRecognizer.getCroppedFace(bestIdx));

            // Detect emotion using Google Vision API
            HashMap<String, String> emotions = gVision.getFaceEmotion(Paths.get(TEMP_PATH));

            // Print likelihood of each of them
            for (String k : emotions.keySet()) {
                emos.put(k, emotions.get(k));
                System.out.println(k + ": " + emotions.get(k)); // emotion

            }

            // Delete the temp file
            new File(TEMP_PATH).delete();

            return bestStudent;
        }
        return null;
    }
}
