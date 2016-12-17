package conferenceOrganisation.managers;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Singleton
public class UserManager {
	
	@PersistenceContext
	private EntityManager em;

}
