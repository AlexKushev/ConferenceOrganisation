package conferenceOrganisation.services;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import conferenceOrganisation.managers.UserManager;
import conferenceOrganisation.models.User;

@Stateless
@Path("users")
public class UserService {

	private static final Response RESPONSE_OK = Response.ok().build();

	@Inject
	private UserManager userManager;

	@Path("register")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response registerUser(User user) {
		try {
			userManager.addUser(user);
			return RESPONSE_OK;
		} catch (SQLException e) {
			System.out.println("Problem occurs while trying to add new user.");
		}
		return Response.status(401).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<User> getAllUsers() throws SQLException, IOException {
		List<User> users = userManager.getAllUsers();
		return users;
	}

}
