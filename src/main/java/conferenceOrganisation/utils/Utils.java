package conferenceOrganisation.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Properties;

import javax.inject.Inject;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.ws.rs.core.Response;

import conferenceOrganisation.managers.UserManager;
import conferenceOrganisation.models.User;

public class Utils {

	public static final Response RESPONSE_OK = Response.ok().build();
	public static final Response RESPONSE_ERROR = Response.status(401).build();
	private static final String USER_NAME = "conferenceorganisationhelp@gmail.com";
	private static final String PASSWORD = "conference1234";

	@Inject
	UserManager userManager;

	public static String getHashedPassword(String password) {

		String passwordToHash = password;
		String generatedPassword = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(passwordToHash.getBytes());
			byte[] bytes = md.digest();
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < bytes.length; i++) {
				sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
			}
			generatedPassword = sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return generatedPassword;

	}

	public static String generateRandomPassword() {
		final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
		SecureRandom rnd = new SecureRandom();

		StringBuilder sb = new StringBuilder(10);
		for (int i = 0; i < 10; i++) {
			sb.append(AB.charAt(rnd.nextInt(AB.length())));
		}
		return sb.toString();
	}

	public static void sendWelcomeEmail(User user) {
		String RECIPIENT = user.getEmail();

		String from = USER_NAME;
		String pass = PASSWORD;
		String[] to = { RECIPIENT };

		String subject = "Conference Organistion Registration";
		String body = String.format(
				"Hello %s, \r\n\r\nWelcome to Conference Organisation and thank you for your registration. \r\n\r\nYour userName : %s \r\nYour password : %s \r\n\r\n"
						+ "If you have any questions you can contact us : %s",
				user.getFirstName(), user.getEmail(), user.getPassword(), USER_NAME);

		sendFromGMail(from, pass, to, subject, body);
	}

	public static void sendNewPasswordEmail(String email, String newPassword) {
		String RECIPIENT = email;

		String from = USER_NAME;
		String pass = PASSWORD;
		String[] to = { RECIPIENT };

		String subject = "Conference Organisation Reset Password";
		String body = String.format("Your new password is : '%s' \r\n\r\nYou can change it from your account settings!",
				newPassword);

		sendFromGMail(from, pass, to, subject, body);
	}

	private static void sendFromGMail(String from, String pass, String[] to, String subject, String body) {
		System.out.println("Start sending email to " + to[0] + " ...");
		Properties props = System.getProperties();
		String host = "smtp.gmail.com";
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.user", from);
		props.put("mail.smtp.password", pass);
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.auth", "true");

		Session session = Session.getDefaultInstance(props);
		MimeMessage message = new MimeMessage(session);

		try {
			message.setFrom(new InternetAddress(from));
			InternetAddress[] toAddress = new InternetAddress[to.length];

			// To get the array of addresses
			for (int i = 0; i < to.length; i++) {
				toAddress[i] = new InternetAddress(to[i]);
			}

			for (int i = 0; i < toAddress.length; i++) {
				message.addRecipient(Message.RecipientType.TO, toAddress[i]);
			}

			message.setSubject(subject);
			message.setText(body);
			Transport transport = session.getTransport("smtp");
			transport.connect(host, from, pass);
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();
			System.out.println("Email to " + to[0] + " sent.");
		} catch (AddressException ae) {
			ae.printStackTrace();
		} catch (MessagingException me) {
			me.printStackTrace();
		}
	}

}
