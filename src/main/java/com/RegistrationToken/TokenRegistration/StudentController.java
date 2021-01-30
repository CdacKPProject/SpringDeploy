package com.RegistrationToken.TokenRegistration;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.RegistrationToken.Models.Student;
import com.RegistrationToken.Models.GeneralAuthentication;
import com.RegistrationToken.Helpers.ControllerExceptionHandler;
import com.RegistrationToken.Helpers.ControllerHelperHandler;
import com.RegistrationToken.Models.PasswordChange;
import com.RegistrationToken.Models.PaymentModel;
import com.RegistrationToken.Service.StudentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.firebase.auth.FirebaseAuthException;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/student")
public class StudentController {

	@Autowired
	StudentService studentService;
	
	final static Logger logger = Logger.getLogger(StudentController.class);
    
	@PutMapping("/updatePassword")
	public ResponseEntity<Object> updateStudentPassword(@RequestBody PasswordChange spc,@RequestHeader("Authorization") String Authorization) {
		ResponseEntity<Object> response=null;
		Map<String, Object> body = new LinkedHashMap<>();
		try {
			String res=studentService.updatePassword(spc.getOldPassword(),spc.getNewPassword(),Authorization);
			if(res.equals("Sucess")) {
				return ControllerHelperHandler.generalHelperHandler(HttpStatus.OK, "Password Updated Sucessfully", true, body, response);
			}else {
				return ControllerHelperHandler.generalHelperHandler(HttpStatus.UNAUTHORIZED, res, false, body, response);
			}
		} catch (FirebaseAuthException e) {
			logger.error("Firebase Exception : "+e.getMessage());
			String msg="Firebase Internal Error";
			return ControllerExceptionHandler.FirebaseInternalExceptionHandler(body, response, msg);
		} catch (Exception e) {
			logger.error("Internal Exception : "+e.getMessage());
			String msg="Internal server error";
			return ControllerExceptionHandler.InternalExceptionHandler(body, response, msg);
		}
	}
	
	@PutMapping("/updateProfile")
	public ResponseEntity<Object> updateStudentProfile(@RequestBody Student newProfile,@RequestHeader("Authorization") String Authorization) {
		ResponseEntity<Object> response=null;
		Map<String, Object> body = new LinkedHashMap<>();
		try {
			boolean res=studentService.profileUpdate(newProfile,Authorization);
			if(res) {
				return ControllerHelperHandler.generalHelperHandler(HttpStatus.OK, "Profile Updated Sucessfully", res, body, response);
			}
			return ControllerHelperHandler.generalHelperHandler(HttpStatus.OK, "Profile Update failed", res, body, response);
		} catch (FirebaseAuthException e) {
			logger.error("Firebase Exception : "+e.getMessage());
			String msg="Firebase Internal Error";
			return ControllerExceptionHandler.FirebaseInternalExceptionHandler(body, response, msg);
		}
	}
	
	
	@PostMapping("/studentLogin")
	public ResponseEntity<Object> loginStudent(@RequestBody Student student){
		ResponseEntity<Object> response = null;
		Map<String, Object> body = new LinkedHashMap<>();
		GeneralAuthentication sAuth=null;
			try {
				sAuth = studentService.checkAuthentication(student);
				if(sAuth.isTokenStatus()) {
					return ControllerHelperHandler.loginHandler(HttpStatus.OK, sAuth, body, response);
				}else {
					return ControllerHelperHandler.loginHandler(HttpStatus.NOT_FOUND, sAuth, body, response);
				}
			} catch (FirebaseAuthException e) {
				logger.error("Firebase Exception : "+e.getMessage());
				String msg="Firebase Internal Error";
				return ControllerExceptionHandler.FirebaseInternalExceptionHandler(body, response, msg);
			} catch (Exception e) {
				logger.error("Internal Exception : "+e.getMessage());
				String msg="Internal server error";
				return ControllerExceptionHandler.InternalExceptionHandler(body, response, msg);
			}
	}
	
