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
import conferenceOrganisation.utils.Utils;

@Singleton
public class UserManager {

	private Statement statement = DatabaseConnection.statement;

	public void addUser(User user) throws SQLException {
		String firstName = user.getFirstName();
		String lastName = user.getLastName();
		String email = user.getEmail();
		String password = user.getPassword();
		String txtQuery = String.format(
				"insert into users(firstName, lastName, email, password) values ('%s', '%s', '%s', '%s')", firstName,
				lastName, email, Utils.getHashedPassword(password));
		statement.executeUpdate(txtQuery);
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

	public User getUserByEmailAndPassword(String email, String password) throws SQLException {
		User user = new User();
		String txtQuery = String.format("select * from users where users.email='%s' and users.password='%s'", email,
				Utils.getHashedPassword(password));
		ResultSet rs = statement.executeQuery(txtQuery);
		if (!rs.next()) {
			return null;
		} else {
			while (rs.next()) {
				user.setUserId(rs.getInt("userId"));
				user.setFirstName(rs.getString("firstName"));
				user.setLastName(rs.getString("lastName"));
				user.setEmail(rs.getString("email"));
			}
			return user;
		}
	}

}
