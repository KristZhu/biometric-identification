//https://www.cnblogs.com/laumians-notes/p/9069498.html

package group.project;

import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.HashMap;

public class DAO {
	static final String DB_URL = "jdbc:derby:StudentDB;create=true";

	static {
		System.out.println("Trying to create table Students ...");
		String sql = "CREATE TABLE Students " + "(" + "feature BLOB, " + "id VARCHAR(10) primary key, " + // primary
																											// key, test
																											// if works
				"name VARCHAR(50), " + "gender VARCHAR(1), " + "major VARCHAR(10), " + "grade VARCHAR(5) " + ")";
		try (Connection conn = DriverManager.getConnection(DB_URL); Statement stmt = conn.createStatement()) {
			boolean resultset = stmt.execute(sql);
			System.out.println("Table Students created");
		} catch (SQLException e) {
			// not a table exists error
			if (!e.getSQLState().equals("X0Y32")) {
				e.printStackTrace();
				System.exit(1);
			} else {
				System.out.println("Table Students already exists");
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	static {
		System.out.println("Trying to create table Visits ...");
		String sql = "CREATE TABLE Visits " + "(" + "studentID VARCHAR(10), " + "reason INTEGER, " + "date DATE " + ")";
		try (Connection conn = DriverManager.getConnection(DB_URL); Statement stmt = conn.createStatement()) {
			boolean resultset = stmt.execute(sql);
			System.out.println("Table Visits created");
		} catch (SQLException e) {
			// not a table exists error
			if (!e.getSQLState().equals("X0Y32")) {
				e.printStackTrace();
				System.exit(1);
			} else {
				System.out.println("Table Visits already exists");
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public static List<Student> getAllStudents() {
		List<Student> students = new LinkedList<Student>();
		try (Connection conn = DriverManager.getConnection(DB_URL);
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT * FROM Students")) {
			while (rs.next()) {
				// Blob featureBlob = rs.getBlob(1);
				Student student = new Student(
						// featureBlob.getBytes(0, featureBlob.length()),
						rs.getBytes(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),
						rs.getString(6));
				students.add(student);
			}
			for (Student student : students) {
				ResultSet rs2 = stmt.executeQuery("SELECT * FROM Visits WHERE studentID = '" + student.getId() + "'");
				Map<Integer, List<Date>> visits = student.getAllVisits();
				while (rs2.next()) {
					List<Date> dates = visits.get(rs2.getInt(2));
					dates.add(dateSqlToUtil(rs2.getDate(3)));
					visits.put(rs2.getInt(2), dates);
				}
				student.setVisits(visits);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return students;
	}

	public static Student getStudentByID(String id) {
		Student student = null;
		try (Connection conn = DriverManager.getConnection(DB_URL);
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT * FROM Students WHERE id = '" + id + "'")) {
			while (rs.next()) {
				// Blob featureBlob = rs.getBlob(1);
				student = new Student(
						// featureBlob.getBytes(0, featureBlob.length()),
						rs.getBytes(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),
						rs.getString(6));
			}
			ResultSet rs2 = stmt.executeQuery("SELECT * FROM Visits WHERE studentID = '" + id + "'");
			Map<Integer, List<Date>> visits = student.getAllVisits();
			while (rs2.next()) {
				List<Date> dates = visits.get(rs2.getInt(2));
				dates.add(dateSqlToUtil(rs2.getDate(3)));
				visits.put(rs2.getInt(2), dates);
			}
			student.setVisits(visits);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return student;
	}

	public static Map<String, Map<Integer, List<Date>>> getAllVisits() {
		Map<String, Map<Integer, List<Date>>> allVisits = new HashMap<>();
		try (Connection conn = DriverManager.getConnection(DB_URL);
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT * FROM Visits")) {
			while (rs.next()) {
				String id = rs.getString(1);
				int reason = rs.getInt(2);
				Date date = dateSqlToUtil(rs.getDate(3));
				Map<Integer, List<Date>> visits = allVisits.get(id);
				if (visits == null)
					visits = new HashMap<>();
				List<Date> dates = visits.get(reason);
				if (dates == null)
					dates = new LinkedList<>();
				dates.add(date);
				visits.put(reason, dates);
				allVisits.put(id, visits);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return allVisits;
	}

	public static void insertStudent(Student student) {
		String sql = "INSERT INTO Students VALUES (?, " + "'" + student.getId() + "' , " + "'" + student.getName()
				+ "' , " + "'" + student.getGender() + "' , " + "'" + student.getMajor() + "' , " + "'"
				+ student.getGrade() + "')";
		try (Connection conn = DriverManager.getConnection(DB_URL);
				PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setBytes(1, student.getFeature());
			stmt.executeUpdate();
			Map<Integer, List<Date>> visits = student.getAllVisits();
			for (int i : visits.keySet()) {
				for (Date date : visits.get(i)) {
					sql = "INSERT INTO Visits VALUES (" + "'" + student.getId() + "' , " + i + " , " + "'"
							+ dateUtilToSql(date) + "')";
					stmt.executeUpdate(sql);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public static void updateStudent(Student student) {
		  String sql = "UPDATE Students SET feature = ?, "
		    // + "feature = '" + new String(student.getFeature()) + "' , "
		    + "name = '" + student.getName() + "' , " + "gender = '" + student.getGender() + "' , " + "major = '"
		    + student.getMajor() + "' , " + "grade = '" + student.getGrade() + "' "
		    + "WHERE id = '" + student.getId() + "'";
		  try (Connection conn = DriverManager.getConnection(DB_URL);
		    PreparedStatement stmt = conn.prepareStatement(sql)) {
			   stmt.setBytes(1, student.getFeature());
			   stmt.executeUpdate();
			   Map<Integer, List<Date>> visits = student.getAllVisits();
			   sql = "DELETE FROM Visits WHERE studentID = '" + student.getId() + "'";
			   Statement stm = conn.createStatement();
		       stm.executeUpdate(sql);
		       for(int i: visits.keySet()) {
		            for(Date date: visits.get(i)) {
		             sql = "INSERT INTO Visits VALUES ("
		                       + "'" + student.getId() + "' , "
		                       + i + " , "
		                       + "'" + dateUtilToSql(date) + "')";
		             Statement st = conn.createStatement();
		             st.executeUpdate(sql);
		            }
		       }
		  } catch (Exception e) {
		   // TODO: handle exception
		   e.printStackTrace();
		  }
		 }

	public static void deleteStudentByID(String id) {
		String sql = "DELETE FROM Students WHERE id = ?";
		try (Connection conn = DriverManager.getConnection(DB_URL);
				PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, id);
			stmt.executeUpdate();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public static void deleteAllStudents() {
		String sql = "DELETE FROM Students";
		try (Connection conn = DriverManager.getConnection(DB_URL);
				PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.executeUpdate();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public static void deleteAllVisits() {
		String sql = "DELETE FROM Visits";
		try (Connection conn = DriverManager.getConnection(DB_URL);
				PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.executeUpdate();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	private static String dateToString(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(date);
		return dateString;
	}

	public static Date stringToDate(String strDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		ParsePosition pos = new ParsePosition(0);
		java.util.Date strtodate = formatter.parse(strDate, pos);
		return strtodate;
	}

	private static java.util.Date dateSqlToUtil(java.sql.Date date) {
		return new java.util.Date(date.getTime());
	}

	private static java.sql.Date dateUtilToSql(java.util.Date date) {
		return new java.sql.Date(date.getTime());
	}

	private static Map stringToMap(String param) {
		Map map = new HashMap();
		String str = "";
		String key = "";
		Object value = "";
		char[] charList = param.toCharArray();
		boolean valueBegin = false;
		for (int i = 0; i < charList.length; i++) {
			char c = charList[i];
			if (c == '{') {
				if (valueBegin == true) {
					value = stringToMap(param.substring(i, param.length()));
					i = param.indexOf('}', i) + 1;
					map.put(key, value);
				}
			} else if (c == '=') {
				valueBegin = true;
				key = str;
				str = "";
			} else if (c == ',') {
				valueBegin = false;
				value = str;
				str = "";
				map.put(key, value);
			} else if (c == '}') {
				if (str != "") {
					value = str;
				}
				map.put(key, value);
				return map;
			} else if (c != ' ') {
				str += c;
			}
		}
		return map;
	}
}
