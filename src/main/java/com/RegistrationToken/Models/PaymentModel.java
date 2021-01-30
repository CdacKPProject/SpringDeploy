package com.RegistrationToken.Models;

public class PaymentModel {
	private boolean status;
	private String orderId;
	private String paymentId;
	private String signature;
	private boolean foodOrTea;
	
	public boolean isFoodOrTea() {
		return foodOrTea;
	}
	public void setFoodOrTea(boolean foodOrTea) {
		this.foodOrTea = foodOrTea;
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
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public PaymentModel(String orderId) {
		this.orderId = orderId;
	}

	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public PaymentModel(boolean status, String orderId, boolean foodOrTea) {
		this.status = status;
		this.orderId = orderId;
		this.foodOrTea=foodOrTea;
	}
	public PaymentModel(boolean status, String orderId, String paymentId, String signature,boolean foodOrTea) {
		this.status = status;
		this.orderId = orderId;
		this.paymentId = paymentId;
		this.signature = signature;
		this.foodOrTea=foodOrTea;
	}
	public PaymentModel() {
		
	}

}
