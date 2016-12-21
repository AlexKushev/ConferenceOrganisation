package conferenceOrganisation.managers;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;

import javax.ejb.Singleton;
import javax.inject.Inject;

import conferenceOrganisation.database.connection.DatabaseConnection;
import conferenceOrganisation.models.Event;

@Singleton
public class EventManager {
	
	@Inject
	private HallManager hallManager;
	
	@Inject 
	DatabaseConnection dbConnection;

	public void addEvent(Event event) throws SQLException, IOException {
		int creatorId = event.getCreatorId();
		int hallId = event.getHallId();
		String title = event.getTitle();
		String description = event.getDescription();
		String date = event.getDate();
		double price = event.getPrice();
		int availableSeats = hallManager.getHallById(hallId).getCapacity();
		String txtQuery = String.format(
				"insert into events(creatorId, hallId, title, description, date, price, availableSeats) values (%d, %d, '%s', '%s', '%s', %s, %d)",
				creatorId, hallId, title, description, date, price, availableSeats);
		Statement statement = dbConnection.createStatement();
		statement.executeQuery(txtQuery);
		statement.close();
	}

}
