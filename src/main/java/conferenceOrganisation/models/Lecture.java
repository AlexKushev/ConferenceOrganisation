package conferenceOrganisation.models;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Lecture implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8509601547542557291L;

	private int lectureId;

	private int lecturerId;

	private int eventId;

	private String title;

	private String description;

	public Lecture(int lectureId, int lecturerId, int eventId, String title, String description) {
		this.lectureId = lectureId;
		this.lecturerId = lecturerId;
		this.eventId = eventId;
		this.title = title;
		this.description = description;
	}

	public Lecture() {

	}

	public int getLectureId() {
		return lectureId;
	}

	public void setLectureId(int lectureId) {
		this.lectureId = lectureId;
	}

	public int getLecturerId() {
		return lecturerId;
	}

	public void setLecturerId(int lecturerId) {
		this.lecturerId = lecturerId;
	}

	public int getEventId() {
		return eventId;
	}

	public void setEventId(int eventId) {
		this.eventId = eventId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
