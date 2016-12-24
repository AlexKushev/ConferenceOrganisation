package conferenceOrganisation.models;

import java.io.Serializable;

import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Table(name = "tickets")
@XmlRootElement
public class Ticket implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2700102265595013895L;

	private int ticketId;
	
	private int ownerId;
	
	private int eventId;
	
	public Ticket() {
		super();
	}

	public Ticket(int ticketId, int ownerId, int eventId) {
		super();
		this.ticketId = ticketId;
		this.ownerId = ownerId;
		this.eventId = eventId;
	}

	public int getTicketId() {
		return ticketId;
	}

	public void setTicketId(int ticketId) {
		this.ticketId = ticketId;
	}

	public int getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(int ownerId) {
		this.ownerId = ownerId;
	}

	public int getEventId() {
		return eventId;
	}

	public void setEventId(int eventId) {
		this.eventId = eventId;
	}
	
}
