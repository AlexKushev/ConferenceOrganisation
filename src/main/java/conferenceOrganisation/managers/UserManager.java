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
import conferenceOrganisation.models.Ticket;
import conferenceOrganisation.models.User;
import conferenceOrganisation.services.CurrentUser;
import conferenceOrganisation.utils.Utils;

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
	CurrentUser currentUser;

	@Inject
	DatabaseConnection dbConnection;

	public void addUser(User user) throws SQLException, IOException {
		String firstName = user.getFirstName();
		String lastName = user.getLastName();
		String email = user.getEmail();
		String password = user.getPassword();
		String txtQuery = String.format(
				"insert into users(firstName, lastName, email, password, isAdmin) values ('%s', '%s', '%s', '%s', %d)",
				firstName, lastName, email, Utils.getHashedPassword(password), 0);
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
				newFirstName, newLastName, newEmail, Utils.getHashedPassword(newPassword), user.getUserId());
		Statement statement = dbConnection.createStatement();
		statement.executeUpdate(txtQuery);
		statement.close();
	}

	public void editEmail(User user) throws SQLException, IOException {
		String txtQuery = String.format("update users set users.email='%s' where users.userId=%d", user.getEmail(),
				user.getUserId());
		Statement statement = dbConnection.createStatement();
		statement.executeUpdate(txtQuery);
		statement.close();
	}

	public void editPassword(User user) throws SQLException, IOException {
		String txtQuery = String.format("update users set users.password='%s' where users.userId=%d",
				Utils.getHashedPassword(user.getPassword()), user.getUserId());
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
				Utils.getHashedPassword(password));
		Statement statement = dbConnection.createStatement();
		ResultSet rss = statement.executeQuery(txtQuery);

		while (rss.next()) {
			user = loadUserProperties(rss);
			currentUser.setCurrentUser(user);
		}
		statement.close();
		return user;

	}

	public User getUserByEmail(String email) throws SQLException, IOException {
		User user = null;
		String txtQuery = String.format("select * from users where users.email='%s'", email);
		Statement statement = dbConnection.createStatement();
		ResultSet rs = statement.executeQuery(txtQuery);

		while (rs.next()) {
			user = loadUserProperties(rs);
		}
		statement.close();
		return user;
	}

	public String changeUserPassword(String email) throws SQLException, IOException {
		User user = getUserByEmail(email);
		if (user == null) {
			return null;
		} else {
			String newPassword = Utils.generateRandomPassword();
			String txtQuery = String.format("update users set users.password='%s' where users.email='%s'",
					Utils.getHashedPassword(newPassword), email);
			Statement statement = dbConnection.createStatement();
			statement.executeUpdate(txtQuery);
			statement.close();
			return newPassword;
		}
	}

	public boolean canCurrentUserBuyTicketForEvent(int eventId) throws SQLException, IOException {
		int userId = currentUser.getCurrentUser().getUserId();
		String txtQuery = String.format("select * from tickets where tickets.ownerId=%d AND tickets.eventId=%d", userId,
				eventId);
		Statement statement = dbConnection.createStatement();
		ResultSet rs = statement.executeQuery(txtQuery);
		Ticket ticket = null;
		while (rs.next()) {
			ticket = ticketManager.loadTicketProperties(rs);
		}
		if (ticket == null) {
			return true;
		}
		return false;
	}

	public boolean checkUserByIdAndPassword(int userId, String password) throws SQLException, IOException {
		String txtQuery = String.format("select * from users where users.userId=%d AND users.email='%s'", userId,
				Utils.getHashedPassword(password));
		Statement statement = dbConnection.createStatement();
		ResultSet rs = statement.executeQuery(txtQuery);
		User user = null;
		while (rs.next()) {
			user = loadUserProperties(rs);
		}
		if (user == null) {
			return false;
		}
		return true;
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
