package conferenceOrganisation.managers;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.ejb.Singleton;
import javax.inject.Inject;

import conferenceOrganisation.database.connection.DatabaseConnection;
import conferenceOrganisation.models.Hall;

@Singleton
public class HallManager {

	@Inject
	DatabaseConnection dbConnection;
	
	public Hall getHallById(int hallId) throws SQLException, IOException {
		String txtQuery = String.format("select * from halls where hallId = %d", hallId);
		Statement statement = dbConnection.createStatement();
		ResultSet rs = statement.executeQuery(txtQuery);
		Hall hall = new Hall();
		while (rs.next()) {
			hall.setHallId(rs.getInt("hallId"));
			hall.setName(rs.getString("name"));
			hall.setCapacity(rs.getInt("capacity"));
			hall.setLocation(rs.getString("location"));
		}
		statement.close();
		return hall;
	}
	
}
