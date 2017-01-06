package conferenceOrganisation.services;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import conferenceOrganisation.managers.LectureManager;
import conferenceOrganisation.models.Lecture;
import conferenceOrganisation.utils.Utils;

@Stateless
@Path("lectures")
public class LectureService {

	@Inject
	LectureManager lectureManager;

	@Path("add")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addNewLecture(Lecture lecture) {
		try {
			lectureManager.addLectureToEvent(lecture);
		} catch (SQLException | IOException e) {
			System.out.println("Exception while trying to add new lecture to event : " + lecture.getEventId());
			return Utils.RESPONSE_ERROR;
		}
		return Utils.RESPONSE_OK;
	}

	@Path("edit")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response editLecture(Lecture lecture) {
		try {
			lectureManager.editLecture(lecture);
			return Utils.RESPONSE_OK;
		} catch (SQLException | IOException e) {
			System.out.println("Exception while trying to edit lecture with id : " + lecture.getLectureId());
			return Utils.RESPONSE_ERROR;
		}
	}

	@Path("delete")
	@POST
	public Response deleteLectureByLectureId(@QueryParam("lectureId") int lectureId) {
		try {
			lectureManager.deleteLectureByLectureId(lectureId);
			return Utils.RESPONSE_OK;
		} catch (SQLException | IOException e) {
			System.out.println("Exception while trying to delete lecture with lectireId : " + lectureId);
			return Utils.RESPONSE_ERROR;
		}
	}

	@Path("getByEventId")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Lecture> getAllLecturesByEventId(@QueryParam("eventId") int eventId) {
		List<Lecture> lectures = new ArrayList<>();
		try {
			lectures = lectureManager.getAllLectuersByEventId(eventId);
		} catch (SQLException | IOException e) {
			System.out.println("Exception while trying to get all lectures by eventId : " + eventId);
		}
		return lectures;
	}

	@Path("getByLectureId")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Lecture getLectureByLectureId(@QueryParam("lectureId") int lectureId) {
		Lecture lecture = new Lecture();
		try {
			lecture = lectureManager.getLectureByLectureId(lectureId);
		} catch (SQLException | IOException e) {
			System.out.println("Exception while trying to get lecture with id : " + lectureId);
		}
		return lecture;
	}
}
