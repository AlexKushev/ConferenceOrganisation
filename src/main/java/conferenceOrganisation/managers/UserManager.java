package conferenceOrganisation.managers;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Singleton;

import conferenceOrganisation.database.connection.DatabaseConnection;
import conferenceOrganisation.models.Event;
import conferenceOrganisation.models.User;
import conferenceOrganisation.utils.Utils;

@Singleton
public class UserManager {

	private Statement statement = DatabaseConnection.statement;
	private Statement secondStatement = DatabaseConnection.secondStatement;

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
			user.setEvents(initUserEventsByUserId(user.getUserId()));
			users.add(user);
		}
		return users;
	}

	public User getUserByEmailAndPassword(String email, String password) throws SQLException {
		User user = new User();
		String txtQuery = String.format("select * from users where users.email='%s' and users.password='%s'", email,
				Utils.getHashedPassword(password));
		ResultSet rss = statement.executeQuery(txtQuery);
		if (!rss.next()) {
			return null;
		} else {
			while (rss.next()) {
				user.setUserId(rss.getInt("userId"));
				user.setFirstName(rss.getString("firstName"));
				user.setLastName(rss.getString("lastName"));
				user.setEmail(rss.getString("email"));
				user.setEvents(initUserEventsByUserId(user.getUserId()));
			}
			return user;
		}
	}

	public List<Event> initUserEventsByUserId(int userId) throws SQLException {
		List<Event> events = new ArrayList<Event>();
		String txtQuery = String.format("select * from events where events.creatorId=%s", String.valueOf(userId));
		ResultSet rss = secondStatement.executeQuery(txtQuery);
		if (rss.next()) {
			while (rss.next()) {
				Event event = new Event();
				event.setEventId(rss.getInt("eventId"));
				event.setCreatorId(rss.getInt("creatorId"));
				event.setHallId(rss.getInt("hallId"));
				event.setTitle(rss.getString("title"));
				event.setDescription(rss.getString("description"));
				event.setDate(rss.getString("date"));
				event.setPrice(rss.getDouble("price"));
				event.setAvailableSeats(rss.getInt("availableSeats"));
				events.add(event);
			}
		}
		return events;
	}

}
