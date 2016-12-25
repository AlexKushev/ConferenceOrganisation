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

import conferenceOrganisation.managers.EventManager;
import conferenceOrganisation.models.Event;

@Stateless
@Path("events")
public class EventService {
	
	@Inject
	EventManager eventManager;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Event> getAllEvents() throws SQLException, IOException {
		List<Event> events = eventManager.getAllEvents();
		return events;
	}

}
