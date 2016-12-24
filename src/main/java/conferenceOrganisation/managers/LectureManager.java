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

	public List<Lecture> getAllLecturesByUserId(int userId) throws SQLException, IOException {
		List<Lecture> lectures = new ArrayList<Lecture>();
		String txtQuery = String.format("select * from lectures where lectures.lecturerId=%d", userId);
		Statement statement = dbConnection.createStatement();
		ResultSet rs = statement.executeQuery(txtQuery);
		while (rs.next()) {
			Lecture lecture = new Lecture();
			lecture.setLectureId(rs.getInt("lectureId"));
			lecture.setEventId(rs.getInt("eventId"));
			lecture.setLecturerId(rs.getInt("lecturerId"));
			lecture.setTitle(rs.getString("title"));
			lecture.setDescription(rs.getString("description"));
			lectures.add(lecture);
		}
		statement.close();
		return lectures;
	}

}
