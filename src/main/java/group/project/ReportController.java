package group.project;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * The controller class of the students report window
 *
 * @author ruby
 */
public class ReportController {

    /**
     * The root element of the window
     */
    @FXML
    private AnchorPane frePane;

    /**
     * The date picker for setting the start of the time period
     */
    @FXML
    private DatePicker startDate;

    /**
     * The date picker for setting the end of the time period
     */
    @FXML
    private DatePicker endDate;

    /**
     * The pie chart to show the portion per visit reasons for female students
     */
    @FXML
    private PieChart fPie;

    /**
     * The pie chart to show the portion per visit reasons for male students
     */
    @FXML
    private PieChart mPie;

    /**
     * The bar chart to show the frequency per visit reasons of all students
     */
    @FXML
    private BarChart<?, ?> barChart;

    /**
     * A method that will be called by JavaFX when this controller is
     * initialized. Set time period to be from 16 Nov 2019 to 30 Nov 2019 and
     * display the charts
     */
    @FXML
    private void initialize() {
        startDate.setValue(Localdate("16-11-2019"));
        startDate.setShowWeekNumbers(true);
        endDate.setValue(Localdate("30-11-2019"));
        // endDate.setShowWeekNumbers(true);
        ReasonPieChart();
        ReasonBarChart();

    }

    /**
     * A method to convert a string of date to a LocalDate object
     *
     * @param dateString The date string
     * @return The LocalDate representation
     */
    public static final LocalDate Localdate(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate localDate = LocalDate.parse(dateString, formatter);
        return localDate;
    }

    /**
     * An event handler to update the charts when the date is changed
     *
     * @param event
     */
    @FXML
    void change(ActionEvent event) {
        ReasonPieChart();
        ReasonBarChart();
    }

    /**
     * A method to convert a LocalDate object to a Date object
     *
     * @param localDate The LocalDate to be converted
     * @return The Date resulted from the conversion
     */
    public Date transformDate(LocalDate localDate) {
        Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
        Date date = Date.from(instant);
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date2 = DAO.stringToDate(formatter.format(date));
        System.out.println(date2);
        return date2;
    }

    /**
     * A method to display the Pie charts of portion per visit reason per gender
     */
    public void ReasonPieChart() {
        // sort by date
        Date date1 = transformDate(startDate.getValue());
        Date date2 = transformDate(endDate.getValue());

        // get data form DB
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

    /**
     * A method to handle back button in the window
     *
     * @param event The back button event
     */
    @FXML
    void Back(ActionEvent event) {
        Stage stage = (Stage) frePane.getScene().getWindow();
        stage.close();
    }

    /**
     * A method to display the bar chart of frequency per visit reasons
     */
    public void ReasonBarChart() {
        int r1 = 0, r2 = 0, r3 = 0, r4 = 0, r5 = 0, r6 = 0;
        Date date1 = transformDate(startDate.getValue());
        Date date2 = transformDate(endDate.getValue());

        // get visits data from db
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
                new XYChart.Data("Borrow Stapler", r1),
                new XYChart.Data("Visit Nereshnee", r2),
                new XYChart.Data("Visit Kim", r3),
                new XYChart.Data("Complain", r4),
                new XYChart.Data("Collect Assignment", r5),
                new XYChart.Data("Others", r6)
        );
        set.setName("amount of visit");
        barChart.getData().clear();
        barChart.getData().addAll(set);

    }

    /**
     * A method to get the visit reasons frequency from db within a specified
     * time period
     *
     * @param startDate The start of the time period
     * @param endDate The end of the time period
     * @return A Map with visit reason enum as the key and frequency as the
     * value
     */
    private Map<Integer, Integer> reasonFrequency(Date startDate, Date endDate) {
        Map<Integer, Integer> res = new HashMap<>();
        for (int i = 1; i <= 6; i++) {
            res.put(i, 0);
        }
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

    /**
     * A method to get the visit reasons frequency from db within a specified
     * time period (gender specific)
     *
     * @param startDate The start of the time period
     * @param endDate The end of the time period
     * @param gender The gender of the students considered in this visit data
     * @return A map with visit reason enum as the key and frequency as the
     * value
     */
    private Map<Integer, Integer> reasonFrequency(Date startDate, Date endDate, String gender) {
        Map<Integer, Integer> res = new HashMap<>();
        for (int i = 1; i <= 6; i++) {
            res.put(i, 0);
        }
        Map<String, Map<Integer, List<Date>>> allVisits = DAO.getAllVisits();
        for (String id : allVisits.keySet()) {
            if (!DAO.getStudentByID(id).getGender().equalsIgnoreCase(gender)) {
                continue;
            }
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
