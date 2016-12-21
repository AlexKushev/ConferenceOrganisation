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
import conferenceOrganisation.models.Event;
import conferenceOrganisation.models.Lecture;
import conferenceOrganisation.models.User;
import conferenceOrganisation.utils.Utils;

@Singleton
public class UserManager {

	@Inject
	HallManager hallManager;

	@Inject
	DatabaseConnection dbConnection;

	public void addUser(User user) throws SQLException, IOException {
		String firstName = user.getFirstName();
		String lastName = user.getLastName();
		String email = user.getEmail();
		String password = user.getPassword();
		String txtQuery = String.format(
				"insert into users(firstName, lastName, email, password) values ('%s', '%s', '%s', '%s')", firstName,
				lastName, email, Utils.getHashedPassword(password));
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
			User user = new User();
			user.setFirstName(rs.getString("firstName"));
			user.setLastName(rs.getString("lastName"));
			user.setEmail(rs.getString("email"));
			user.setUserId(Integer.parseInt(rs.getString("userId")));
			user.setEvents(initUserEventsByUserId(user.getUserId()));
			user.setLectures(initUserLecturesByUserId(user.getUserId()));
			users.add(user);
		}
		statement.close();
		return users;
	}

	public User getUserByEmailAndPassword(String email, String password) throws SQLException, IOException {
		User user = new User();
		String txtQuery = String.format("select * from users where users.email='%s' and users.password='%s'", email,
				Utils.getHashedPassword(password));
		Statement statement = dbConnection.createStatement();
		ResultSet rss = statement.executeQuery(txtQuery);

		while (rss.next()) {
			user.setUserId(rss.getInt("userId"));
			user.setFirstName(rss.getString("firstName"));
			user.setLastName(rss.getString("lastName"));
			user.setEmail(rss.getString("email"));
			user.setEvents(initUserEventsByUserId(user.getUserId()));
			user.setLectures(initUserLecturesByUserId(user.getUserId()));
		}
		statement.close();
		return user;

	}

	public List<Event> initUserEventsByUserId(int userId) throws SQLException, IOException {
		List<Event> events = new ArrayList<Event>();
		String txtQuery = String.format("select * from events where events.creatorId=%s", String.valueOf(userId));
		Statement statement = dbConnection.createStatement();
		ResultSet rss = statement.executeQuery(txtQuery);
		while (rss.next()) {
			Event event = new Event();
			event.setEventId(rss.getInt("eventId"));
			event.setHall(hallManager.getHallById(rss.getInt("hallId")));
			event.setCreatorId(rss.getInt("creatorId"));
			event.setHallId(rss.getInt("hallId"));
			event.setTitle(rss.getString("title"));
			event.setDescription(rss.getString("description"));
			event.setDate(rss.getString("date"));
			event.setPrice(rss.getDouble("price"));
			event.setAvailableSeats(rss.getInt("availableSeats"));
			events.add(event);
		}

		return events;
	}

	public List<Lecture> initUserLecturesByUserId(int userId) throws SQLException, IOException {
		List<Lecture> lectures = new ArrayList<Lecture>();
		String txtQuery = String.format("select * from lectures where lectures.lecturerId=%d", userId);
		Statement statement = dbConnection.createStatement();
		ResultSet rs = statement.executeQuery(txtQuery);
		while (rs.next()) {
			Lecture lecture = new Lecture();
			lecture.setLectureId(rs.getInt("lectureId"));
			lecture.setEventId(rs.getInt("eventId"));
			lecture.setLecturerId(rs.getInt("lecturerId"));
			lecture.setTitle(rs.getString("title"));
			lecture.setDescription(rs.getString("description"));
			lectures.add(lecture);
		}
		statement.close();
		return lectures;
	}

}
