package conferenceOrganisation.database.connection;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.ejb.Singleton;

@Singleton
public class DatabaseConnection {

	public Statement createStatement() throws SQLException, IOException {

		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("No driver!");
		}

		Connection connection = null;

		try {
			Properties prop = new Properties();
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			InputStream input = classLoader
					.getResourceAsStream("conferenceOrganisation/database/connection/config.properties");

			prop.load(input);

			String databaseUrl = String.format("jdbc:mysql://%s:%s/%s", prop.getProperty("databaseNetworkAddress"),
					prop.getProperty("databasePort"), prop.getProperty("databaseName"));

			connection = DriverManager.getConnection(databaseUrl, prop.getProperty("databaseUserAccount"),
					prop.getProperty("databasePassword"));
		} catch (SQLException e) {
			System.out.println("Connection failed");
		}

		if (connection != null) {
			System.out.println("Connected successfuly to the database.");
		} else {
			System.out.println("Connection failed.");
		}

		return connection.createStatement();
	}
	
	public Connection createConnection() throws IOException {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("No driver!");
		}

		Connection connection = null;

		try {
			Properties prop = new Properties();
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			InputStream input = classLoader
					.getResourceAsStream("conferenceOrganisation/database/connection/config.properties");

			prop.load(input);

			String databaseUrl = String.format("jdbc:mysql://%s:%s/%s", prop.getProperty("databaseNetworkAddress"),
					prop.getProperty("databasePort"), prop.getProperty("databaseName"));

			connection = DriverManager.getConnection(databaseUrl, prop.getProperty("databaseUserAccount"),
					prop.getProperty("databasePassword"));
		} catch (SQLException e) {
			System.out.println("Connection failed");
		}

		if (connection != null) {
			System.out.println("Connected successfuly to the database.");
		} else {
			System.out.println("Connection failed.");
		}
		
		return connection;
	}

}
