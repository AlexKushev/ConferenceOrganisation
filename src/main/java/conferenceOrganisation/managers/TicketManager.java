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

@Stateless
public class TicketManager {

	@Inject
	DatabaseConnection dbConnection;

	@Inject
	UserManager userManager;

	public List<Ticket> getAllTicketsByUserId(int userId) throws SQLException, IOException {
		List<Ticket> tickets = new ArrayList<Ticket>();
		String txtQuery = String.format("select * from tickets where tickets.ownerId = %d", userId);
		Statement statement = dbConnection.createStatement();
		ResultSet rs = statement.executeQuery(txtQuery);
		while (rs.next()) {
			Ticket ticket = new Ticket();
			ticket.setTicketId(rs.getInt("ticketId"));
			ticket.setOwnerId(rs.getInt("ownerId"));
			ticket.setEventId(rs.getInt("eventId"));
			tickets.add(ticket);
		}
		statement.close();
		return tickets;
	}

	public boolean addTicketToUser(int userId, int eventId) throws SQLException {
		String txtQuery = String.format("insert into tickets(ownerId, eventId) values (%d, %d)", userId, eventId);
		User user = null;
		Statement statement = null;
		try {
			statement = dbConnection.createStatement();
			statement.executeUpdate(txtQuery);
			user = userManager.getUserById(userId);
			user.setTickets(getAllTicketsByUserId(userId));
		} catch (SQLException | IOException e) {
			statement.close();
			return false;
		}
		statement.close();
		return true;
	}
}
