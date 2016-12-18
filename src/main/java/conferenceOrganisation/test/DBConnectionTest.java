package conferenceOrganisation.test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnectionTest {

	public static void main(String[] args) throws SQLException, IOException {

		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("No driver!");
		}

		Connection connection = null;

		try {
			String databaseUrl = "jdbc:mysql://localhost:3306/conferenceOrganisation";

			connection = DriverManager.getConnection(databaseUrl, "root", "1013");
		} catch (SQLException e) {
			System.out.println("Connection failed.");
		}

		if (connection != null) {
			System.out.println("Connected successfuly to the database.");
		} else {
			System.out.println("Connection failed.");
		}

	}

}
