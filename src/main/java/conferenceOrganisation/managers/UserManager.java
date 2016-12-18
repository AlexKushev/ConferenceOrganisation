package conferenceOrganisation.managers;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Singleton;

import conferenceOrganisation.database.connection.DatabaseConnection;
import conferenceOrganisation.models.User;

@Singleton
public class UserManager {
	
	private Statement statement = DatabaseConnection.statement;

	public void addUser(User user) throws SQLException {
		String firstName = user.getFirstName();
		String lastName = user.getLastName();
		String email = user.getEmail();
		String password = user.getPassword();
		String txtQuery = String.format(
				"insert into user(firstName, lastName, email, password) values ('%s', '%s', '%s', '%s')", firstName,
				lastName, email, password);
		// Statement statement.executeUpdate(txtQuery);
	}

	public List<User> getAllUsers() throws SQLException, IOException {
		String txtQuery = "select * from users u";
		ResultSet rs = statement.executeQuery(txtQuery);
		List<User> users = new ArrayList<User>();
		while (rs.next()) {
			User user = new User();
			user.setFirstName(rs.getString("firstName"));
			user.setLastName(rs.getString("lastName"));
			user.setEmail(rs.getString("email"));
			user.setUserId(Integer.parseInt(rs.getString("userId")));
			users.add(user);
		}
		return users;
	}

}
