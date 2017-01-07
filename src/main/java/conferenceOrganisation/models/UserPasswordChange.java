package conferenceOrganisation.models;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class UserPasswordChange {

	private int userId;

	private String oldPassword;

	private String newPassword;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

}
