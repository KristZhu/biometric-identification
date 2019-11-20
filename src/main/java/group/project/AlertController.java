package group.project;

import static org.bytedeco.opencv.global.opencv_imgcodecs.imwrite;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.bytedeco.opencv.opencv_core.Mat;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * A controller for the alert window when the student is not recognised
 *
 * @author ruby
 */
public class AlertController {

    /**
     * The add button used to go to the add new student form window
     */
    @FXML
    private Button add;

    /**
     * The element that is used to show the picture of current student
     */
    @FXML
    private ImageView image;

    /**
     * The root element of the UI
     */
    @FXML
    private AnchorPane alertPane;

    /**
     * The facial feature of current student
     */
    private byte[] face;

    /**
     * The image of current student stored in OpenCV Mat
     */
    private Mat frameMat;

    /**
     * A method used by the CameraController to pass the facial feature of
     * current student to this controller
     *
     * @param feature The facial feature of current student
     */
    public void setFace(byte[] feature) {
        face = feature;
        System.out.println("New feature Saved");
    }

    /**
     * A method used by the CameraController to pass the image of current
     * student to this controller and save the picture of current student into a
     * temp file. This temp file will be used by the AddStudentController for
     * displaying and storing the picture
     *
     * @param frame The image of current student captured by the
     * CameraController
     */
    public void setImage(Mat frame) {
        frameMat = frame;
        try {
            // check if the 'image' folder exist
            URL imageFolder = getClass().getClassLoader().getResource("image");
            if (imageFolder == null) {
                // if it does not exist, create one
                Path base = Paths.get(getClass().getResource("/").toURI());
                Path newFolder = Paths.get(base.toAbsolutePath() + "/image/");
                Files.createDirectory(newFolder);
            }

            // write current image to the temp file
            File f = new File(getClass().getClassLoader().getResource("image").getFile());
            imwrite(f.getAbsolutePath() + "/test.jpg", frameMat);
            System.out.println("test.jpg generated!");

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        try {
            // Load the previously saved temp file and display it in the image view
            File f = new File(getClass().getClassLoader().getResource("image/test.jpg").getFile());
            Image img = new Image(f.toURI().toString());
            image.setImage(img);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * A method used to handle the add button in the alert window. This will
     * basically load the add new student form window and pass the necessary
     * data to its controller
     *
     * @param event The add button event
     */
    @FXML
    void addStudent(ActionEvent event) {
        Platform.runLater(() -> {
            FXMLLoader fxmlLoaderStuFxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("AddStu.fxml"));
            Parent stuParent;
            try {
                stuParent = (Parent) fxmlLoaderStuFxmlLoader.load();
                AddStudentController addStudentController = fxmlLoaderStuFxmlLoader.getController();
                addStudentController.setFeature(face); // pass face feature
                addStudentController.setImage(); // make the AddStudentController load the image in the temp file previously saved
                Scene stuScene = new Scene(stuParent);
                ((Stage) add.getScene().getWindow()).setScene(stuScene);  // change to add new student form page
            } catch (IOException e) {
                e.printStackTrace();
            }

        });

    }

    /**
     * A method used to handle the quit button in the alert window. When a user
     * is not recognised, the admin can choose whether current student should be
     * added to the database or not. This button is used if the admin does not
     * want to add current student to the database
     *
     * @param event The quit button event
     */
    @FXML
    void quit(ActionEvent event) {
        Stage stage = (Stage) alertPane.getScene().getWindow();
        stage.close();
    }

}
