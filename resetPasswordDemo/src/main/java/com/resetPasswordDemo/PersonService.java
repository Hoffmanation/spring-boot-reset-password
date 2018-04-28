package com.resetPasswordDemo;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.sun.enterprise.module.bootstrap.Main;

@Service
@Component
public class PersonService implements PersonDAO {

	Map<Person, String> tokenRegister = new HashMap<>();
	
	@Autowired
	private JpaService service;

	@Override
	public boolean signup(Person person) {
		String newPassword = Util.encryptSHY2(person.getPassword());
		person.setPassword(newPassword);
		service.save(person);
		return true;
	}

	@Override
	public boolean login(Person person) {
		String newPass = Util.encryptSHY2(person.getPassword());
		Person person2 = service.login(person.getEmail(), newPass);
		if (person2 != null) {
			return true;
		}
		return false;
	}

	@Override
	public boolean updatePassword(String email, String password) {      
		Person temp = service.getPersonByEmail(email);
		temp.setPassword(Util.encryptSHY2(password));
		service.save(temp);
		return true;
	}

	@Override
	public boolean sendConformationMail(String sendConformationMailTo, String username) {
		tokenRegister.clear();
		Person tempPrson = service.getPersonByEmail(sendConformationMailTo);
		if (tempPrson != null) {
			String token = Util.getSaltString();
			MailMessageRequest message = new MailMessageRequest(sendConformationMailTo, "Change password for paeew App",
					"<h1>To reset your password please click on the button below</h1></br><a href=\"http://localhost:8090/reset.html?"
							+ token + "\">link</a>",
					"donotrelplay@paeew.com");
			Util.sendMail(message);
			tokenRegister.put(tempPrson, token);
			return true;
		}
		return false;
	}

	@Override
	public boolean isEligible(Person customer, String token) {
		String tokenInMap = tokenRegister.get(customer);
		return token.equals(tokenInMap);
	}

}
