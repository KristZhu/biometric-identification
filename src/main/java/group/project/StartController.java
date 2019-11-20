package group.project;

import java.io.IOException;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * A controller class to handle the home view
 *
 * @author ruby
 */
public class StartController {

    /**
     * The FaceRecognizer object to be passed to StartCameraController
     */
    FaceRecognizer faceRecognizer;

    /**
     * An event handler for start camera button. Used to display the camera
     * window
     *
     * @param event The start camera button event
     */
    @FXML
    void startCamera(ActionEvent event) {
        loadWindow("StartCamera.fxml");
    }

    /**
     * An event handler for quit button. Used to quit the program.
     *
     * @param event The quit button event
     */
    @FXML
    void quit(ActionEvent event) {
        Platform.exit();
        System.exit(0);
    }

    /**
     * An event handler for report button. Used to display the students report
     * window
     *
     * @param event The report button event
     */
    @FXML
    void showReport(ActionEvent event) {
        loadWindow("Report.fxml");
    }

    /**
     * A method to load and display the window with 'name' fxml file
     *
     * @param name The FXML file name to be displayed
     */
    void loadWindow(String name) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(name));
            Parent parent = loader.load();
            if (name.equals("StartCamera.fxml")) {
                ((StartCameraController) loader.getController()).setFaceRecognizer(faceRecognizer);
            }
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setScene(new Scene(parent));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * The constructor of this controller. Used to load the pre-trained models
     * used in the FaceRecognizer
     *
     * @throws IOException Exception when the files of the pre-trained models
     * could not be found/loaded
     */
    public StartController() throws IOException {
        faceRecognizer = new FaceRecognizer("haarcascade_frontalface_alt2.xml", "nn4.small2.v1.t7");
    }
}
