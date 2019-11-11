package group.project;


import static org.bytedeco.opencv.global.opencv_imgcodecs.imwrite;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;

import com.fasterxml.jackson.core.JsonFactory.Feature;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class AddStudentController {

    @FXML
    private Button cancel;

    @FXML
    private TextField gender;

    @FXML
    private TextField major;

    @FXML
    private TextField grade;

    @FXML
    private TextField name;

    @FXML
    private Button save;

    @FXML
    private AnchorPane stuPane;

    @FXML
    private TextField id;
    
    @FXML
    private ImageView imageview;

    private byte[] feature;
    
    public void setFeature(byte[] feature) {
        this.feature = feature;
        System.out.println("New feature Saved in Stu");

    }
    
	//set image
    public void setImage() {
    	try {
			File f = new File(getClass().getClassLoader().getResource("image/test.jpg").getFile());
			Image img = new Image(f.toURI().toString());
			imageview.setImage(img);
		} catch (Exception e) {
			e.printStackTrace();
		} 
    }

    

    @FXML
    void addStu(ActionEvent event) {
    	String stuID = id.getText();
    	String stuName = name.getText();
    	String stuGrade = grade.getText();
    	String stuMajor = major.getText();
    	String stuGender = gender.getText();
    	
    	// can't leave blank
    	if(stuID.isEmpty()||stuName.isEmpty()||stuGrade.isEmpty()||stuMajor.isEmpty()||stuGender.isEmpty()) {
    		Alert alert = new Alert(AlertType.ERROR);
    		alert.setHeaderText(null);
    		alert.setContentText("Can't leave any information blank!");
    		alert.showAndWait();
    		return;
    	  	
    	}
    	//new student (parameter)
    	Student s1 = new Student(feature, stuID, stuName, stuGender, stuMajor, stuGrade);
    	//static add student
    	DAO.insertStudent(s1);  
		System.out.println("New student added! Now " + DAO.getAllStudents().size() + " student(s) in the database");
		
		// rename student image
		//File oldfile =new File("images/test.jpg");
        //File newfile =new File("images/"+stuID+".jpg");
		File oldfile = new File(getClass().getClassLoader().getResource("image/test.jpg").getFile());
		File newfile = new File(getClass().getClassLoader().getResource("image").getFile(), stuID + ".jpg");
        
        if(oldfile.renameTo(newfile)){
            System.out.println("Success! test.jpg renamed!!");
        }else{
            System.out.println("test.jpg can't be renamed");
        }
        
		//CLOSE WINDOW
    	Stage stage = (Stage)stuPane.getScene().getWindow();
    	stage.close();
    	
    }

    @FXML
    void Cancel(ActionEvent event) {
    	Stage stage = (Stage)stuPane.getScene().getWindow();
    	stage.close();
    }

}

