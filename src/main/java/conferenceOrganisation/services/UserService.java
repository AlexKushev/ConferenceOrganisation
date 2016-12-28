package conferenceOrganisation.services;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.sql.SQLException;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import conferenceOrganisation.managers.EventManager;
import conferenceOrganisation.managers.TicketManager;
import conferenceOrganisation.managers.UserManager;
import conferenceOrganisation.models.Event;
import conferenceOrganisation.models.User;

@Stateless
@Path("user")
public class UserService {

	private static final Response RESPONSE_OK = Response.ok().build();

	@Inject
	private UserManager userManager;

	@Inject
	private CurrentUser currentUser;

	@Inject
	private TicketManager ticketManager;

	@Inject
	private EventManager eventManager;

	@Path("login")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response loginUser(User user) throws SQLException, IOException {
		User foundUser = userManager.getUserByEmailAndPassword(user.getEmail(), user.getPassword());
		if (foundUser == null) {
			return Response.status(HttpURLConnection.HTTP_UNAUTHORIZED).build();
		}
		currentUser.setCurrentUser(user);
		return RESPONSE_OK;
	}

	@Path("register")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response registerUser(User user) throws IOException {
		try {
			userManager.addUser(user);
			return RESPONSE_OK;
		} catch (SQLException e) {
			System.out.println("Problem occurs while trying to add new user.");
		}
		return Response.status(401).build();
	}

	@Path("current")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public User getUser() {
		if (currentUser.getCurrentUser() == null) {
			return null;
		}
		return currentUser.getCurrentUser();
	}

	@Path("logout")
	@GET
	public void logoutUser() {
		currentUser.setCurrentUser(null);
	}

	@Path("bookTicket")
	@POST
	public Response bookTicket(@QueryParam("eventId") int eventId) throws SQLException, IOException {
		if (!ticketManager.addTicketToUser(eventId)) {
			return Response.status(401).build();
		}
		return RESPONSE_OK;
	}

	@Path("createEvent")
	@POST
	public Response createEvent(Event event) throws SQLException, IOException {
		if (!eventManager.addEvent(event)) {
			return Response.status(401).build();
		}
		return RESPONSE_OK;
	}

}
