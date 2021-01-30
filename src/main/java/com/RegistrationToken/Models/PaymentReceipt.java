package com.RegistrationToken.Models;

import java.util.Date;

public class PaymentReceipt {

	private String prnNumber;
	private String orderId;
	private String paymentId;
	private String name;
	private String course;
	private String amount; 
	private String date;
	
	
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getPrnNumber() {
		return prnNumber;
	}

	public void setPrnNumber(String prnNumber) {
		this.prnNumber = prnNumber;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
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

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}


	public PaymentReceipt(String prnNumber, String orderId, String paymentId, String name, String course, String amount,
			String date) {
	
		this.prnNumber = prnNumber;
		this.orderId = orderId;
		this.paymentId = paymentId;
		this.name = name;
		this.course = course;
		this.amount = amount;
		this.date = date;
	}

	public PaymentReceipt() {
	}
	
}
