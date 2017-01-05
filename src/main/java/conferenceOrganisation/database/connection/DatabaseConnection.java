package conferenceOrganisation.database.connection;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;

@Singleton
@Startup
public class DatabaseConnection {

	public Connection connection;

	@PostConstruct
	public void init() throws IOException {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("No driver!");
		}

		Connection newConnection = null;

		Properties prop = new Properties();
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream input = classLoader
				.getResourceAsStream("conferenceOrganisation/database/connection/config.properties");

		prop.load(input);

		String databaseUrl = String.format("jdbc:mysql://%s:%s/%s", prop.getProperty("databaseNetworkAddress"),
				prop.getProperty("databasePort"), prop.getProperty("databaseName"));

		try {
			newConnection = DriverManager.getConnection(databaseUrl, prop.getProperty("databaseUserAccount"),
					prop.getProperty("databasePassword"));
			System.out.println("Connection successfuly to the database.");
		} catch (SQLException e) {
			System.out.println("Connection failed");
		}

		this.connection = newConnection;
	}

	public Statement createStatement() throws SQLException, IOException {
		return connection.createStatement();
	}
}
