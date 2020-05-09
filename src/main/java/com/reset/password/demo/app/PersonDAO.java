package com.reset.password.demo.app;

/**
 * An Interface for the {@link Person} DAO-Layer service
 * 
 * @author Hoffman
 *
 */
public interface PersonDAO {

	public Person  login(Person person);

	public Person  signup(Person person);

	public boolean updatePassword(String email, String password);

	public Person findByEmail(String email);

}
