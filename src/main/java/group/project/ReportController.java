package group.project;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.bytedeco.librealsense.intrinsics;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ReportController {

	@FXML
	private AnchorPane frePane;

	@FXML
	private DatePicker startDate;

	@FXML
	private DatePicker endDate;

	@FXML
	private PieChart fPie;

	@FXML
	private PieChart mPie;

	@FXML
	private BarChart<?, ?> barChart;
	
    @FXML
    private CategoryAxis x;

    @FXML
    private NumberAxis y;

	@FXML
	private Button back;

	@FXML
	private void initialize() {
		startDate.setValue(Localdate("16-11-2019"));
		startDate.setShowWeekNumbers(true);
		endDate.setValue(Localdate("30-11-2019"));
		// endDate.setShowWeekNumbers(true);
		ReasonPieChart();
		ReasonBarChart();
		
	}
   // format date
	public static final LocalDate Localdate(String dateString) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		LocalDate localDate = LocalDate.parse(dateString, formatter);
		return localDate;
	}

	@FXML // refresh when Date changed
	void change(ActionEvent event) { 
		ReasonPieChart();
		ReasonBarChart();
	}

	// transform LocalDate to Date
	public Date transformDate(LocalDate localDate) {
		Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
		Date date = Date.from(instant);
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date2 = DAO.stringToDate(formatter.format(date));
		System.out.println(date2);
		return date2;
	}

	// display Reason pie chart
	public void ReasonPieChart() {
		// sort by date
		Date date1 = transformDate(startDate.getValue());
		Date date2 = transformDate(endDate.getValue());

		// get data from DB
		int r1F = 0, r2F = 0, r3F = 0, r4F = 0, r5F = 0, r6F = 0;
		int r1M = 0, r2M = 0, r3M = 0, r4M = 0, r5M = 0, r6M = 0;

		Map<Integer, Integer> reasonFrequency = reasonFrequency(date1, date2, "F");
		r1F = reasonFrequency.get(1);
		r2F = reasonFrequency.get(2);
		r3F = reasonFrequency.get(3);
		r4F = reasonFrequency.get(4);
		r5F = reasonFrequency.get(5);
		r6F = reasonFrequency.get(6);

		System.out.println("female: " + r1F + " " + r2F + " " + r3F + " " + r4F + " " + r5F + " " + r6F);
        // add to a pie chart
		ObservableList<Data> listF = FXCollections.observableArrayList(new PieChart.Data("Borrow Stapler", r1F),
				new PieChart.Data("Visit Nereshnee", r2F), new PieChart.Data("Visit Kim", r3F), new PieChart.Data("Complain", r4F),
				new PieChart.Data("Collect Assignment", r5F), new PieChart.Data("Others", r6F));
		fPie.setData(listF);
        // get data from DB
		Map<Integer, Integer> reasonFrequency2 = reasonFrequency(date1, date2, "M");
		r1M = reasonFrequency2.get(1);
		r2M = reasonFrequency2.get(2);
		r3M = reasonFrequency2.get(3);
		r4M = reasonFrequency2.get(4);
		r5M = reasonFrequency2.get(5);
		r6M = reasonFrequency2.get(6);
		System.out.println("male:" + r1M + " " + r2M + " " + r3M + " " + r4M + " " + r5M + " " + r6M);
		//add to a pie chart
		ObservableList<Data> listF2 = FXCollections.observableArrayList(new PieChart.Data("Borrow Stapler", r1M),
				new PieChart.Data("Visit Nereshnee", r2M), new PieChart.Data("Visit Kim", r3M), new PieChart.Data("Complain", r4M),
				new PieChart.Data("Collect Assignment", r5M), new PieChart.Data("Others", r6M));
		mPie.setData(listF2);

	}

	@FXML  // quit
	void Back(ActionEvent event) {
		Stage stage = (Stage) frePane.getScene().getWindow();
		stage.close();
	}
	
	// display reason bar chart
	public void ReasonBarChart() {
		int r1 = 0, r2 = 0, r3 = 0, r4 = 0, r5 = 0, r6 = 0;
		Date date1 = transformDate(startDate.getValue());
		Date date2 = transformDate(endDate.getValue());
		
		Map<Integer, Integer> reasonFrequency = reasonFrequency(date1, date2);		
		r1 = reasonFrequency.get(1);
		r2 = reasonFrequency.get(2);
		r3 = reasonFrequency.get(3);
		r4 = reasonFrequency.get(4);
		r5 = reasonFrequency.get(5);
		r6 = reasonFrequency.get(6);
		System.out.println("total:" + r1 + " " + r2 + " " + r3 + " " + r4 + " " + r5 + " " + r6);
		// add to barchart		
		XYChart.Series set = new XYChart.Series<>();
		set.getData().addAll(
				new XYChart.Data("Borrow Stapler",r1),
				new XYChart.Data("Visit Nereshnee",r2),
				new XYChart.Data("Visit Kim",r3),
				new XYChart.Data("Complain",r4),
				new XYChart.Data("Collect Assignment",r5),
				new XYChart.Data("Others",r6)
				);
		set.setName("amount of visit");
		barChart.getData().clear();
		barChart.getData().addAll(set);
		
		
	}

	/**
	 * @param startDate
	 * @param endDate
	 * @return Integer1: Visit reason Integer2: How many of this reason
	 */
	
	private Map<Integer, Integer> reasonFrequency(Date startDate, Date endDate) {
		Map<Integer, Integer> res = new HashMap<>();
		for (int i = 1; i <= 6; i++)
			res.put(i, 0);
		Map<String, Map<Integer, List<Date>>> allVisits = DAO.getAllVisits();
		for (String id : allVisits.keySet()) {
			Map<Integer, List<Date>> visits = allVisits.get(id);
			for (int reason : visits.keySet()) {
				int newFre = res.get(reason);
				List<Date> dates = visits.get(reason);
				for (Date date : dates) {
					if (date.getTime() >= startDate.getTime() && date.getTime() <= endDate.getTime()) {
						newFre++;
					}
				}
				res.put(reason, newFre);
			}
		}
		return res;
	}

	private Map<Integer, Integer> reasonFrequency(Date startDate, Date endDate, String gender) {
		Map<Integer, Integer> res = new HashMap<>();
		for (int i = 1; i <= 6; i++)
			res.put(i, 0);
		Map<String, Map<Integer, List<Date>>> allVisits = DAO.getAllVisits();
		for (String id : allVisits.keySet()) {
			if (!DAO.getStudentByID(id).getGender().equalsIgnoreCase(gender))
				continue;
			Map<Integer, List<Date>> visits = allVisits.get(id);
			for (int reason : visits.keySet()) {
				int newFre = res.get(reason);
				List<Date> dates = visits.get(reason);
				for (Date date : dates) {
					if (date.getTime() >= startDate.getTime() && date.getTime() <= endDate.getTime()) {
						newFre++;
					}
				}
				res.put(reason, newFre);
			}
		}
		return res;
	}
}
