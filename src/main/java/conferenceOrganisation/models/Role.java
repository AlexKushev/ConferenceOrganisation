package conferenceOrganisation.models;

import javax.persistence.Id;
import javax.persistence.Table;

@Table (name = "roles")
public class Role {
	
	private static final String ROLE_ADMIN = "role_admin";
	private static final String ROLE_HOST = "role_host";
	private static final String ROLE_LECTURER = "role_lecturer";
	
	@Id
	private int roleId;

}
