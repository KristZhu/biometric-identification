package group.project;

import javafx.event.ActionEvent;

import java.io.File;
import java.net.URISyntaxException;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class StudentInfoController {

	@FXML
	private PieChart piechart;
	@FXML
	private ChoiceBox choicebox;
	@FXML
	private TextField StudentID;
	@FXML
	private TextField name;
    @FXML
    private BarChart<?, ?> barChart;
    @FXML
    private CategoryAxis x;
    @FXML
    private NumberAxis y;
    @FXML
    private ImageView imageview;
    
    private String ID;

    public void setStudentID(String id) {
        ID = id;
		Student s1 = DAO.getStudentByID(ID); // pass ID , suppose id = 1
		System.out.println(ID);
		StudentID.setText(s1.getId()); 
		name.setText(s1.getName());
		
	//set image
		try {
			File file = new File(getClass().getResource("images/"+ID+".jpg").getFile());
			Image img = new Image(file.getAbsolutePath());
			imageview.setImage(img);
		} catch (Exception e) {
			e.printStackTrace();
		} // src/main/resources/image/test.jpg
    }
    
	@FXML
	private void initialize() {		

		XYChart.Series set = new XYChart.Series<>();
		set.getData().addAll(
				new XYChart.Data("James",5),
				new XYChart.Data("More",4)				
				);
		barChart.getData().addAll(set);
		
		//pie chart
		ObservableList<String> type = FXCollections.observableArrayList("Major", "Gender", "Test");
		choicebox.setValue("Major");
		choicebox.setItems(type);

		choicebox.getSelectionModel().selectedIndexProperty()
				.addListener((ChangeListener<? super Number>) new ChangeListener<Number>() {
					@Override
					public void changed(ObservableValue<? extends Number> ov, Number oldSelected, Number newSelected) {
						int choice = choicebox.getSelectionModel().getSelectedIndex();
						System.out.println("Selected Option: " + choice);
						switch (choice) {
						case 0:
							MajorChart();
							break;
						case 1:
							GenderChart();
							break;
						case 2:
							System.out.println("test");
							break;
						}

					}
				});
	}

	public void GenderChart() {
		// get data form DB
		int male = 0, female = 0;
		for (Student s : DAO.getAllStudents()) {
			if (s.getGender().equals("M")) { // may change
				male++;
			} else {
				female++;
			}
		}
		ObservableList<Data> list = FXCollections.observableArrayList(new PieChart.Data("Male", male),
				new PieChart.Data("Female", female));
		piechart.setData(list);

	}

	public void MajorChart() {
		// get data form DB
		int mism = 0, msppm = 0, msit = 0;
		for (Student s : DAO.getAllStudents()) {
			if (s.getMajor().equals("MISM")) { // may change
				mism++;
			} else if (s.getMajor().equals("MSPPM")) {
				msppm++;
			} else {
				msit++;
			}
		}
		ObservableList<Data> list = FXCollections.observableArrayList(new PieChart.Data("MISM", mism),
				new PieChart.Data("MSPPM", msppm), new PieChart.Data("MSIT", msit));
		piechart.setData(list);

	}
}
