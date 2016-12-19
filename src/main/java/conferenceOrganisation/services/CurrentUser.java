package conferenceOrganisation.services;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;

import conferenceOrganisation.models.User;

@SessionScoped
public class CurrentUser implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2715975266874972110L;

	private User currentUser;

	public User getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}

}
