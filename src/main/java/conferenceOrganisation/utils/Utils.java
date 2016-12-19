package conferenceOrganisation.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Utils {

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
