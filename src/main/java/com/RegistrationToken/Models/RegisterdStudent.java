package com.RegistrationToken.Models;

public class RegisterdStudent {

	private String prnNumber;
	private String name;
	private String course;
	private boolean eatUpStatus;
	
	public boolean isEatUpStatus() {
		return eatUpStatus;
	}
	public void setEatUpStatus(boolean eatUpStatus) {
		this.eatUpStatus = eatUpStatus;
	}
	public String getPrnNumber() {
		return prnNumber;
	}
	public void setPrnNumber(String prnNumber) {
		this.prnNumber = prnNumber;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCourse() {
		return course;
	}
	public void setCourse(String course) {
		this.course = course;
	}
	
	public RegisterdStudent(String prnNumber, String name, String course, boolean eatUpStatus) {
		this.prnNumber = prnNumber;
		this.name = name;
		this.course = course;
		this.eatUpStatus = eatUpStatus;
	}
	public RegisterdStudent() {
		
	}
}
