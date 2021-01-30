package com.RegistrationToken.Helpers;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ControllerExceptionHandler {

	public static ResponseEntity<Object> InternalExceptionHandler(Map<String, Object> body,ResponseEntity<Object> response,String message){
		body.put("status", HttpStatus.INTERNAL_SERVER_ERROR);
		body.put("timestamp", LocalDateTime.now());
        body.put("message", message);
        body.put("jsonObject",null);
        response = new ResponseEntity<Object>(body, HttpStatus.INTERNAL_SERVER_ERROR);
        return response;
	}
	
	public static ResponseEntity<Object> FirebaseInternalExceptionHandler(Map<String, Object> body,ResponseEntity<Object> response,String message){
		body.put("status", HttpStatus.BAD_GATEWAY);
		body.put("timestamp", LocalDateTime.now());
        body.put("message", message);
        body.put("jsonObject",null);
        response = new ResponseEntity<Object>(body, HttpStatus.BAD_GATEWAY);
        return response;
	}
}