	@PostMapping("/studentRegLunch")
	public ResponseEntity<Object> registerForLunch(@RequestBody List<Student> student,@RequestHeader("Authorization") String Authorization){
		ResponseEntity<Object> response = null;
		Map<String, Object> body = new LinkedHashMap<>();
		GeneralAuthentication sAuth=null;
		try {
			if(studentService.checkTime()) {
				sAuth=studentService.lunchRegistration(student,Authorization);
				if(sAuth.isTokenStatus()) {
					return ControllerHelperHandler.generalHelperHandler(HttpStatus.CREATED, sAuth.getMessage(), sAuth.isTokenStatus(), body, response);
				}else {
					return ControllerHelperHandler.generalHelperHandler(HttpStatus.CONFLICT, sAuth.getMessage(), sAuth.isTokenStatus(), body, response);
				}
			}else {
				return ControllerHelperHandler.generalHelperHandler(HttpStatus.BAD_REQUEST, "Registration Timeout,can't register between 10AM and 4PM", false, body, response);
			}
			
		} catch (FirebaseAuthException e) {
			logger.error("Firebase Exception : "+e.getMessage());
			String msg="Firebase Internal Error";
			return ControllerExceptionHandler.FirebaseInternalExceptionHandler(body, response, msg);
		}  catch (Exception e) {
			logger.error("Internal Exception : "+e.getMessage());
			String msg="Internal server error";
			return ControllerExceptionHandler.InternalExceptionHandler(body, response, msg);
		}
		
	}
	
	@GetMapping("/getStudent")
	public ResponseEntity<Object> getStudentByPrnNumber(@RequestParam("prnNumber") String prnNumber,@RequestHeader("Authorization") String Authorization){
		ResponseEntity<Object> response = null;
		Map<String, Object> body = new LinkedHashMap<>();
		GeneralAuthentication sAuth=null;
		try {
			sAuth=studentService.getDataOfStudent(prnNumber,Authorization);
			if(sAuth.isTokenStatus()) {
				return ControllerHelperHandler.generalHelperHandler(HttpStatus.OK, sAuth.getMessage(), sAuth.getStudent(), body, response);
			}else {
				return ControllerHelperHandler.generalHelperHandler(HttpStatus.CONFLICT, sAuth.getMessage(), sAuth.getStudent(), body, response);
			}
		} catch (FirebaseAuthException e) {
			logger.error("Firebase Exception : "+e.getMessage());
			String msg="Firebase Internal Error";
			return ControllerExceptionHandler.FirebaseInternalExceptionHandler(body, response, msg);
		} catch (InterruptedException | ExecutionException  e) {
			logger.error("Internal Exception : "+e.getMessage());
			String msg="Internal server error";
			return ControllerExceptionHandler.InternalExceptionHandler(body, response, msg);
		} 
	}
	
	@GetMapping("/getOrderId")
	public ResponseEntity<Object> getOrderIdForPayment(@RequestParam("amount") String amount,@RequestHeader("Authorization") String Authorization){
		ResponseEntity<Object> response = null;
		Map<String, Object> body = new LinkedHashMap<>();
		GeneralAuthentication sAuth=null;
		try {
			sAuth=studentService.orderIdForPayment(amount,Authorization);
			if(sAuth.isTokenStatus()) {
				return ControllerHelperHandler.paymentHelperHandler(HttpStatus.OK, sAuth.getMessage(), sAuth.getPayment(), body, response);
			}else {
				return ControllerHelperHandler.paymentHelperHandler(HttpStatus.CONFLICT, sAuth.getMessage(), sAuth.getPayment(), body, response);
			}
		} catch (FirebaseAuthException e) {
			logger.error("Firebase Exception : "+e.getMessage());
			String msg="Firebase Internal Error";
			return ControllerExceptionHandler.FirebaseInternalExceptionHandler(body, response, msg);
		} catch (JsonProcessingException e) {
			logger.error("JSON Exception : "+e.getMessage());
			String msg="Internal server error";
			return ControllerExceptionHandler.InternalExceptionHandler(body, response, msg);
		}
	}
	
	@PostMapping("/checkSignatureForPayment")
	public ResponseEntity<Object> checkSignature(@RequestBody PaymentModel payment,@RequestHeader("Authorization") String Authorization){
		ResponseEntity<Object> response = null;
		Map<String, Object> body = new LinkedHashMap<>();
		GeneralAuthentication sAuth=null;
		try {
			sAuth=studentService.checkPaymentSignature(payment,Authorization);
			if(sAuth.isTokenStatus()) {
				return ControllerHelperHandler.generalHelperHandler(HttpStatus.OK, sAuth.getMessage(), sAuth.isTokenStatus(), body, response);
			}else {
				return ControllerHelperHandler.generalHelperHandler(HttpStatus.CONFLICT, sAuth.getMessage(), sAuth.isTokenStatus(), body, response);
			}
		} catch (FirebaseAuthException e) {
			logger.error("Firebase Exception : "+e.getMessage());
			String msg="Firebase Internal Error";
			return ControllerExceptionHandler.FirebaseInternalExceptionHandler(body, response, msg);
		}  catch (Exception e) {
			logger.error("Internal Exception : "+e.getMessage());
			String msg="Internal server error";
			return ControllerExceptionHandler.InternalExceptionHandler(body, response, msg);
		} 
	}
}
