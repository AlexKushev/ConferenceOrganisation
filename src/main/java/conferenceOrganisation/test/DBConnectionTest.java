package conferenceOrganisation.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnectionTest {

	public static void main(String[] args) {
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("No driver!");
		}
		
		Connection connection = null;
		
		try {
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/conferenceOrganisation", "root", "1013");
		} catch (SQLException e) {
			System.out.println("Connection failed");
		}
		
		if (connection != null) {
			System.out.println("Connected successfuly to the database.");
		} else {
			System.out.println("Connection failed.");
		}

	}

}
