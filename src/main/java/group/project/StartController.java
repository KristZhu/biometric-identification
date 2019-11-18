package group.project;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class StartController {

    @FXML
    private Button exit;

    @FXML
    private Button report;

    @FXML
    private Button camera;
    
    @FXML
    private AnchorPane startPane;
    
    FaceRecognizer faceRecognizer;

    @FXML
    void startCamera(ActionEvent event) {
		//camera.setStyle("-fx-background-color: #9489DE; ");
    	loadWindow("StartCamera.fxml");

    }

    @FXML
    void quit(ActionEvent event) {
		//exit.setStyle("-fx-background-color: #9489DE; ");
    	Platform.exit();
    	System.exit(0);
    }

    @FXML
    void showReport(ActionEvent event) { 
		//report.setStyle("-fx-background-color: #9489DE; ");
    	loadWindow("Report.fxml");
    }
    
    // load desired window
    void loadWindow(String name) {
    	try {
    		FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(name));
			Parent parent = loader.load();
    		if(name.equals("StartCamera.fxml")) {
    			((StartCameraController)loader.getController()).setFaceRecognizer(faceRecognizer);
    		}
			Stage stage = new Stage(StageStyle.DECORATED);
			stage.setScene(new Scene(parent));
			stage.show();
		} catch (IOException e) {
			e.printStackTrace(); //?
		}
    	
    	
    }
    
    public StartController() throws IOException {
		faceRecognizer = new FaceRecognizer("haarcascade_frontalface_alt2.xml", "nn4.small2.v1.t7");
	}
}

