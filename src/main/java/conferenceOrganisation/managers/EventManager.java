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
				"insert into events(creatorId, hallId, title, description, date, price, availableSeats, isPublished) values (%d, %d, '%s', '%s', '%s', %s, %d)",
				userId, hallId, title, description, date, price, availableSeats, 0);
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
		String txtQuery = "select * from events where events.isPublished=1";
		Statement statement = dbConnection.createStatement();
		ResultSet rs = statement.executeQuery(txtQuery);
		while (rs.next()) {
			Event event = loadEventProperties(rs);
			events.add(event);
		}
		statement.close();
		return events;
	}

	public List<Event> getAllUnpublishedEvents() throws SQLException, IOException {
		List<Event> events = new ArrayList<Event>();
		String txtQuery = "select * from events where events.isPublished=0";
		Statement statement = dbConnection.createStatement();
		ResultSet rs = statement.executeQuery(txtQuery);
		while (rs.next()) {
			Event event = loadEventProperties(rs);
			events.add(event);
		}
		statement.close();
		return events;
	}

	public void makeEventPublish(int eventId) throws SQLException, IOException {
		String txtQuery = String.format("update events set isPublished = 1 where events.eventId=%d", eventId);
		Statement statement = dbConnection.createStatement();
		statement.executeUpdate(txtQuery);
		statement.close();
	}

	public List<Event> getAllEventsByUserId(int userId) throws SQLException, IOException {
		List<Event> events = new ArrayList<Event>();
		String txtQuery = String.format("select * from events where events.creatorId=%s", String.valueOf(userId));
		Statement statement = dbConnection.createStatement();
		ResultSet rs = statement.executeQuery(txtQuery);
		while (rs.next()) {
			Event event = loadEventProperties(rs);
			events.add(event);
		}

		return events;
	}

	public CitiesContainer getAllCytiesWithEvent() throws SQLException, IOException {
		CitiesContainer cyties = new CitiesContainer();
		String txtQuery = "select distinct(city) from halls where hallId IN (select hallId from events where events.isPublished=1)";
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
				"select * from events where events.hallId IN (select hallId from halls where city='%s') AND events.isPublished=1",
				city);
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
		event.setLectures(lectureManager.getAllLectuersByEventId(event.getEventId()));
		event.setIsPublished(rs.getInt("isPublished"));
		return event;
	}

}
