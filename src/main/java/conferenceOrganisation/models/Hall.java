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
	
	private String name;

	private int capacity;

	private String location;

	public Hall() {

	}

	public Hall(int hallId, String name, int capacity, String location) {
		super();
		this.hallId = hallId;
		this.name = name;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
