package conferenceOrganisation.services;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import conferenceOrganisation.managers.UserManager;
import conferenceOrganisation.models.User;

@Stateless
@Path("users")
public class UserService {
	
	@Inject
	private UserManager userManager;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<User> getAllUsers() throws SQLException, IOException {
		List<User> users = userManager.getAllUsers();
		if (users == null) {
			return new ArrayList<User>();
		}
		return users;
		
	}

}
