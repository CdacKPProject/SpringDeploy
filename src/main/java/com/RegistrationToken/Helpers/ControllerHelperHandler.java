package com.RegistrationToken.Helpers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.RegistrationToken.Models.GeneralAuthentication;
import com.RegistrationToken.Models.PaymentModel;
import com.RegistrationToken.Models.PaymentReceipt;
import com.RegistrationToken.Models.RegisterdStudent;
import com.RegistrationToken.Models.Staff;
import com.RegistrationToken.Models.Student;

public class ControllerHelperHandler {

	public static ResponseEntity<Object> generalHelperHandler(HttpStatus statusCode,String message,boolean bool,Map<String, Object> body,ResponseEntity<Object> response){
		body.put("status", statusCode);
		body.put("timestamp", LocalDateTime.now());
        body.put("message", message);
        body.put("jsonObject",bool);
        response = new ResponseEntity<Object>(body, statusCode);
        return response;
	}
	
	public static ResponseEntity<Object> loginHandler(HttpStatus statusCode,GeneralAuthentication gAuth,Map<String, Object> body,ResponseEntity<Object> response){
		body.put("status", statusCode);
		body.put("timestamp", LocalDateTime.now());
        body.put("message", gAuth.getMessage());
        body.put("jsonObject",gAuth.getfToken());
        response = new ResponseEntity<Object>(body, statusCode);
        return response;
	}

	public static ResponseEntity<Object> generalHelperHandler(HttpStatus statusCode, String message, Student student, Map<String, Object> body, ResponseEntity<Object> response) {
		body.put("status", statusCode);
		body.put("timestamp", LocalDateTime.now());
        body.put("message", message);
        body.put("jsonObject",student);
        response = new ResponseEntity<Object>(body, statusCode);
        return response;
	}
	
	public static ResponseEntity<Object> generalHelperHandler(HttpStatus statusCode, String message, List<Student> student, Map<String, Object> body, ResponseEntity<Object> response) {
		body.put("status", statusCode);
		body.put("timestamp", LocalDateTime.now());
        body.put("message", message);
        body.put("jsonObject",student);
        response = new ResponseEntity<Object>(body, statusCode);
        return response;
	}
	
	public static ResponseEntity<Object> listHelperHandler(HttpStatus statusCode, String message, List<String> list, Map<String, Object> body, ResponseEntity<Object> response) {
		body.put("status", statusCode);
		body.put("timestamp", LocalDateTime.now());
        body.put("message", message);
        body.put("jsonObject",list);
        response = new ResponseEntity<Object>(body, statusCode);
        return response;
	}
	
	public static ResponseEntity<Object> paymentHelperHandler(HttpStatus statusCode, String message, PaymentModel payment, Map<String, Object> body, ResponseEntity<Object> response) {
		body.put("status", statusCode);
		body.put("timestamp", LocalDateTime.now());
        body.put("message", message);
        body.put("jsonObject",payment);
        response = new ResponseEntity<Object>(body, statusCode);
        return response;
	}
	
	public static ResponseEntity<Object> listRegHelperHandler(HttpStatus statusCode, String message, List<RegisterdStudent> list, Map<String, Object> body, ResponseEntity<Object> response) {
		body.put("status", statusCode);
		body.put("timestamp", LocalDateTime.now());
        body.put("message", message);
        body.put("jsonObject",list);
        response = new ResponseEntity<Object>(body, statusCode);
        return response;
	}
	public static ResponseEntity<Object> generalHelperHandler(HttpStatus statusCode, String message, Staff staff, Map<String, Object> body, ResponseEntity<Object> response) {
		body.put("status", statusCode);
		body.put("timestamp", LocalDateTime.now());
        body.put("message", message);
        body.put("jsonObject",staff);
        response = new ResponseEntity<Object>(body, statusCode);
        return response;
	}
	
	public static ResponseEntity<Object> payListRegHelperHandler(HttpStatus statusCode, String message, List<PaymentReceipt> list, Map<String, Object> body, ResponseEntity<Object> response) {
		body.put("status", statusCode);
		body.put("timestamp", LocalDateTime.now());
        body.put("message", message);
        body.put("jsonObject",list);
        response = new ResponseEntity<Object>(body, statusCode);
        return response;
	}
}
