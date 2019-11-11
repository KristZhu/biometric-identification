package group.project;

import org.bytedeco.librealsense.intrinsics;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class FreReportController {

	@FXML
	private AnchorPane frePane;

	@FXML
	private PieChart fPie;

	@FXML
	private PieChart mPie;

	@FXML
	private Button back;
	@FXML
	private ChoiceBox choiceBox;

	@FXML
	private void initialize() {

		ObservableList<String> type = FXCollections.observableArrayList("Major", "Grade", "Reason");
		choiceBox.setValue("Major");
		choiceBox.setItems(type);

		choiceBox.getSelectionModel().selectedIndexProperty()
				.addListener((ChangeListener<? super Number>) new ChangeListener<Number>() {
					@Override
					public void changed(ObservableValue<? extends Number> ov, Number oldSelected, Number newSelected) {
						int choice = choiceBox.getSelectionModel().getSelectedIndex();
						System.out.println("Selected Option: " + choice);
						switch (choice) {
						case 0:
							MajorChart();
							break;
						case 1:
							GradeChart();
							break;
						case 2:
							System.out.println("SHOW by REASON");
							break;
						}

					}
				});

	}

// display major pie chart	
	public void MajorChart() {
		// get data form DB
		int mismF = 0, msppmF = 0, msitF = 0;
		int mismM = 0, msppmM = 0, msitM = 0;
		for (Student s : DAO.getAllStudents()) {
			if (s.getGender().equals("F")) // F
			{
				if (s.getMajor().equals("MISM")) { // may change
					mismF++;
				} else if (s.getMajor().equals("MSPPM")) {
					msppmF++;
				} else {
					msitF++;
				}
			} else {
				if (s.getMajor().equals("MISM")) { // may change
					mismM++;
				} else if (s.getMajor().equals("MSPPM")) {
					msppmM++;
				} else {
					msitM++;
				}
			}
		}

		ObservableList<Data> listF = FXCollections.observableArrayList(new PieChart.Data("MISM", mismF),
				new PieChart.Data("MSPPM", msppmF), new PieChart.Data("MSIT", msitF));
		fPie.setData(listF);
		ObservableList<Data> listM = FXCollections.observableArrayList(new PieChart.Data("MISM", mismM),
				new PieChart.Data("MSPPM", msppmM), new PieChart.Data("MSIT", msitM));
		mPie.setData(listM);

	}

	// diaplay grade pie chart
	public void GradeChart() {
		// get data form DB
		int oneF = 0, twoF = 0;
		int oneM = 0, twoM = 0;
		for (Student s : DAO.getAllStudents()) {
			if (s.getGender().equals("F")) // F
			{
				if (s.getGrade().equals("1")) { // may change
					oneF++;
				} else if (s.getGrade().equals("2")) {
					twoF++;
				}
			} else {  //M
				if (s.getGrade().equals("1")) { // may change
					oneM++;
				} else if (s.getGrade().equals("2")) {
					twoM++;
				}
			}
		}

		ObservableList<Data> listF = FXCollections.observableArrayList(
				new PieChart.Data("Grade 1", oneF), new PieChart.Data("Grade 2", twoF));
		fPie.setData(listF);
		
		ObservableList<Data> listM = FXCollections.observableArrayList(new PieChart.Data("Grade 1", oneM),
				new PieChart.Data("Grade 2", twoM));
		mPie.setData(listM);

	}

	@FXML
	void Back(ActionEvent event) {
    	Stage stage = (Stage)frePane.getScene().getWindow();
    	stage.close();
	}

}
