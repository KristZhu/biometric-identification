package group.project;

import javafx.event.ActionEvent;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bytedeco.librealsense.intrinsics;

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
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class StudentInfoController {

    @FXML
    private Label majorLabel;  

    @FXML
    private Label nameLabel;
    
	@FXML
    private Label nameText;

    @FXML
    private Label  studentID;
    
    @FXML
    private Label  gender;

    @FXML
    private Label  year;

    @FXML
    private Label  major;

    @FXML
    private Label AngerScale1;

    @FXML
    private Label SurpriseScale1;

    @FXML
    private Label HappyScale1;

    @FXML
    private Label SadScale1;
    
    @FXML
    private BarChart<?, ?> barChart;
    
    @FXML
    private CategoryAxis x;
    
    @FXML
    private NumberAxis y;
    
    @FXML
    private ImageView imageview;
    
    @FXML
    private Label VisitTimes;
    
    @FXML
    private Label lastVisitTime;

    @FXML
    private Label message;
    
    @FXML
    private Label alert;
    
    private String ID;
    
    private HashMap<String, String> emotions;
    
    public void saveEmotions(HashMap<String, String> emos) {
    	emotions = emos; 	
    }

    public void setStudentID(String id) {
        ID = id;
		Student s1 = DAO.getStudentByID(ID); // pass ID , suppose id = 1
      
		//set info
		studentID.setText(s1.getId()); 
		nameText.setText(s1.getName());
		gender.setText(s1.getGender());
		year.setText(s1.getGrade());
		major.setText(s1.getMajor());
		VisitTimes.setText(""+getVisitsAmount(s1));
		
		Date lastVisitDate = lastVisit(s1);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(lastVisitDate);
		
		lastVisitTime.setText("" + calendar.get(Calendar.DAY_OF_MONTH) + "/"
							+ (calendar.get(Calendar.MONTH)+1) + "/"
							+ calendar.get(Calendar.YEAR));
		nameLabel.setText(s1.getName());
		majorLabel.setText(s1.getMajor());
		
		HappyScale1.setText(emotions.get("JOY"));
		SadScale1.setText(emotions.get("SORROW"));
		SurpriseScale1.setText(emotions.get("SURPRISE"));
		AngerScale1.setText(emotions.get("ANGER"));
		
		message.setText("MY HEART IS IN THE WORK");
		alert.setText(genMessage());
				
		ReasonBarChart(s1);
		
	//set image
		try {
			File file = new File(getClass().getClassLoader().getResource("image/"+ID+".jpg").getFile());
			Image img = new Image(file.toURI().toString());//file.getAbsolutePath()
			imageview.setImage(img);
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
    }
    
    
	// generate alert message
	private String genMessage() {			
		String[] strList = {"You have a JAVA quiz tommorow!",
				"Remember to respond Prof.Murli",
				"Have a meeting with Riaz",
				"Join SRC Christmas Party!",
				"Reserve the Data mining Speech"};
		int i = (int)(Math.random()*5);		
		return strList[i];
	}
	
	// display reason bar chart
	public void ReasonBarChart(Student s) {
		int r1 = 0, r2 = 0, r3 = 0, r4 = 0, r5 = 0, r6 = 0;
		
		Map<Integer, Integer> reasonFrequency = getEachVisitAmount(s);		
		r1 = reasonFrequency.get(1);
		r2 = reasonFrequency.get(2);
		r3 = reasonFrequency.get(3);
		r4 = reasonFrequency.get(4);
		r5 = reasonFrequency.get(5);
		r6 = reasonFrequency.get(6);
		
		
		System.out.println("total:" + r1 + " " + r2 + " " + r3 + " " + r4 + " " + r5 + " " + r6);
				
		XYChart.Series set = new XYChart.Series<>();
		set.getData().addAll(
				new XYChart.Data("1",r1),
				new XYChart.Data("2",r2),
				new XYChart.Data("3",r3),
				new XYChart.Data("4",r4),
				new XYChart.Data("5",r5),
				new XYChart.Data("6",r6)
				);
		set.setName("amount of visit");
		barChart.getData().clear();
		barChart.getData().addAll(set);		
	}

	// get the last time of visit
	private static Date lastVisit(Student student) {
		List<Date> dates = new ArrayList<Date>();				
		for(int i=1; i<=6; i++) {
			dates.addAll(student.getAllVisits().get(i));
		}
		Collections.sort(dates);
		return dates.get(dates.size()-1);
	}
	
	// get the total amount of visit 
	private static int getVisitsAmount(Student student) {
		int amount = 0;
		Map<Integer, List<Date>> visits = student.getAllVisits();
		for(int i: visits.keySet()) {
			amount += visits.get(i).size();
		}
		return amount;
	}
	
	/**
	 * @Integer1: Visit reason Integer2: How many of this reason
	 */
	private static Map<Integer, Integer> getEachVisitAmount(Student student) {
		Map<Integer, Integer> eachVisitAmount = new HashMap<Integer, Integer>();
		Map<Integer, List<Date>> visits = student.getAllVisits();
		System.out.println("getEachVisitAmountDebug2: "+visits);
		for(int i: visits.keySet()) {
			eachVisitAmount.put(i, visits.get(i).size());
		}
		System.out.println("getEachVisitAmountDebug3: "+eachVisitAmount);
		return eachVisitAmount;
	}
}
