package com.RegistrationToken.Models;

public class Student {

	private String prnNumber;
	private String name;
	private String batch;
	private String course;
	private String mobileNumber;
	private String password;
	public int foodCoupon;
	public int teaCofeeCoupon;
	
	public String getBatch() {
		return batch;
	}
	public void setBatch(String batch) {
		this.batch = batch;
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
	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getFoodCoupon() {
		return foodCoupon;
	}
	public void setFoodCoupon(int foodCoupon) {
		this.foodCoupon = foodCoupon;
	}
	public int getTeaCofeeCoupon() {
		return teaCofeeCoupon;
	}
	public void setTeaCofeeCoupon(int teaCofeeCoupon) {
		this.teaCofeeCoupon = teaCofeeCoupon;
	}
	
	
	public Student(String prnNumber, String name, String batch, String course, String mobileNumber, int foodCoupon,
			int teaCofeeCoupon) {
		this.prnNumber = prnNumber;
		this.name = name;
		this.batch = batch;
		this.course = course;
		this.mobileNumber = mobileNumber;
		this.foodCoupon = foodCoupon;
		this.teaCofeeCoupon = teaCofeeCoupon;
	}
	public Student(String prnNumber, String name, String batch, String course, String mobileNumber, String password) {
		
		this.prnNumber = prnNumber;
		this.name = name;
		this.batch = batch;
		this.course = course;
		this.mobileNumber = mobileNumber;
		this.password = password;
	}
	public Student(String prnNumber, String name, String batch, String course, String mobileNumber) {
		
		this.prnNumber = prnNumber;
		this.name = name;
		this.batch = batch;
		this.course = course;
		this.mobileNumber = mobileNumber;
	}
	public Student(String prnNumber, String password) {
		this.prnNumber = prnNumber;
		this.password = password;
	}
	public Student(String prnNumber, String name, String course) {
		this.prnNumber = prnNumber;
		this.name = name;
		this.course = course;
	}
	
	public Student() {
		
	}
}
