package group.project;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Student {
	private byte[] feature;
	private String id;
	private String name;
	private String gender; // M F
	private String major; // MISM MSPPS MSIT
	private String grade; // 1 2
	private Map<Integer, List<Date>> visits = new HashMap<Integer, List<Date>>();

	public Student(byte[] feature, int reason) {
		// TODO Auto-generated constructor stub
		this(feature, "1", "Tara", "F", "MISM", "1");
		this.putVisit(reason); // 123456
		// this.visits.put(date, "complaint");
	}

	public Student(byte[] feature, String id, String name, String gender, String major, String grade) {
		// TODO Auto-generated constructor stub
		this.feature = feature;
		this.id = id;
		this.name = name;
		this.gender = gender;
		this.major = major;
		this.grade = grade;
		visits.put(1, new LinkedList<Date>());
		visits.put(2, new LinkedList<Date>());
		visits.put(3, new LinkedList<Date>());
		visits.put(4, new LinkedList<Date>());
		visits.put(5, new LinkedList<Date>());
		visits.put(6, new LinkedList<Date>());
	}

	public Student(byte[] feature, String id, String name, String gender, String major, String grade,
			Map<Integer, List<Date>> visits) {
		// TODO Auto-generated constructor stub
		this.feature = feature;
		this.id = id;
		this.name = name;
		this.gender = gender;
		this.major = major;
		this.grade = grade;
		this.visits = visits;
	}

	public byte[] getFeature() {
		return feature;
	}

	public void setFeature(byte[] feature) {
		this.feature = feature;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getMajor() {
		return major;
	}

	public void setMajor(String major) {
		this.major = major;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public Map<Integer, List<Date>> getAllVisits() {
		return visits;
	}

	public void putVisit(int reason, Date date) {
		List<Date> dates = visits.get(reason);
		dates.add(date);
		visits.put(reason, dates);
	}

	// preferred
	public void putVisit(int reason) {
		List<Date> dates = visits.get(reason);
		dates.add(new Date());
		visits.put(reason, dates);
	}

	public void setVisits(Map<Integer, List<Date>> visits) {
		this.visits = visits;
	}

	@Override
	public String toString() {
		DateFormat d = DateFormat.getDateInstance();
		return "ID: " + id + "\n" + "Name: " + name + "\n" + "Major: " + major + "\n" + "Grade: " + grade + "\n"
				+ "Gender: " + gender; // date reason
	}
}
