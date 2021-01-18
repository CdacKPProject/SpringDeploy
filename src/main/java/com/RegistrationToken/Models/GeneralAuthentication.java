package com.RegistrationToken.Models;

public class GeneralAuthentication {

	private boolean tokenStatus;
	private String fToken;
	private String message;
	private Student student;
	
	public Student getStudent() {
		return student;
	}
	public void setStudent(Student student) {
		this.student = student;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public boolean isTokenStatus() {
		return tokenStatus;
	}
	public void setTokenStatus(boolean tokenStatus) {
		this.tokenStatus = tokenStatus;
	}
	public String getfToken() {
		return fToken;
	}
	public void setfToken(String fToken) {
		this.fToken = fToken;
	}

	public GeneralAuthentication(boolean tokenStatus, String fToken, String message) {
		this.tokenStatus = tokenStatus;
		this.fToken = fToken;
		this.message = message;
	}
	public GeneralAuthentication(boolean tokenStatus, String message) {
		this.tokenStatus = tokenStatus;
		this.message = message;
	}
	
	public GeneralAuthentication(Student student,boolean tokenStatus, String message) {
		this.tokenStatus = tokenStatus;
		this.message = message;
		this.student = student;
	}
	public GeneralAuthentication() {
		
	}
	
}
