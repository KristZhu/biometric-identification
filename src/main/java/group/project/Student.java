package group.project;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.bytedeco.librealsense.intrinsics;

public class Student {
	private byte[] feature;
	private String id;
	private String name;
	private String gender; // M F
	private String major; // MISM MSPPS MSIT
	private String grade; // 1 2
	private Map<Integer, List<Date>> visits = new HashMap<Integer, List<Date>>();
	
	 private Student() {
		  this("123".getBytes(), "127", "Krist", "f", "MISM", "1");
		  Date d1 = new Date();
		  try { Thread.sleep(1000); } catch (Exception e) { }
		  Date d2 = new Date();
		  try { Thread.sleep(1000); } catch (Exception e) { }
		  Date d3 = new Date();
		  try { Thread.sleep(1000); } catch (Exception e) { }
		  Date d4 = new Date();
		  try { Thread.sleep(1000); } catch (Exception e) { }
		  Date d5 = new Date();
		  List<Date> list1 = new LinkedList<Date>();
		  list1.add(d1); list1.add(d2);
		  List<Date> list2 = new LinkedList<Date>();
		  list2.add(d3); list2.add(d4); list2.add(d5);
		  Map<Integer, List<Date>> visits = new HashMap<Integer, List<Date>>();
		  visits.put(1, list1);
		  visits.put(2, list2);
		  visits.put(3, list1);
		  visits.put(4, list2);
		  visits.put(5, list2);
		  visits.put(6, list1);
		  this.visits=visits;
	 }

	public Student(byte[] feature, String id, String name, String gender, String major, String grade) {
		// TODO Auto-generated constructor stub
		this.feature = feature;
		this.id = id;
		this.name = name;
		this.gender = gender;
		this.major = major;
		this.grade = grade;
		for(int i=1; i<=6; i++) visits.put(i, new LinkedList<Date>());
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
