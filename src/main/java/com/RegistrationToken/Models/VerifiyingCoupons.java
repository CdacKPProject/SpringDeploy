package com.RegistrationToken.Models;

public class VerifiyingCoupons {

	private String prnNumber;
	private boolean couponStatus;
	private int noOfCups;
	public String getPrnNumber() {
		return prnNumber;
	}
	public void setPrnNumber(String prnNumber) {
		this.prnNumber = prnNumber;
	}
	public boolean isCouponStatus() {
		return couponStatus;
	}
	public void setCouponStatus(boolean couponStatus) {
		this.couponStatus = couponStatus;
	}
	public int getNoOfCups() {
		return noOfCups;
	}
	public void setNoOfCups(int noOfCups) {
		this.noOfCups = noOfCups;
	}
	public VerifiyingCoupons(String prnNumber, boolean couponStatus, int noOfCups) {
		this.prnNumber = prnNumber;
		this.couponStatus = couponStatus;
		this.noOfCups = noOfCups;
	}
	public VerifiyingCoupons(String prnNumber, boolean couponStatus) {
		this.prnNumber = prnNumber;
		this.couponStatus = couponStatus;
	}
	public VerifiyingCoupons() {
	}
	
}
