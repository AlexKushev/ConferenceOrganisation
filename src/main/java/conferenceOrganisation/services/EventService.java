package conferenceOrganisation.services;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import conferenceOrganisation.managers.EventManager;
import conferenceOrganisation.managers.LectureManager;
import conferenceOrganisation.models.CitiesContainer;
import conferenceOrganisation.models.Event;
import conferenceOrganisation.models.Lecture;

@Stateless
@Path("events")
public class EventService {
	
	@Inject
	EventManager eventManager;
	
	@Inject 
	LectureManager lectureManager;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Event> getAllEvents() throws SQLException, IOException {
		List<Event> events = eventManager.getAllEvents();
		return events;
	}
	
	@Path("lectures")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Lecture> getAllLecturesByEventId(@QueryParam("eventId") int eventId) throws SQLException, IOException {
		List<Lecture> lectures = lectureManager.getAllLectuersByEventId(eventId);
		return lectures;
	}
	
	@Path("cities")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public CitiesContainer getAllCytiesWithEvent() throws SQLException, IOException {
		CitiesContainer cyties = eventManager.getAllCytiesWithEvent();
		return cyties;
	}
	

}
