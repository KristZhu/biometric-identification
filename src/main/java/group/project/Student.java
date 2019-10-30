package group.project;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Student {
	private byte[] feature;
	private String id;
	private String name;
	private String gender;
	private String major;
	private String grade;
	private Map<Date, String> visits;
	public Student() {
		// TODO Auto-generated constructor stub
	}
	public Student(byte[] feature, String id, String name, String gender, String major, String grade, Map<Date, String> visits) {
		// TODO Auto-generated constructor stub
		this.feature=feature;
		this.id=id;
		this.name=name;
		this.gender=gender;
		this.major=major;
		this.grade=grade;
		this.visits=visits;
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
	public Map<Date, String> getAllVisits() {
		return visits;
	}
	public void putVisit(Date date, String reason) {
		this.visits.put(date, reason);
	}
	public void setVisits(Map<Date, String> visits) {
		this.visits=visits;
	}
}
