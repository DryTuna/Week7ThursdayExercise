package resources;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

@Named(value = "courseName")
@ApplicationScoped
public class CourseNameJSFBean {
	private PreparedStatement studentStatement;
	private String choice;
	private String[] titles;
	
	public CourseNameJSFBean() {
		initializeJDBC();
	}
	
	public void initializeJDBC() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("Driver loaded");
			
			Connection conn = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/jpadb", "drytuna", "Pa$$word");
			
			PreparedStatement stmt = conn.prepareStatement(
					"select title from course");
			
			ResultSet res = stmt.executeQuery();
			
			ArrayList<String> list = new ArrayList<String>();
			while(res.next()) {
				list.add(res.getString(1));
			}
			
			titles = new String[list.size()];
			list.toArray(titles);
			
			studentStatement = conn.prepareStatement(
				"select Student.ssn, "
				+ "Student.firstName, Student.mi, Student.lastName, "
				+ "Student.phone, Student.birthDate, student.street, "
				+ "Student.zipCode, Student.deptId "
				+ "from Student, Enrollment, Course "
				+ "where Course.title = ? "
				+ "and Student.ssn = Enrollment.ssn "
				+ "and Enrollment.courseId = Course.courseId;");
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public String getChoice() {
		return choice;
	}

	public void setChoice(String choice) {
		this.choice = choice;
	}

	public String[] getTitles() {
		return titles;
	}

	public ResultSet getStudent() throws SQLException {
		if (choice == null) {
			if (titles.length == 0)
				return null;
			else
				studentStatement.setString(1, titles[0]);
		}
		else {
			studentStatement.setString(1, choice);
		}
		
		return studentStatement.executeQuery();
	}
	
	
}
