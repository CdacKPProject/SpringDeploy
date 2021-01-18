package com.RegistrationToken.TokenRegistration;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.RegistrationToken.Helpers.ControllerExceptionHandler;
import com.RegistrationToken.Helpers.ControllerHelperHandler;
import com.RegistrationToken.Models.GeneralAuthentication;
import com.RegistrationToken.Models.PasswordChange;
import com.RegistrationToken.Models.Staff;
import com.RegistrationToken.Models.VerifiyingCoupons;
import com.RegistrationToken.Service.StaffService;
import com.google.firebase.auth.FirebaseAuthException;
@CrossOrigin(origins = "*")
@RestController
public class StaffController {

	@Autowired
	StaffService staffService;
	
	final static Logger logger = Logger.getLogger(StaffController.class);

	@PutMapping("/staff/updatePassword")
	public ResponseEntity<Object> updateStaffPassword(@RequestBody PasswordChange spc,@RequestHeader("Authorization") String Authorization) {
		ResponseEntity<Object> response=null;
		Map<String, Object> body = new LinkedHashMap<>();
		try {
			String res=staffService.updatePassword(spc.getOldPassword(),spc.getNewPassword(),Authorization);
			if(res.equals("Sucess")) {
				return ControllerHelperHandler.generalHelperHandler(HttpStatus.OK, "Password Updated Sucessfully", true, body, response);
			}else {
				return ControllerHelperHandler.generalHelperHandler(HttpStatus.UNAUTHORIZED, res, false, body, response);
			}
		} catch (FirebaseAuthException e) {
			logger.error("Firebase Exception : "+e.getMessage());
			String msg="Firebase Internal Error";
			return ControllerExceptionHandler.InternalExceptionHandler(body, response, msg);
		} catch (Exception e) {
			logger.error("Internal Exception : "+e.getMessage());
			String msg="Internal server error";
			return ControllerExceptionHandler.InternalExceptionHandler(body, response, msg);
		}
	}

	@PutMapping("/staff/updateProfile")
	public ResponseEntity<Object> updateStaffProfile(@RequestBody Staff newProfile,@RequestHeader("Authorization") String Authorization) {
		ResponseEntity<Object> response=null;
		Map<String, Object> body = new LinkedHashMap<>();
		try {
			boolean res=staffService.profileUpdate(newProfile,Authorization);
			if(res) {
				return ControllerHelperHandler.generalHelperHandler(HttpStatus.OK, "Profile Updated Sucessfully", res, body, response);
			}
			return ControllerHelperHandler.generalHelperHandler(HttpStatus.OK, "Profile Update failed", res, body, response);
		} catch (FirebaseAuthException e) {
			logger.error("Firebase Exception : "+e.getMessage());
			String msg="Firebase Internal Error";
			return ControllerExceptionHandler.InternalExceptionHandler(body, response, msg);
		}
	}
	
	@PostMapping("/staff/staffLogin")
	public ResponseEntity<Object> staffLogin(@RequestBody Staff staff){
		ResponseEntity<Object> response = null;
		Map<String, Object> body = new LinkedHashMap<>();
			GeneralAuthentication sAuth;
			try {
				sAuth = staffService.checkAuthentication(staff);
				if(sAuth.isTokenStatus()) {
					return ControllerHelperHandler.loginHandler(HttpStatus.OK, sAuth, body, response);
				}else {
					return ControllerHelperHandler.loginHandler(HttpStatus.NOT_FOUND, sAuth, body, response);
				}
			} catch (FirebaseAuthException e) {
				logger.error("Firebase Exception : "+e.getMessage());
				String msg="Firebase server error";
				return ControllerExceptionHandler.InternalExceptionHandler(body, response, msg);
			} catch (Exception e) {
				logger.error("Internal Exception : "+e.getMessage());
				String msg="Internal server error";
				return ControllerExceptionHandler.InternalExceptionHandler(body, response, msg);
			}
			
	}
	
	@PostMapping("/staff/lunchRegCheck")
	public ResponseEntity<Object> checkLunRegistration(@RequestBody VerifiyingCoupons verCoupons,@RequestHeader("Authorization") String Authorization){
		ResponseEntity<Object> response = null;
		Map<String, Object> body = new LinkedHashMap<>();
		GeneralAuthentication sAuth;
			try {
				if(!verCoupons.isCouponStatus()) {
					sAuth=staffService.checkForLunch(verCoupons,Authorization);
					if(sAuth.isTokenStatus()) {
						return ControllerHelperHandler.generalHelperHandler(HttpStatus.OK,sAuth.getMessage(), sAuth.isTokenStatus(), body, response);
					}
					return ControllerHelperHandler.generalHelperHandler(HttpStatus.CONFLICT, sAuth.getMessage(), sAuth.isTokenStatus(), body, response);
				}else {
					sAuth=staffService.checkForTea(verCoupons,Authorization);
					if(sAuth.isTokenStatus()) {
						return ControllerHelperHandler.generalHelperHandler(HttpStatus.OK,sAuth.getMessage(), sAuth.isTokenStatus(), body, response);
					}
					return ControllerHelperHandler.generalHelperHandler(HttpStatus.CONFLICT, sAuth.getMessage(), sAuth.isTokenStatus(), body, response);
				}
			} catch (FirebaseAuthException e) {
				logger.error("Internal Exception : "+e.getMessage());
				String msg="Firebase server error";
				return ControllerExceptionHandler.InternalExceptionHandler(body, response, msg);
			}catch (Exception e) {
				logger.error("Internal Exception : "+e.getMessage());
				String msg="Internal server error";
				return ControllerExceptionHandler.InternalExceptionHandler(body, response, msg);
			}
	}

}
