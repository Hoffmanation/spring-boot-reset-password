package com.reset.password.demo.app;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

/**
 * A Service class that manage Access-Token registry to be used by the 'Forgot
 * my password' flow When a user clicks on the 'Forgot my password' a unique
 * token will be generated and stored inside the @param tokenRegister Map An
 * email will be send with this exact token and will be validate when the user
 * will send the request back to the server
 * 
 * @author Hoffman
 *
 */
@Component
public class AccessTokenService {
	private static final Logger log = Logger.getLogger(AccessTokenService.class.getSimpleName());

	// Access-Token registry
	Map<String, String> tokenRegister;

	public AccessTokenService() {
		tokenRegister = new ConcurrentHashMap<String, String>();
	}

	/**
	 * Method will validate if user have the same Access-Token that was send to him
	 * in the 'Reset your password' mail
	 * 
	 * @param user
	 * @param token
	 * @return {@link Boolean}
	 */
	public boolean isEligible(Person person, String token) {
		try {
			String tokenInMap = tokenRegister.get(person.getEmail());
			return token.equals(tokenInMap);
		} catch (Exception e) {
			log.error("Fiald to resolve Access-Token from reporitory, Data that was passed to Cotroller: Username: "
					+ person.getEmail() + ", Access-Token: " + token);
		}
		return false;
	}

	/**
	 * Method will add a new key pair of <Person-mail , Access-Token> to the registry
	 * 
	 * @param sendConformationMailTo
	 * @param token
	 */
	public void addAccessToken(String sendConformationMailTo, String token) {
		try {
			tokenRegister.put(sendConformationMailTo, token);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
