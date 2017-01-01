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

	private int eventId;

	private String title;

	private String description;

	private String lecturerName;
	
	private String lecturerEmail;
	
	private String lecturerDescription;

	public Lecture(int lectureId, int eventId, String title, String description, String lecturerName,
			String lecturerEmail, String lecturerDescription) {
		super();
		this.lectureId = lectureId;
		this.eventId = eventId;
		this.title = title;
		this.description = description;
		this.lecturerName = lecturerName;
		this.lecturerEmail = lecturerEmail;
		this.lecturerDescription = lecturerDescription;
	}
	
	public Lecture() {
		
	}

	public int getLectureId() {
		return lectureId;
	}

	public void setLectureId(int lectureId) {
		this.lectureId = lectureId;
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

	public String getLecturerName() {
		return lecturerName;
	}

	public void setLecturerName(String lecturerName) {
		this.lecturerName = lecturerName;
	}

	public String getLecturerEmail() {
		return lecturerEmail;
	}

	public void setLecturerEmail(String lecturerEmail) {
		this.lecturerEmail = lecturerEmail;
	}

	public String getLecturerDescription() {
		return lecturerDescription;
	}

	public void setLecturerDescription(String lecturerDescription) {
		this.lecturerDescription = lecturerDescription;
	}
}
