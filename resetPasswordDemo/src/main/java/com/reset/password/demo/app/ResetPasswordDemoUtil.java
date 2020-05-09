package com.reset.password.demo.app;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

/**
 * A Helper-Service class with global utility methods for the Reset-Password-Demo  Application
 * @author Hoffman
 *
 */
public abstract class ResetPasswordDemoUtil {
	private static final Logger logger = Logger.getLogger(ResetPasswordDemoUtil.class);

	// ===============SHY2 method ====================
	/**
	 * Method will encrypt a given string with SHY2 algorithm
	 * @param password
	 * @return {@link String}
	 */
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
	/**
	 * Method will create a salt String 
	 * @return {@link String}
	 */
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
	
	
	/**
	 * Method Will validate if a string is an email
	 * @param email
	 * @return {@link Boolean}
	 */
	public static boolean emailValidator(String email) {
		if (email != "" && email != null) {
			final Pattern EMAIL_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
					Pattern.CASE_INSENSITIVE);
			Matcher matcher = EMAIL_REGEX.matcher(email);
			return matcher.find();
		} else {
			logger.error("Field email have a null value in it.", new RuntimeException("At least one attributes returned a null value."));
			return false;
		}

	}
	
	
	/**
	 * Method will retrieve a string content from a given {@link InputStream}
	 * @param is
	 * @return {@link String}
	 */
	public static String getEmailTemplateFromClasspath(InputStream is) {
		String emailTemplate = null;
		try {
			BufferedReader buf = new BufferedReader(new InputStreamReader(is));
			String line = buf.readLine();
			StringBuilder sb = new StringBuilder();
			while (line != null) {
				sb.append(line).append("\n");
				line = buf.readLine();
			}
			emailTemplate = sb.toString();
		} catch (Exception e) {
			logger.error("Failed to get Email-Template resource from classpath." ,e);
		}
		return emailTemplate ;
	}
	
	
	/**
	 * Method will resolve the filestone application Machine Host Name
	 * @param request
	 * @return {@link String}
	 */
	public static String getMachineHostName(HttpServletRequest request) {
		StringBuffer url = request.getRequestURL();
		String uri = request.getRequestURI();
		int idx = (((uri != null) && (uri.length() > 0)) ? url.indexOf(uri) : url.length());
		String host = url.substring(0, idx); //base url
		idx = host.indexOf("://");
		if(idx > 0) {
		  host = host.substring(idx); //remove scheme if present
		}
		
		return host ;
	}

	


}
