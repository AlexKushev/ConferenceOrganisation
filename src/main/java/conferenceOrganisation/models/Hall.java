package conferenceOrganisation.models;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Hall implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3137686270741827052L;

	private int hallId;

	private int capacity;

	private String location;

	public Hall() {

	}

	public Hall(int hallId, int capacity, String location) {
		super();
		this.hallId = hallId;
		this.capacity = capacity;
		this.location = location;
	}

	public int getHallId() {
		return hallId;
	}

	public void setHallId(int hallId) {
		this.hallId = hallId;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

}
