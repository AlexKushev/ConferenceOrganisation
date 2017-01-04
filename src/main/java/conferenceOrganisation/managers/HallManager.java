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
		statement.close();
		return newHall.getHallId();
	}

	public void editHall(Hall hall) throws SQLException, IOException {
		String newHallName = hall.getName();
		int newHallCapacity = hall.getCapacity();
		String newHallLocation = hall.getLocation();
		String newHallCity = hall.getCity();
		String txtQuery = String.format(
				"update halls set halls.name='%s', halls.capacity=%d, halls.location='%s',"
						+ "halls.city='%s' where halls.hallId=%d",
				newHallName, newHallCapacity, newHallLocation, newHallCity, hall.getHallId());
		Statement statement = dbConnection.createStatement();
		statement.executeUpdate(txtQuery);
		statement.close();
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
