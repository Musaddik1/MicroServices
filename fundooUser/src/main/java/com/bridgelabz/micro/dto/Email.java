package com.bridgelabz.micro.dto;

import java.io.Serializable;

public class Email implements Serializable {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String emailId;
	private String to;
	private String subject;
	private String body;
	
	public Email() {
		
	}

	public Email(String emailId, String to, String subject, String body) {
	
		this.emailId = emailId;
		this.to = to;
		this.subject = subject;
		this.body = body;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
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


}
