package com.RegistrationToken.Models;

public class Admin {

	private String adminId;
	private String adminName;
	private String password;
	private String mobileNumber;
	
	public String getAdminId() {
		return adminId;
	}

	public void setAdminId(String adminId) {
		this.adminId = adminId;
	}

	public String getAdminName() {
		return adminName;
	}

	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public Admin(String adminId, String adminName, String password, String mobileNumber) {
		this.adminId = adminId;
		this.adminName = adminName;
		this.password = password;
		this.mobileNumber = mobileNumber;
	}

	public Admin() {
		
	}
}
