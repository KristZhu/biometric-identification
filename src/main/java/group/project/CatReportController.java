package group.project;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

public class CatReportController {

	@FXML
	private BarChart<?, ?> barChart;

	@FXML
	private AnchorPane catPane;

	@FXML
	private Button back;
	@FXML
	private CategoryAxis x;
	@FXML
	private NumberAxis y;
	
	
	@FXML
	private void initialize() {
		int r1 = 0, r2 = 0, r3 = 0, r4 = 0, r5 = 0, r6 = 0;
		// Student need a getReason method 
				
		XYChart.Series set = new XYChart.Series<>();
		set.getData().addAll(
				new XYChart.Data("Reason 1",r1),
				new XYChart.Data("Reason 2",r2),
				new XYChart.Data("Reason 3",r3),
				new XYChart.Data("Reason 4",r4),
				new XYChart.Data("Reason 5",r5),
				new XYChart.Data("Reason 6",r6)
				);
		barChart.getData().addAll(set);
		
	}

	@FXML
	void Back(ActionEvent event) {

	}

}
