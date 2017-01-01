package conferenceOrganisation.services;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import conferenceOrganisation.database.connection.DatabaseConnection;
import conferenceOrganisation.managers.EventManager;
import conferenceOrganisation.managers.HallManager;
import conferenceOrganisation.managers.UserManager;
import conferenceOrganisation.models.Event;
import conferenceOrganisation.models.User;
import conferenceOrganisation.utils.Utils;

@Stateless
@Path("admin")
public class AdminService {

	@Inject
	DatabaseConnection dbConnection;

	@Inject
	UserManager userManager;

	@Inject
	HallManager hallManager;

	@Inject
	EventManager eventManager;

	@Path("users")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<User> getAllUsers() throws SQLException, IOException {
		List<User> users = userManager.getAllUsers();
		return users;
	}

	@Path("events")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Event> getAllEvents() throws SQLException, IOException {
		List<Event> events = eventManager.getAllEvents();
		return events;
	}

	@Path("pendingEvents")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Event> getAllPendingEvents() throws SQLException, IOException {
		List<Event> events = eventManager.getAllPendingEvents();
		return events;
	}

	@Path("acceptEvent")
	@POST
	public Response acceptEvent(@QueryParam("eventId") int eventId) {
		try {
			eventManager.acceptEvent(eventId);
		} catch (SQLException | IOException e) {
			System.out.println("Exception while trying to accept event with id : " + eventId);
			return Utils.RESPONSE_ERROR;
		}
		return Utils.RESPONSE_OK;
	}

	@Path("declineEvent")
	@POST
	public Response declineEvent(@QueryParam("eventId") int eventId) {
		try {
			eventManager.declineEvent(eventId);
		} catch (SQLException | IOException e) {
			System.out.println("Exception while trying to decline event with id : " + eventId);
			return Utils.RESPONSE_ERROR;
		}
		return Utils.RESPONSE_OK;
	}

}
