package com.reset.password.demo.app;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * A Service-Implementation class  for the {@link Person} DAO-Layer service
 * @author Hoffman
 *
 */

@Service
public class PersonService implements PersonDAO {

	Map<Person, String> tokenRegister = new HashMap<>();

	@Autowired
	private PersonRepository service;

	@Override
	public Person  signup(Person person) {
		String newPassword = ResetPasswordDemoUtil.encryptSHY2(person.getPassword());
		person.setPassword(newPassword);
		return service.save(person);
	}

	@Override
	public Person  login(Person person) {
		String newPass = ResetPasswordDemoUtil.encryptSHY2(person.getPassword());
		return service.login(person.getEmail(), newPass);
	}

	@Override
	public boolean updatePassword(String email, String password) {
		Person temp = service.getPersonByEmail(email);
		temp.setPassword(ResetPasswordDemoUtil.encryptSHY2(password));
		service.save(temp);
		return true;
	}

	public Person findByEmail(String email) {
		return service.getPersonByEmail(email);
	}

}
