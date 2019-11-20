package group.project;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * A class for controlling the student info window
 *
 * @author ruby
 */
public class StudentInfoController {

    /**
     * The label major
     */
    @FXML
    private Label majorLabel;

    /**
     * The label name
     */
    @FXML
    private Label nameLabel;

    /**
     * The value of the name of this student
     */
    @FXML
    private Label nameText;

    /**
     * The label ID
     */
    @FXML
    private Label studentID;

    /**
     * The gender of this student
     */
    @FXML
    private Label gender;

    /**
     * The year of this student
     */
    @FXML
    private Label year;

    /**
     * The major of this student
     */
    @FXML
    private Label major;

    /**
     * The scale of the anger emotion of this student
     */
    @FXML
    private Label AngerScale1;

    /**
     * The scale of the surprise emotion of this student
     */
    @FXML
    private Label SurpriseScale1;

    /**
     * The scale of the happiness emotion of this student
     */
    @FXML
    private Label HappyScale1;

    /**
     * The scale of the sadness emotion of this student
     */
    @FXML
    private Label SadScale1;

    /**
     * The bar chart of this student data
     */
    @FXML
    private BarChart<?, ?> barChart;

    /**
     * The place to display the image of current student
     */
    @FXML
    private ImageView imageview;

    /**
     * The visits time of this student
     */
    @FXML
    private Label VisitTimes;

    /**
     * The last visit time fo this student
     */
    @FXML
    private Label lastVisitTime;

    /**
     * The message for this student
     */
    @FXML
    private Label message;

    /**
     * The alert for this student
     */
    @FXML
    private Label alert;

    /**
     * The ID of this student (to be set by other controller)
     */
    private String ID;

    /**
     * The emotions of this student (to be set by other controller)
     */
    private HashMap<String, String> emotions;

    /**
     * A method to be called by other controller to pass the emotions to this
     * controller
     *
     * @param emos The emotions of this student
     */
    public void saveEmotions(HashMap<String, String> emos) {
        emotions = emos;
    }

    /**
     * A method to be called by other controller to pass the ID to this
     * controller
     *
     * @param id The ID of this student
     */
    public void setStudentID(String id) {
        // get Student by this ID
        ID = id;
        Student s1 = DAO.getStudentByID(ID);

        //set info
        studentID.setText(s1.getId());
        nameText.setText(s1.getName());
        gender.setText(s1.getGender());
        year.setText(s1.getGrade());
        major.setText(s1.getMajor());
        VisitTimes.setText("" + getVisitsAmount(s1));

        Date lastVisitDate = lastVisit(s1);
        if (lastVisitDate != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(lastVisitDate);

            lastVisitTime.setText("" + calendar.get(Calendar.DAY_OF_MONTH) + "/"
                    + (calendar.get(Calendar.MONTH) + 1) + "/"
                    + calendar.get(Calendar.YEAR));
        } else {
            lastVisitTime.setText("");
        }
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
            File file = new File(getClass().getClassLoader().getResource("image/" + ID + ".jpg").getFile());
            Image img = new Image(file.toURI().toString());
            imageview.setImage(img);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * A method to generate alert message randomly
     *
     * @return
     */
    private String genMessage() {
        String[] strList = {"You have a JAVA quiz tommorow!",
            "Remember to respond Prof.Murli",
            "Have a meeting with Riaz",
            "Join SRC Christmas Party!",
            "Reserve the Data mining Speech"};
        int i = (int) (Math.random() * 5);
        return strList[i];
    }

    /**
     * A method to display the bar chart of frequency per visit reason of this
     * student
     *
     * @param s Current student
     */
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

        // set bar chart data
        XYChart.Series set = new XYChart.Series<>();
        set.getData().addAll(
                new XYChart.Data("1", r1),
                new XYChart.Data("2", r2),
                new XYChart.Data("3", r3),
                new XYChart.Data("4", r4),
                new XYChart.Data("5", r5),
                new XYChart.Data("6", r6)
        );
        set.setName("Amount of Visit");
        barChart.getData().clear();
        barChart.getData().addAll(set);
    }

    /**
     * Get the last visit time
     *
     * @param student The student to be analysed
     * @return The time of last visit
     */
    private static Date lastVisit(Student student) {
        List<Date> dates = new ArrayList<Date>();
        for (int i = 1; i <= 6; i++) {
            dates.addAll(student.getAllVisits().get(i));
        }
        Collections.sort(dates);
        if (dates.size() > 1) {
            return dates.get(dates.size() - 2);
        } else {
            return null;
        }
    }

    /**
     * A method to get the total amount of visit
     *
     * @param student The student to be analysed
     * @return The number of visit
     */
    private static int getVisitsAmount(Student student) {
        int amount = 0;
        Map<Integer, List<Date>> visits = student.getAllVisits();
        for (int i : visits.keySet()) {
            amount += visits.get(i).size();
        }
        return amount;
    }

    /**
     * A method to get frequency of each visit reason
     *
     * @param student The student to be analysed
     * @return A map with the visit reason index as the key and the frequency as
     * the value
     */
    private static Map<Integer, Integer> getEachVisitAmount(Student student) {
        Map<Integer, Integer> eachVisitAmount = new HashMap<Integer, Integer>();
        Map<Integer, List<Date>> visits = student.getAllVisits();
        System.out.println("getEachVisitAmountDebug2: " + visits);
        for (int i : visits.keySet()) {
            eachVisitAmount.put(i, visits.get(i).size());
        }
        System.out.println("getEachVisitAmountDebug3: " + eachVisitAmount);
        return eachVisitAmount;
    }
}
