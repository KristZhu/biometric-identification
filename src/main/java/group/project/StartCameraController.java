
package group.project;

import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.application.*;
import javafx.event.*;
import javafx.fxml.*;
import java.io.*;
import java.nio.file.Files;
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

public class StartCameraController implements Initializable {
	@FXML
	private ImageView currFrame;
	@FXML
	private Button startBtn;
    @FXML
    private AnchorPane cameraPane;
    
	private FaceRecognizer faceRecognizer;
	private GoogleVisionInterface gVision;
	private VideoCapture capture = new VideoCapture();
	private ScheduledExecutorService timer = Executors.newSingleThreadScheduledExecutor();
	private FXMLLoader fxmlLoaderInfo;
	private FXMLLoader fxmlLoaderAlert;
	private Parent dashboard;
	private Parent alert;
	private Scene dashboardScene;
	private Scene alertScene;
	private static int counter = 0;
	private static int checkedCount = 0;
	private static final int ALLOWED_CHECKED_COUNT = 3; // 6 seconds
	private static final String TEMP_PATH = "./tmp.jpg";

	public StartCameraController() throws IOException, GeneralSecurityException {
		faceRecognizer = new FaceRecognizer("haarcascade_frontalface_alt2.xml", "nn4.small2.v1.t7");

		// Load Google Vision Credential
		gVision = new GoogleVisionInterface();

		// student details
		fxmlLoaderInfo = new FXMLLoader(getClass().getClassLoader().getResource("StudentInfo.fxml"));
		dashboard = (Parent) fxmlLoaderInfo.load();

		// alert student not found
		fxmlLoaderAlert = new FXMLLoader(getClass().getClassLoader().getResource("Alert1.fxml"));
		alert = (Parent) fxmlLoaderAlert.load();

	}

	public void initialize(URL location, ResourceBundle resources) {
		Image newFrame = new Image(getClass().getClassLoader().getResource("blank.png").toExternalForm());
		currFrame.setImage(newFrame);
		currFrame.setFitWidth(newFrame.getWidth());
		currFrame.setFitHeight(newFrame.getHeight());
		currFrame.setX((892 - newFrame.getWidth()) / 2);
		currFrame.setY(150 + (583 - newFrame.getHeight()) / 2);
	}

	@FXML
	protected void startDetecting(ActionEvent event) throws IOException {
		capture.open(0);

		Runnable frameGrabber = new Runnable() {
			@Override
			public void run() {
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

				// draw all face boundaries
				Rect[] facesArray = faces.get();
				for (int i = 0; i < facesArray.length; i++)
					rectangle(frame, facesArray[i].tl(), facesArray[i].br(), new Scalar(0, 255, 0, 1), 3, FILLED, 0);

				++counter;
				System.out.println(counter);
				System.out.println("DETECTED");

				// Re-Verify face every 50 Frame / 25 FPS = 2 seconds
				if (facesArray.length > 0 && counter >= 50) {
					counter = 0;

					faceRecognizer.calculateEmbedding(facesArray);

					// Compare with student embeddings in the database
					HashMap<String, String> emotions = new HashMap<String, String>();
					Student s = getBestMatchedStudentWithEmotion(emotions);

					if (emotions.size() > 0 && s != null) {
						// Student Recognized

						// No need webcam anymore
						capture.release();

						// Change view to dashboard
						Platform.runLater(() -> {
							StudentInfoController stuInfoController = fxmlLoaderInfo.getController();
							System.out.println(stuInfoController + "controller");
							stuInfoController.setStudentID(s.getId()); // pass parameter
							dashboardScene = new Scene(dashboard, 892, 733);
							dashboardScene.getStylesheets()
									.add(getClass().getClassLoader().getResource("stylesheet.css").toExternalForm());

							((Stage) startBtn.getScene().getWindow()).setScene(dashboardScene); // pass parameter

						});

						System.out.println(s); // print student

						Platform.runLater(() -> {
							try {
								timer.shutdown();
							} catch (Exception e) {
								e.printStackTrace();
								System.exit(1);
							}
						});
					} else if (++checkedCount >= ALLOWED_CHECKED_COUNT) {
						System.out.println("Not Identified");
						// stop webcam
						capture.release();


						// Mat to byte[]
						Mat embedding = faceRecognizer.getCurrentEmbedding();
						byte[] feature = new byte[(int) (embedding.total() * embedding.elemSize())];
						embedding.data().get(feature);
						
						// Change view to alert						
						Platform.runLater(() -> {
							
							AlertController alertController = fxmlLoaderAlert.getController();
							System.out.println(alertController + "alert controller");
							alertController.setFace(feature); // pass parameter

							alertController.setImage(frame);

							alertScene = new Scene(alert, 500, 500);
							((Stage) startBtn.getScene().getWindow()).setScene(alertScene);

						}); 

					}
				}

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
			}
		};

		// 1000/40 = 25 FPS
		timer.scheduleAtFixedRate(frameGrabber, 0, 40, TimeUnit.MILLISECONDS);
	}

	private Student getBestMatchedStudentWithEmotion(HashMap<String, String> emos) {
		double bestCossim = 0;
		double bestIdx = 0;
		Student bestStudent = null;

		for (Student s : DAO.getAllStudents()) {
			Mat storedEmbedding = new Mat(1, 128, CV_32F);
			storedEmbedding.data().put(s.getFeature());
			double[] comparison = faceRecognizer.compare(storedEmbedding);
			if (comparison != null && comparison[0] > bestCossim) {
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
