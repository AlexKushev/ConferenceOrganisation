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

	public int createHall(Hall newHall) throws SQLException, IOException {
		String hallName = newHall.getName();
		int hallCapacity = newHall.getCapacity();
		String hallLocation = newHall.getLocation();
		String hallCity = newHall.getCity();
		String txtQuery = String.format(
				"insert into halls(name, capacity, location, city) values('%s', %d, '%s', '%s')", hallName,
				hallCapacity, hallLocation, hallCity);
		Statement statement = dbConnection.createStatement();
		statement.executeUpdate(txtQuery);
		ResultSet generatedKeys = statement.executeQuery("SELECT LAST_INSERT_ID()");
		if (generatedKeys.next()) {
			newHall.setHallId(generatedKeys.getInt(1));
		}
		return newHall.getHallId();
	}

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
			hall.setCity(rs.getString("city"));
		}
		statement.close();
		return hall;
	}

}
