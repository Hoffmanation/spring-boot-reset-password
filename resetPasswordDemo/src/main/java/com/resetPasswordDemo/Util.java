package com.resetPasswordDemo;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Formatter;
import java.util.Properties;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public abstract class Util {
	private static final Logger log = Logger.getLogger(Util.class.getSimpleName());

	// ===============SHY2 method ====================
	public static String encryptSHY2(String password) {
		MessageDigest md;
		StringBuffer hexString = null;
		try {
			md = MessageDigest.getInstance("SHA-256");
			md.update(password.getBytes());
			byte byteData[] = md.digest();

			hexString = new StringBuffer();
			for (int i = 0; i < byteData.length; i++) {
				String hex = Integer.toHexString(0xff & byteData[i]);
				if (hex.length() == 1)
					hexString.append('0');
				hexString.append(hex);
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return hexString.toString();
	}

	// ===============String salt method====================

	public static String getSaltString() {
		String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
		StringBuilder salt = new StringBuilder();
		Random rnd = new Random();
		while (salt.length() < 18) {
			int index = (int) (rnd.nextFloat() * SALTCHARS.length());
			salt.append(SALTCHARS.charAt(index));
		}

		return salt.toString();

	}

	// ===============mail method ====================
	public static boolean sendMail(MailMessageRequest messageRequest) {

		final String username = "----";
		final String password = "----";

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(username, "TestingApp"));
			message.setSender(new InternetAddress(username, "TestingApp"));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(messageRequest.getSentTo()));
			message.setSubject(messageRequest.getSubject());
			message.setContent(messageRequest.getBody(), "text/html");
			message.setReplyTo(messageRequest.getReplayTo());
			Transport.send(message);
			log.info("A mail has been sent to " + messageRequest.getSentTo() + " at --> " + new Date());
		} catch (MessagingException | UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
		return true;
	}

	public static boolean emailValidator(String email) {
		if (email != "" && email != null) {
			final Pattern EMAIL_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
					Pattern.CASE_INSENSITIVE);
			Matcher matcher = EMAIL_REGEX.matcher(email);
			return matcher.find();
		} else {
			log.log(Level.WARNING, "Field email have a null value in it.",
					new RuntimeException("At least one attributes returned a null value."));
			return false;
		}

	}
}
