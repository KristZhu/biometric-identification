package group.project;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * A class representing a Student object
 *
 * @author Krist
 */
public class Student {

    /**
     * The facial feature of this student
     */
    private byte[] feature;

    /**
     * The ID of this student
     */
    private String id;

    /**
     * The name of this student
     */
    private String name;

    /**
     * The gender of this student (M or F)
     */
    private String gender;

    /**
     * The major of this student (MISM, MSPPM, or MSIT)
     */
    private String major;

    /**
     * The grade/year of this student (1 or 2)
     */
    private String grade;

    /**
     * The visit data of this student. The key is the visit reason index and the
     * value is the list of dates this student visited with the corresponding
     * visit reason
     */
    private Map<Integer, List<Date>> visits = new HashMap<Integer, List<Date>>();

    /**
     * The constructor of this class.
     *
     * @param feature The facial feature of this student
     * @param id The ID of this student
     * @param name The name of this student
     * @param gender The gender of this student
     * @param major The major of this student
     * @param grade The grade/year of this student
     */
    public Student(byte[] feature, String id, String name, String gender, String major, String grade) {
        this.feature = feature;
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.major = major;
        this.grade = grade;
        for (int i = 1; i <= 6; i++) {
            visits.put(i, new LinkedList<Date>());
        }
    }

    /**
     * The constructor of this class when the visits are known.
     *
     * @param feature The facial feature of this student
     * @param id The ID of this student
     * @param name The name of this student
     * @param gender The gender of this student
     * @param major The major of this student
     * @param grade The grade/year of this student
     * @param visits The visits data of this student
     */
    public Student(byte[] feature, String id, String name, String gender, String major, String grade,
            Map<Integer, List<Date>> visits) {
        this.feature = feature;
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.major = major;
        this.grade = grade;
        this.visits = visits;
    }

    /**
     * A getter for the facial feature
     *
     * @return The facial feature of this student
     */
    public byte[] getFeature() {
        return feature;
    }

    /**
     * A setter for the facial feature
     *
     * @param feature The facial feature of this student
     */
    public void setFeature(byte[] feature) {
        this.feature = feature;
    }

    /**
     * A getter for the ID
     *
     * @return The ID of this student
     */
    public String getId() {
        return id;
    }

    /**
     * A setter for the ID
     *
     * @param id The ID of this student
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * A getter for the name
     *
     * @return The name of this student
     */
    public String getName() {
        return name;
    }

    /**
     * A setter for the name
     *
     * @param name The name of this student
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * A getter for the gender
     *
     * @return The gender of this student
     */
    public String getGender() {
        return gender;
    }

    /**
     * A setter for the gender
     *
     * @param gender The gender of this student
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * A getter for the major
     *
     * @return The major of this student
     */
    public String getMajor() {
        return major;
    }

    /**
     * A setter for the major
     *
     * @param major The major of this student
     */
    public void setMajor(String major) {
        this.major = major;
    }

    /**
     * A getter for the grade/year
     *
     * @return The grade/year of this student
     */
    public String getGrade() {
        return grade;
    }

    /**
     * A setter for the grade/year
     *
     * @param grade The grade/year of this student
     */
    public void setGrade(String grade) {
        this.grade = grade;
    }

    /**
     * A getter for the visits
     *
     * @return The visits data of this student
     */
    public Map<Integer, List<Date>> getAllVisits() {
        return visits;
    }

    /**
     * A method to add a new visit data
     *
     * @param reason The visit reason index
     * @param date The date of this new visit
     */
    public void putVisit(int reason, Date date) {
        List<Date> dates = visits.get(reason);
        dates.add(date);
        visits.put(reason, dates);
    }

    /**
     * A method to add a new visit data with current time as the visit date
     *
     * @param reason The visit reason index
     */
    public void putVisit(int reason) {
        List<Date> dates = visits.get(reason);
        dates.add(new Date());
        visits.put(reason, dates);
    }

    /**
     * A setter for the visits
     *
     * @param visits The visits data of this student
     */
    public void setVisits(Map<Integer, List<Date>> visits) {
        this.visits = visits;
    }

    /**
     * An overridden method of toString for printing the data of this student
     *
     * @return
     */
    @Override
    public String toString() {
        DateFormat d = DateFormat.getDateInstance();
        return "ID: " + id + "\n" + "Name: " + name + "\n" + "Major: " + major + "\n" + "Grade: " + grade + "\n"
                + "Gender: " + gender; // date reason
    }
}
