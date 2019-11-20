package group.project;

import java.io.IOException;
import java.util.HashMap;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * The controller class of the window with the visit reason dropdown
 *
 * @author ruby
 */
public class CategoryListController {

    /**
     * The text label for the visit reason
     */
    @FXML
    private Label label;

    /**
     * The dropdown menu for the visit reasons
     */
    @FXML
    private ChoiceBox<String> choiceBox;

    /**
     * The choose button
     */
    @FXML
    private Button choose;

    /**
     * The ID of current student
     */
    private String ID;

    /**
     * The emotions of current student
     */
    private HashMap<String, String> emotions;

    /**
     * A method to be called by the CameraController when a student face is
     * recognised to keep the current student ID into this controller
     *
     * @param id
     */
    public void setStudentID(String id) {
        ID = id;
        Student s = DAO.getStudentByID(id);
        label.setText("Choose Visit Reason for " + s.getName());

    }

    /**
     * A method to be called by the CameraController when a student face is
     * recognised to keep the current student emotions into this controller
     *
     * @param emos
     */
    public void saveEmotions(HashMap<String, String> emos) {
        emotions = emos;
    }

    /**
     * A method that will be called when this controller is initialized by
     * JavaFX
     */
    @FXML
    private void initialize() {
        // choice box for choose reasons
        ObservableList<String> type = FXCollections.observableArrayList("Borrow Stapler", "Visit Nereshnee",
                "Visit Kim", "Complain", "Collect Assignment", "Others");
        choiceBox.setValue("Borrow Stapler");  // default value
        choiceBox.setItems(type);
        choiceBox.getSelectionModel().selectedIndexProperty()
                .addListener((ChangeListener<? super Number>) new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> ov, Number oldSelected, Number newSelected) {
                        int choice = choiceBox.getSelectionModel().getSelectedIndex();
                        System.out.println("Selected Option: " + choice);

                    }
                });

    }

    /**
     * A method used to handle save button. This method will update the
     * student's visit reasons and change the window to the dashboard of student
     * info
     *
     * @param event The save button event
     */
    @FXML
    void save(ActionEvent event) {
        // get current student from the database using the ID
        Student s1 = DAO.getStudentByID(ID);
        int choice = choiceBox.getSelectionModel().getSelectedIndex();
        System.out.println("Saved Option: " + (choice + 1));

        // update the student object's visit reasons
        s1.putVisit(choice + 1); // 1,2,3,4,5,6

        // update current student info in the database
        DAO.updateStudent(s1);

        // change to student info page
        Platform.runLater(() -> {
            FXMLLoader fxmlLoaderInfo = new FXMLLoader(getClass().getClassLoader().getResource("StudentInfo.fxml"));
            Parent stuParent;
            try {
                stuParent = (Parent) fxmlLoaderInfo.load();
                StudentInfoController stuInfoController = fxmlLoaderInfo.getController();
                stuInfoController.saveEmotions(emotions);  // pass emotions to the student info controller
                stuInfoController.setStudentID(ID); // pass ID to the student info controller
                Scene stuScene = new Scene(stuParent);
                stuScene.getStylesheets()
                        .add(getClass().getClassLoader().getResource("stylesheet.css").toExternalForm());
                ((Stage) choose.getScene().getWindow()).setScene(stuScene);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        });

    }

}
