package conferenceOrganisation.managers;

import java.sql.SQLException;
import java.sql.Statement;

import javax.ejb.Singleton;

import conferenceOrganisation.database.connection.DatabaseConnection;
import conferenceOrganisation.models.Event;

@Singleton
public class EventManager {

	private Statement statement = DatabaseConnection.statement;

	public void addEvent(Event event) throws SQLException {
		int creatorId = event.getCreatorId();
		int hallId = event.getHallId();
		String title = event.getTitle();
		String description = event.getDescription();
		String date = event.getDate();
		double price = event.getPrice();
		int availableSeats = 100;
		// TODO get available seats from hall object
		String txtQuery = String.format(
				"insert into events(creatorId, hallId, title, description, date, price, availableSeats) values (%d, %d, '%s', '%s', '%s', %s, %d)",
				creatorId, hallId, title, description, date, price, availableSeats);
		statement.executeQuery(txtQuery);
	}

}
