package conferenceOrganisation.managers;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import conferenceOrganisation.database.connection.DatabaseConnection;
import conferenceOrganisation.models.Ticket;
import conferenceOrganisation.models.User;
import conferenceOrganisation.services.CurrentUser;

@Stateless
public class TicketManager {

	@Inject
	DatabaseConnection dbConnection;

	@Inject
	UserManager userManager;

	@Inject
	EventManager eventManager;

	@Inject
	CurrentUser currentUser;

	public List<Ticket> getAllTicketsByUserId(int userId) throws SQLException, IOException {
		List<Ticket> tickets = new ArrayList<Ticket>();
		String txtQuery = String.format("select * from tickets where tickets.ownerId = %d", userId);
		Statement statement = dbConnection.createStatement();
		ResultSet rs = statement.executeQuery(txtQuery);
		while (rs.next()) {
			Ticket ticket = loadTicketProperties(rs);
			tickets.add(ticket);
		}
		statement.close();
		return tickets;
	}

	public List<Ticket> getAllTicketsByEventId(int eventId) throws SQLException, IOException {
		List<Ticket> tickets = new ArrayList<Ticket>();
		String txtQuery = String.format("select * from tickets where tickets.eventId = %d", eventId);
		Statement statement = dbConnection.createStatement();
		ResultSet rs = statement.executeQuery(txtQuery);
		while (rs.next()) {
			Ticket ticket = loadTicketProperties(rs);
			tickets.add(ticket);
		}
		statement.close();
		return tickets;
	}

	public boolean addTicketToUser(int eventId) throws SQLException, IOException {
		User user = currentUser.getCurrentUser();
		int userId = user.getUserId();
		String txtQuery = String.format("insert into tickets(ownerId, eventId) values (%d, %d)", userId, eventId);
		Statement statement = dbConnection.createStatement();
		statement.executeUpdate(txtQuery);
		user.setTickets(getAllTicketsByUserId(userId));
		eventManager.reduceEventAvailabeSeatsByOneByEventId(eventId);
		statement.close();
		return true;
	}

	public Ticket loadTicketProperties(ResultSet rs) throws SQLException {
		Ticket ticket = new Ticket();
		ticket.setTicketId(rs.getInt("ticketId"));
		ticket.setOwnerId(rs.getInt("ownerId"));
		ticket.setEventId(rs.getInt("eventId"));
		return ticket;
	}
}
