package conferenceOrganisation.services;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import conferenceOrganisation.managers.TicketManager;
import conferenceOrganisation.models.Ticket;

@Stateless
@Path("tickets")
public class TicketService {
	
	@Inject
	TicketManager ticketManager;

	@Path("ticketsByUserId")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Ticket> getAllTicketsByUserId(@QueryParam("ownerId") int ownerId) throws SQLException, IOException {
		List<Ticket> tickets = ticketManager.getAllTicketsByUserId(ownerId);	
		return tickets;
	}
	
	@Path("ticketsByEventId")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Ticket> getAllTicketsByEventId(@QueryParam("eventId") int eventId) throws SQLException, IOException {
		List<Ticket> tickets = ticketManager.getAllTicketsByEventId(eventId);	
		return tickets;
	}
}
