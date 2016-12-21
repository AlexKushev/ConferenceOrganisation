package conferenceOrganisation.models;

import java.io.Serializable;

import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Table(name = "events")
@XmlRootElement
public class Event implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int eventId;
	
	private int creatorId;
	
	private int hallId;
	
	private String title;
	
	private String description;
	
	private String date;
	
	private double price;
	
	private int availableSeats;
	
	private Hall hall;

	public int getEventId() {
		return eventId;
	}

	public void setEventId(int eventId) {
		this.eventId = eventId;
	}

	public int getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(int creatorId) {
		this.creatorId = creatorId;
	}

	public int getHallId() {
		return hallId;
	}

	public void setHallId(int hallId) {
		this.hallId = hallId;
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

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getAvailableSeats() {
		return availableSeats;
	}

	public void setAvailableSeats(int availableSeats) {
		this.availableSeats = availableSeats;
	}

	public Hall getHall() {
		return hall;
	}

	public void setHall(Hall hall) {
		this.hall = hall;
	}
	
}
