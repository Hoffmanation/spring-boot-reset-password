
/**
 * Message POJO will be used as a customer (JAX-RS) response for REST API.
 */
package com.resetPasswordDemo;

public class Message {

	public String message;
	public Object obj;

	public Message() {
	}

	public Message(String message, Object obj) {
		super();
		this.message = message;
		this.obj = obj;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}

}
