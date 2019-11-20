package group.project;

import java.io.File;
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

/**
 * A controller for the New Student Form Window
 *
 * @author ruby
 */
public class AddStudentController {

    /**
     * The cancel button used to exit from adding new student
     */
    @FXML
    private Button cancel;

    /**
     * The Gender field of the student
     */
    @FXML
    private TextField gender;

    /**
     * The Major field of the student
     */
    @FXML
    private TextField major;

    /**
     * The Grade (Year) field of the student
     */
    @FXML
    private TextField grade;

    /**
     * The Name field of the student
     */
    @FXML
    private TextField name;

    /**
     * The root of current scene
     */
    @FXML
    private AnchorPane stuPane;

    /**
     * The ID field of the student
     */
    @FXML
    private TextField id;

    /**
     * The element in the window to show the image of current student
     */
    @FXML
    private ImageView imageview;

    /**
     * The facial feature of current student passed from the AlertController
     */
    private byte[] feature;

    /**
     * A method to pass the facial feature of current student from the
     * AlertController to this controller
     *
     * @param feature The facial feature of current student
     */
    public void setFeature(byte[] feature) {
        this.feature = feature;
        System.out.println("New feature Saved in Stu");

    }

    /**
     * A method to fetch the image of current student that has been saved by the
     * AlertController in a temp file
     */
    public void setImage() {
        try {
            File f = new File(getClass().getClassLoader().getResource("image/test.jpg").getFile());
            Image img = new Image(f.toURI().toString());
            imageview.setImage(img);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * A method that handles the save button. Used to create a new student
     * object using the data filled in the form and save it into the database
     *
     * @param event The button event
     */
    @FXML
    void addStu(ActionEvent event) {
        // get textfield contents of each field in the form
        String stuID = id.getText();
        String stuName = name.getText();
        String stuGrade = grade.getText();
        String stuMajor = major.getText();
        String stuGender = gender.getText();

        // can't leave any field blank
        if (stuID.isEmpty() || stuName.isEmpty() || stuGrade.isEmpty() || stuMajor.isEmpty() || stuGender.isEmpty()) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Can't leave any information blank!");
            alert.showAndWait();
            return;

        }
        // create a new student object with the data filled in the form
        Student s1 = new Student(feature, stuID, stuName, stuGender, stuMajor, stuGrade);

        // add student to the database
        DAO.insertStudent(s1);
        System.out.println("New student added! Now " + DAO.getAllStudents().size() + " student(s) in the database");

        // rename student image
        File oldfile = new File(getClass().getClassLoader().getResource("image/test.jpg").getFile());
        File newfile = new File(getClass().getClassLoader().getResource("image").getFile(), stuID + ".jpg");

        if (oldfile.renameTo(newfile)) {
            System.out.println("Success! test.jpg renamed!!");
        } else {
            System.out.println("test.jpg can't be renamed");
        }

        // CLOSE WINDOW
        Stage stage = (Stage) stuPane.getScene().getWindow();
        stage.close();

    }

    /**
     * A method used to handle the cancel button event
     *
     * @param event The cancel button event
     */
    @FXML
    void Cancel(ActionEvent event) {
        Stage stage = (Stage) stuPane.getScene().getWindow();
        stage.close();
    }

}
