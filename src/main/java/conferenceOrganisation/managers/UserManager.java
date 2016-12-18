package conferenceOrganisation.managers;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import conferenceOrganisation.database.connection.DatabaseConnection;
import conferenceOrganisation.models.User;

@Singleton
public class UserManager {

	@PersistenceContext
	private EntityManager em;
	
	@Inject
	DatabaseConnection db;


	public void addUser(User user) throws SQLException {
		String firstName = user.getFirstName();
		String lastName = user.getLastName();
		String email = user.getEmail();
		String password = user.getPassword();
		String txtQuery = String.format(
				"insert into user(firstName, lastName, email, password) values ('%s', '%s', '%s', '%s')", firstName,
				lastName, email, password);
		//Statement statement.executeUpdate(txtQuery);
	}
	
	public List<User> getAllUsers() throws SQLException, IOException {
		String txtQuery = "select * from users u";
		Statement statement = db.getStatement();
		ResultSet rs = statement.executeQuery(txtQuery);
		List<User> users = new ArrayList<User>();
		while (rs.next()) {
			User user = new User();
			user.setFirstName(rs.getString("firstName"));
			user.setLastName(rs.getString("lastName"));
			user.setEmail(rs.getString("email"));
			users.add(user);
		}
		return users;
	}

}
