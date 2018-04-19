package com.resetPasswordDemo;

import javax.mail.Address;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;


/**
 * MailMessageRequest POJO will be used as a custom response for the Mail REST API.
 */
public class MailMessageRequest {

	private String subject;
	private String body;
	private String replayTo;
	private String sentTo;

	public MailMessageRequest(String sentTo, String subject, String body, String replayTo) {
		super();
		this.subject = subject;
		this.body = body;
		this.replayTo = replayTo;
		this.sentTo = sentTo;
	}

	public void setSentTo(String sentTo) {
		this.sentTo = sentTo;
	}

	public String getSentTo() {
		return sentTo;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Address[] getReplayTo() throws AddressException {
		Address[] a = { new InternetAddress(replayTo) };
		return a;
	}

	public void setReplayTo(String replayTo) {
		this.replayTo = replayTo;
	}

}