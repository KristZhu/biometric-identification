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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class CategoryListController {

	@FXML
	private Label label;

	@FXML
	private ChoiceBox<String> choiceBox;

	@FXML
	private Button choose;

	@FXML
	private AnchorPane reasonPane;

	private String ID;

	private HashMap<String, String> emotions;

	// set student ID
	public void setStudentID(String id) {
		ID = id;
		Student s = DAO.getStudentByID(id);
		label.setText("Choose Visit Reason for " + s.getName());

	}

	// save emotion parameters
	public void saveEmotions(HashMap<String, String> emos) {
		emotions = emos;
	}

	@FXML
	private void initialize() {
		// choice box for choose reasons
		ObservableList<String> type = FXCollections.observableArrayList("Borrow Stapler", "Visit Nereshnee",
				"Visit Kim", "Complain", "Collect Assignment", "Others");
		choiceBox.setValue("Borrow Stapler");
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

	@FXML
	void save(ActionEvent event) {
		// save one reason for visit in DB
		Student s1 = DAO.getStudentByID(ID);
		int choice = choiceBox.getSelectionModel().getSelectedIndex();
		System.out.println("Saved Option: " + (choice + 1));
		s1.putVisit(choice + 1); // 1,2,3,4,5,6
		// System.out.println("Category...Debug1 "+s1.getAllVisits());
		DAO.updateStudent(s1);
		// System.out.println("Category...Debug2 "+DAO.getAllVisits());

		// change to student info page
		Platform.runLater(() -> {
			FXMLLoader fxmlLoaderInfo = new FXMLLoader(getClass().getClassLoader().getResource("StudentInfo.fxml"));
			Parent stuParent;
			try {
				stuParent = (Parent) fxmlLoaderInfo.load();
				StudentInfoController stuInfoController = fxmlLoaderInfo.getController();
				stuInfoController.saveEmotions(emotions);
				stuInfoController.setStudentID(ID); // pass ID
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
