
/**
 * Message POJO will be used as a customer (JAX-RS) response for REST API.
 */
package com.reset.password.demo.app;

public class Message {

	public String message;


	public Message() {
	}

	public Message(String message) {
		super();
		this.message = message;

	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}



}
