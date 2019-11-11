package group.project;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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

    @FXML
    void startCamera(ActionEvent event) {
    	loadWindow("StartCamera.fxml");

    }

    @FXML
    void quit(ActionEvent event) {
    	Stage stage = (Stage)startPane.getScene().getWindow();
    	stage.close();
    }

    @FXML
    void showReport(ActionEvent event) {  // change to menu bar
    	loadWindow("FreReport.fxml");
    }
    
    
    void loadWindow(String name) {
    	try {
			Parent parent=FXMLLoader.load(getClass().getClassLoader().getResource(name));
			Stage stage = new Stage(StageStyle.DECORATED);
			//stage.setTitle("");
			stage.setScene(new Scene(parent));
			stage.show();
		} catch (IOException e) {
			e.printStackTrace(); //?
		}
    	
    	
    }
}

