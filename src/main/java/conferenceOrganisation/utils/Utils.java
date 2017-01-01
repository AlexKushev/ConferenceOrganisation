package conferenceOrganisation.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.ws.rs.core.Response;

public class Utils {
	
	public static final Response RESPONSE_OK = Response.ok().build();
	public static final Response RESPONSE_ERROR = Response.status(401).build();

	public static String getHashedPassword(String password) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA-512");
		} catch (NoSuchAlgorithmException e) {
			// should not happen
		}
		password = new String(md.digest(password.getBytes()));
		return password;
	}

}
