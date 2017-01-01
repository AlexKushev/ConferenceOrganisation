package conferenceOrganisation.services;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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

	@Path("published")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Event> getAllPublishedEvents() throws SQLException, IOException {
		List<Event> events = eventManager.getAllPublishedEvents();
		return events;
	}

	@Path("unpublished")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Event> getAllUnpublishedEvents() throws SQLException, IOException {
		List<Event> events = eventManager.getAllUnpublishedEvents();
		return events;
	}

	@Path("publish")
	@POST
	public void publishEvent(@QueryParam("eventId") int eventId) throws SQLException, IOException {
		eventManager.makeEventPublish(eventId);
		// TODO detect exceptions
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

	@Path("eventsByCity")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Event> getAllEventsByCity(@QueryParam("city") String city) throws SQLException, IOException {
		List<Event> events = eventManager.getAllEventsByCity(city);
		return events;
	}
	
	@Path("eventByEventId")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Event getEventByEventId(@QueryParam("eventId") int eventId) throws SQLException, IOException {
		Event event = eventManager.getEventByEventId(eventId);
		return event;
	}

}
