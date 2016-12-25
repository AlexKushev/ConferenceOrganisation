package conferenceOrganisation.services;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import conferenceOrganisation.database.connection.DatabaseConnection;
import conferenceOrganisation.managers.UserManager;
import conferenceOrganisation.models.User;

@Stateless
@Path("admin")
public class AdminService {
	
	@Inject
	DatabaseConnection dbConnection;
	
	@Inject
	UserManager userManager;
	
	@Path("users")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<User> getAllUsers() throws SQLException, IOException {
		List<User> users = userManager.getAllUsers();
		return users;
	}

}
