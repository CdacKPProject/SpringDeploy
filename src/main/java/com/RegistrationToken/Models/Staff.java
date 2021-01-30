package com.RegistrationToken.Models;

public class Staff {

	private String staffId;
	private String staffName;
	private String password;
	private String mobileNumber;
	public String getStaffId() {
		return staffId;
	}
	public void setStaffId(String staffId) {
		this.staffId = staffId;
	}
	public String getStaffName() {
		return staffName;
	}
	public void setStaffName(String staffName) {
		this.staffName = staffName;
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
	public Staff(String staffId, String staffName, String password, String mobileNumber) {
		this.staffId = staffId;
		this.staffName = staffName;
		this.password = password;
		this.mobileNumber = mobileNumber;
	}
	
	public Staff(String staffId, String staffName, String mobileNumber) {
		this.staffId = staffId;
		this.staffName = staffName;
		this.mobileNumber = mobileNumber;
	}
	public Staff() {
		
	}
	
	
}
