package conferenceOrganisation.models;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table (name = "roles")
public class Role {
	
	private static final String ROLE_ADMIN = "role_admin";
	private static final String ROLE_HOST = "role_host";
	private static final String ROLE_LECTURER = "role_lecturer";
	
	@Id
	private int roleId;
	
	@Column
	private String roleType;

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public String getRoleType() {
		return roleType;
	}

	public void setRoleType(String roleType) {
		this.roleType = roleType;
	}

	public static String getRoleAdmin() {
		return ROLE_ADMIN;
	}

	public static String getRoleHost() {
		return ROLE_HOST;
	}

	public static String getRoleLecturer() {
		return ROLE_LECTURER;
	}
	
}
