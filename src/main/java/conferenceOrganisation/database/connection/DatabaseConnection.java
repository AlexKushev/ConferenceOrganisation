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

	private Connection createConnection() throws IOException, SQLException {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("No driver!");
		}

		Connection connection = null;

		Properties prop = new Properties();
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream input = classLoader
				.getResourceAsStream("conferenceOrganisation/database/connection/config.properties");

		prop.load(input);

		String databaseUrl = String.format("jdbc:mysql://%s:%s/%s", prop.getProperty("databaseNetworkAddress"),
				prop.getProperty("databasePort"), prop.getProperty("databaseName"));

		connection = DriverManager.getConnection(databaseUrl, prop.getProperty("databaseUserAccount"),
				prop.getProperty("databasePassword"));

		return connection;
	}

	public Statement createStatement() throws SQLException, IOException {

		Connection connection = createConnection();
		while (connection == null) {
			System.out.println("Connection failed.");
			connection = createConnection();
		}

		System.out.println("Connection successfuly to the database.");
		return connection.createStatement();
	}
}
