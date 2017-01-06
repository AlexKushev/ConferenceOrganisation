package conferenceOrganisation.managers;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import conferenceOrganisation.database.connection.DatabaseConnection;
import conferenceOrganisation.models.Lecture;

@Stateless
public class LectureManager {

	@Inject
	DatabaseConnection dbConnection;

	public void addLectureToEvent(Lecture lecture) throws SQLException, IOException {
		int eventId = lecture.getEventId();
		String title = lecture.getTitle();
		String description = lecture.getDescription();
		String lecturerName = lecture.getLecturerName();
		String lecturerEmail = lecture.getLecturerEmail();
		String lecturerDescription = lecture.getLecturerDescription();
		String txtQuery = String.format(
				"insert into lectures (eventId, title, description, lecturerEmail, lecturerDescription,"
						+ "lecturerName, isDeleted) values (%d, '%s', '%s', '%s', '%s', '%s', %d) ",
				eventId, title, description, lecturerEmail, lecturerDescription, lecturerName, 0);
		Statement statement = dbConnection.createStatement();
		statement.executeUpdate(txtQuery);
		statement.close();
	}

	public void editLecture(Lecture lecture) throws SQLException, IOException {
		String newTitle = lecture.getTitle();
		String newDescription = lecture.getDescription();
		String newLecturerName = lecture.getLecturerName();
		String newLecturerEmail = lecture.getLecturerEmail();
		String newLecturerDescription = lecture.getLecturerDescription();
		String txtQuery = String.format(
				"update lectures set lectures.title='%s', lectures.description='%s',"
						+ "lectures.lecturerEmail='%s', lectures.lecturerDescription='%s', lectures.lecturerName='%s' "
						+ "where lectures.lectureId=%d",
				newTitle, newDescription, newLecturerEmail, newLecturerDescription, newLecturerName,
				lecture.getLectureId());
		Statement statement = dbConnection.createStatement();
		statement.executeUpdate(txtQuery);
		statement.close();
	}

	public List<Lecture> getAllLectuersByEventId(int eventId) throws SQLException, IOException {
		List<Lecture> lectures = new ArrayList<Lecture>();
		String txtQuery = String.format("select * from lectures where lectures.eventId=%d AND lectures.isDeleted=%d",
				eventId, 0);
		Statement statement = dbConnection.createStatement();
		ResultSet rs = statement.executeQuery(txtQuery);
		while (rs.next()) {
			Lecture lecture = loadLectureProperties(rs);
			lectures.add(lecture);
		}
		statement.close();
		return lectures;
	}

	public void deleteLectureByLectureId(int lectureId) throws SQLException, IOException {
		String txtQuery = String.format("update lectures set lectures.isDeleted=%d where lectures.lectureId=%d", 1,
				lectureId);
		Statement statement = dbConnection.createStatement();
		statement.executeUpdate(txtQuery);
		statement.close();
	}
	
	public Lecture getLectureByLectureId(int lectureId) throws SQLException, IOException {
		String txtQuery = String.format("select * from lectures where lectures.lectureId=%d", lectureId);
		Statement statement = dbConnection.createStatement();
		ResultSet rs = statement.executeQuery(txtQuery);
		Lecture lecture = null;
		while (rs.next()) {
			lecture = loadLectureProperties(rs);
		}
		return lecture;
	}

	private Lecture loadLectureProperties(ResultSet rs) throws SQLException {
		Lecture lecture = new Lecture();
		lecture.setLectureId(rs.getInt("lectureId"));
		lecture.setEventId(rs.getInt("eventId"));
		lecture.setTitle(rs.getString("title"));
		lecture.setDescription(rs.getString("description"));
		lecture.setLecturerName(rs.getString("lecturerName"));
		lecture.setLecturerDescription(rs.getString("lecturerDescription"));
		lecture.setLecturerEmail(rs.getString("lecturerEmail"));
		return lecture;
	}

}
