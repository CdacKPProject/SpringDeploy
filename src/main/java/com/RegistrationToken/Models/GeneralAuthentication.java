package com.RegistrationToken.Models;

public class GeneralAuthentication {

	private boolean tokenStatus;
	private String fToken;
	private String message;
	private Student student;
	private PaymentModel payment;
	private Staff staff;
	
	public Staff getStaff() {
		return staff;
	}
	public void setStaff(Staff staff) {
		this.staff = staff;
	}
	public PaymentModel getPayment() {
		return payment;
	}
	public void setPayment(PaymentModel payment) {
		this.payment = payment;
	}
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
	
	public GeneralAuthentication(PaymentModel payment,boolean tokenStatus, String message) {
		this.tokenStatus = tokenStatus;
		this.message = message;
		this.payment = payment;
	}
	
	
	public GeneralAuthentication(String message , boolean tokenStatus, Staff staff) {
		this.tokenStatus = tokenStatus;
		this.message = message;
		this.staff = staff;
	}
	public GeneralAuthentication() {
		
	}
	
}
