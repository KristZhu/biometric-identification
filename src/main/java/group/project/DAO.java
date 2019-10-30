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
		String sql = "CREATE TABLE Students " +
			"(" +
			"feature BLOB, " +
			"id VARCHAR(10), " +
			"name VARCHAR(50), " +
			"gender VARCHAR(1), " +
			"major VARCHAR(10), " +
			"grade VARCHAR(5), " +
			"visits VARCHAR(1000)" +
			")";
		try(Connection conn = DriverManager.getConnection(DB_URL);
			Statement stmt = conn.createStatement()) {
			boolean resultset = stmt.execute(sql);
			System.out.println("Table Students created");
		} catch(SQLException e) {
			// not a table exists error
			if(!e.getSQLState().equals("X0Y32")) {
				e.printStackTrace();
				System.exit(1);
			} else {
				System.out.println("Table Students already exists");
			}
		} catch(Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
    
	public static List<Student> getAllStudents() {
		List<Student> students = new LinkedList<Student>();
		try(Connection conn = DriverManager.getConnection(DB_URL);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Students")) {
            while (rs.next()) {
				//Blob featureBlob = rs.getBlob(1);
				Student student = new Student(
						//featureBlob.getBytes(0, featureBlob.length()),
						rs.getBytes(1),
						rs.getString(2),
						rs.getString(3),
						rs.getString(4),
						rs.getString(5),
						rs.getString(6),
						stringToMap(rs.getString(7)));
				students.add(student);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return students;
	}
	
	public static void insertStudent(Student student) {
		String sql = "INSERT INTO Students VALUES (?, "
				+ "'" + student.getId() + "' , "
				+ "'" + student.getName() + "' , "
				+ "'" + student.getGender() + "' , "
				+ "'" + student.getMajor() + "' , "
				+ "'" + student.getGrade() + "' , "
				+ "'" + student.getAllVisits() + "')";
		try(Connection conn = DriverManager.getConnection(DB_URL);
            PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setBytes(1, student.getFeature());
            stmt.executeUpdate();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	public static void updateStudent(Student student) {
		String sql = "UPDATE Students SET feature = ?, "
				//+ "feature = '" + new String(student.getFeature()) + "' , "
				+ "name = '" + student.getName() + "' , "
				+ "gender = '" + student.getGender() + "' , "
				+ "major = '" + student.getMajor() + "' , "
				+ "grade = '" + student.getGrade() + "' , "
				+ "visit = '" + student.getAllVisits() + "'"
				+ "WHERE id = " + student.getId();
		try(Connection conn = DriverManager.getConnection(DB_URL);
            PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setBytes(1, student.getFeature());
            stmt.executeUpdate();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	private static String dateToString (Date date) {
		 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 String dateString = formatter.format(date);
		 return dateString; 
	}
	private static Date stringToDate(String strDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		ParsePosition pos = new ParsePosition(0);
		java.util.Date strtodate = formatter.parse(strDate, pos);
		return strtodate;
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
