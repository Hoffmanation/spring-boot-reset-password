package com.resetPasswordDemo;

public interface PersonDAO {
	
	public boolean login(Person person);
	public boolean signup(Person person);
	boolean updatePassword(String email, String password);
	 boolean sendConformationMail(String sendConformationMailTo , String username) ;
	public boolean isEligible(Person customer, String token);

}
