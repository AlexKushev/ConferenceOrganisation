package conferenceOrganisation.managers;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Singleton;
import javax.inject.Inject;

import conferenceOrganisation.database.connection.DatabaseConnection;
import conferenceOrganisation.models.User;

@Singleton
public class UserManager {

	@Inject
	HallManager hallManager;

	@Inject
	TicketManager ticketManager;

	@Inject
	EventManager eventManager;

	@Inject
	LectureManager lectureManager;

	@Inject
	DatabaseConnection dbConnection;

	public void addUser(User user) throws SQLException, IOException {
		String firstName = user.getFirstName();
		String lastName = user.getLastName();
		String email = user.getEmail();
		String password = user.getPassword();
		// TODO add password encryption
		String txtQuery = String.format(
				"insert into users(firstName, lastName, email, password, isAdmin) values ('%s', '%s', '%s', '%s', %d)",
				firstName, lastName, email, password, 0);
		Statement statement;

		statement = dbConnection.createStatement();
		statement.executeUpdate(txtQuery);
		statement.close();

	}

	public void editUser(User user) throws SQLException, IOException {
		String newFirstName = user.getFirstName();
		String newLastName = user.getLastName();
		String newEmail = user.getEmail();
		String newPassword = user.getPassword();
		String txtQuery = String.format(
				"update users set users.firstName = '%s', users.lastName='%s', users.email='%s'"
						+ ", users.password = '%s' where users.userId=%d",
				newFirstName, newLastName, newEmail, newPassword, user.getUserId());
		Statement statement = dbConnection.createStatement();
		statement.executeUpdate(txtQuery);
		statement.close();
	}

	public List<User> getAllUsers() throws SQLException, IOException {
		String txtQuery = "select * from users u";
		Statement statement = dbConnection.createStatement();
		ResultSet rs = statement.executeQuery(txtQuery);
		List<User> users = new ArrayList<User>();
		while (rs.next()) {
			User user = loadUserProperties(rs);
			users.add(user);
		}
		statement.close();
		return users;
	}

	public User getUserById(int userId) throws SQLException, IOException {
		User user = null;
		String txtQuery = String.format("select * from users where users.userId=%d", userId);
		Statement statement = dbConnection.createStatement();
		ResultSet rs = statement.executeQuery(txtQuery);

		while (rs.next()) {
			user = loadUserProperties(rs);
		}
		statement.close();
		return user;
	}

	public User getUserByEmailAndPassword(String email, String password) throws SQLException, IOException {
		User user = null;
		String txtQuery = String.format("select * from users where users.email='%s' and users.password='%s'", email,
				password);
		Statement statement = dbConnection.createStatement();
		ResultSet rss = statement.executeQuery(txtQuery);

		while (rss.next()) {
			user = loadUserProperties(rss);
		}
		statement.close();
		return user;

	}

	private User loadUserProperties(ResultSet rs) throws SQLException, IOException {
		User user = new User();
		user.setUserId(rs.getInt("userId"));
		user.setFirstName(rs.getString("firstName"));
		user.setLastName(rs.getString("lastName"));
		user.setEmail(rs.getString("email"));
		user.setIsAdmin(rs.getInt("isAdmin"));
		user.setEvents(eventManager.getAllEventsByUserId(user.getUserId()));
		user.setTickets(ticketManager.getAllTicketsByUserId(user.getUserId()));
		return user;
	}
}
