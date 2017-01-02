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
import conferenceOrganisation.models.CitiesContainer;
import conferenceOrganisation.models.Event;
import conferenceOrganisation.models.Hall;
import conferenceOrganisation.models.User;
import conferenceOrganisation.services.CurrentUser;
import conferenceOrganisation.utils.EventStatus;

@Singleton
public class EventManager {

	@Inject
	private HallManager hallManager;

	@Inject
	CurrentUser currentUser;

	@Inject
	LectureManager lectureManager;

	@Inject
	DatabaseConnection dbConnection;

	public boolean addEvent(Event event, Hall hall) throws SQLException, IOException {
		int hallId = hallManager.createHall(hall);
		hall.setHallId(hallId);
		User user = currentUser.getCurrentUser();
		int userId = user.getUserId();
		String title = event.getTitle();
		String description = event.getDescription();
		String date = event.getDate();
		double price = event.getPrice();
		int availableSeats = hallManager.getHallById(hallId).getCapacity();
		String txtQuery = String.format(
				"insert into events(creatorId, hallId, title, description, date, price, availableSeats, status) values (%d, %d, '%s', '%s', '%s', %s, %d)",
				userId, hallId, title, description, date, price, availableSeats, String.valueOf(EventStatus.NEW));
		Statement statement = null;
		try {
			statement = dbConnection.createStatement();
			statement.executeQuery(txtQuery);
			currentUser.getCurrentUser().setEvents(getAllEventsByUserId(userId));
		} catch (SQLException | IOException e) {
			return false;
		}
		statement.close();
		return true;
	}

	public List<Event> getAllEvents() throws SQLException, IOException {
		List<Event> events = new ArrayList<Event>();
		String txtQuery = "select * from events e";
		Statement statement = dbConnection.createStatement();
		ResultSet rs = statement.executeQuery(txtQuery);
		while (rs.next()) {
			Event event = loadEventProperties(rs);
			events.add(event);
		}
		statement.close();
		return events;
	}

	public List<Event> getAllPublishedEvents() throws SQLException, IOException {
		List<Event> events = new ArrayList<Event>();
		String txtQuery = String.format("select * from events where events.status='%s'",
				String.valueOf(EventStatus.PUBLISHED));
		Statement statement = dbConnection.createStatement();
		ResultSet rs = statement.executeQuery(txtQuery);
		while (rs.next()) {
			Event event = loadEventProperties(rs);
			events.add(event);
		}
		statement.close();
		return events;
	}

	public void sendEventForReview(int eventId) throws SQLException, IOException {
		String txtQuery = String.format("update events set status='%s' where events.eventId=%d",
				String.valueOf(EventStatus.PENDING), eventId);
		Statement statement = dbConnection.createStatement();
		statement.executeUpdate(txtQuery);
		statement.close();
	}

	public CitiesContainer getAllCytiesWithEvent() throws SQLException, IOException {
		CitiesContainer cyties = new CitiesContainer();
		String txtQuery = String.format(
				"select distinct(city) from halls where hallId IN (select hallId from events where events.status='%s')",
				String.valueOf(EventStatus.PUBLISHED));
		Statement statement = dbConnection.createStatement();
		ResultSet rs = statement.executeQuery(txtQuery);
		while (rs.next()) {
			cyties.addCity(rs.getString("city"));
		}
		return cyties;
	}

	public List<Event> getAllEventsByCity(String city) throws SQLException, IOException {
		List<Event> events = new ArrayList<Event>();
		String txtQuery = String.format(
				"select * from events where events.hallId IN (select hallId from halls where city='%s') AND events.status='%s'",
				city, String.valueOf(EventStatus.PUBLISHED));
		Statement statement = dbConnection.createStatement();
		ResultSet rs = statement.executeQuery(txtQuery);
		while (rs.next()) {
			Event event = loadEventProperties(rs);
			events.add(event);
		}
		statement.close();
		return events;
	}

	public Event getEventByEventId(int eventId) throws SQLException, IOException {
		Event event = null;
		String txtQuery = String.format("select * from events where events.eventId=%d", eventId);
		Statement statement = dbConnection.createStatement();
		ResultSet rs = statement.executeQuery(txtQuery);
		while (rs.next()) {
			event = loadEventProperties(rs);
		}
		return event;
	}

	public List<Event> getAllPendingEvents() throws SQLException, IOException {
		List<Event> events = new ArrayList<Event>();
		String txtQuery = String.format("select * from events where events.status='%s'",
				String.valueOf(EventStatus.PENDING));
		Statement statement = dbConnection.createStatement();
		ResultSet rs = statement.executeQuery(txtQuery);
		while (rs.next()) {
			Event event = loadEventProperties(rs);
			events.add(event);
		}
		statement.close();
		return events;
	}

	public void acceptEvent(int eventId) throws SQLException, IOException {
		String txtQuery = String.format("update events set events.status='%s' where events.eventId=%d",
				String.valueOf(EventStatus.PUBLISHED), eventId);
		Statement statement = dbConnection.createStatement();
		statement.executeUpdate(txtQuery);
		statement.close();
	}

	public void declineEvent(int eventId) throws SQLException, IOException {
		String txtQuery = String.format("update events set events.status='%s' where events.eventId=%d",
				String.valueOf(EventStatus.NOT_APPROVED), eventId);
		Statement statement = dbConnection.createStatement();
		statement.executeUpdate(txtQuery);
		statement.close();
	}

	public List<Event> getAllEventsByUserId(int userId) throws SQLException, IOException {
		List<Event> events = new ArrayList<Event>();
		String txtQuery = String.format("select * from events where events.creatorId=%d", userId);
		Statement statement = dbConnection.createStatement();
		ResultSet rs = statement.executeQuery(txtQuery);
		while (rs.next()) {
			Event event = loadEventProperties(rs);
			events.add(event);
		}
		statement.close();
		return events;
	}

	private Event loadEventProperties(ResultSet rs) throws SQLException, IOException {
		Event event = new Event();
		event.setEventId(rs.getInt("eventId"));
		event.setHall(hallManager.getHallById(rs.getInt("hallId")));
		event.setCreatorId(rs.getInt("creatorId"));
		event.setHallId(rs.getInt("hallId"));
		event.setTitle(rs.getString("title"));
		event.setDescription(rs.getString("description"));
		event.setDate(rs.getString("date"));
		event.setPrice(rs.getDouble("price"));
		event.setAvailableSeats(rs.getInt("availableSeats"));
		event.setStatus(EventStatus.valueOf(rs.getString("status")));
		event.setLectures(lectureManager.getAllLectuersByEventId(event.getEventId()));
		return event;
	}

}
