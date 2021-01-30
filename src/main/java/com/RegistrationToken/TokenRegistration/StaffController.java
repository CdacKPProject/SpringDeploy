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
import org.springframework.web.bind.annotation.RestController;
import com.RegistrationToken.Helpers.ControllerExceptionHandler;
import com.RegistrationToken.Helpers.ControllerHelperHandler;
import com.RegistrationToken.Models.GeneralAuthentication;
import com.RegistrationToken.Models.PasswordChange;
import com.RegistrationToken.Models.RegisterdStudent;
import com.RegistrationToken.Models.Staff;
import com.RegistrationToken.Models.VerifiyingCoupons;
import com.RegistrationToken.Service.StaffService;
import com.google.firebase.auth.FirebaseAuthException;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/staff")
public class StaffController {

	@Autowired
	StaffService staffService;
	
	final static Logger logger = Logger.getLogger(StaffController.class);

	@PutMapping("/updatePassword")
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
			return ControllerExceptionHandler.FirebaseInternalExceptionHandler(body, response, msg);
		} catch (Exception e) {
			logger.error("Internal Exception : "+e.getMessage());
			String msg="Internal server error";
			return ControllerExceptionHandler.InternalExceptionHandler(body, response, msg);
		}
	}

	@PutMapping("/updateProfile")
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
			return ControllerExceptionHandler.FirebaseInternalExceptionHandler(body, response, msg);
		}
	}
	
	@PostMapping("/staffLogin")
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
				String msg="Firebase Internal Error";
				return ControllerExceptionHandler.FirebaseInternalExceptionHandler(body, response, msg);
			} catch (Exception e) {
				logger.error("Internal Exception : "+e.getMessage());
				String msg="Internal server error";
				return ControllerExceptionHandler.InternalExceptionHandler(body, response, msg);
			}
			
	}
	
	@PostMapping("/lunchRegCheck")
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
				logger.error("Firebase Exception : "+e.getMessage());
				String msg="Firebase Internal Error";
				return ControllerExceptionHandler.FirebaseInternalExceptionHandler(body, response, msg);
			}catch (Exception e) {
				logger.error("Internal Exception : "+e.getMessage());
				String msg="Internal server error";
				return ControllerExceptionHandler.InternalExceptionHandler(body, response, msg);
			}
	}
	
	@GetMapping("/getRegCollection")
	public ResponseEntity<Object> getCollectionReg(@RequestHeader("Authorization") String Authorization){
		ResponseEntity<Object> response = null;
		Map<String, Object> body = new LinkedHashMap<>();
		try {
			List<RegisterdStudent> listReceived = staffService.getRegCollection(Authorization);
			if(listReceived.size()>0) {
				return ControllerHelperHandler.listRegHelperHandler(HttpStatus.OK, "Recived Registered List", listReceived, body, response);
			}else {
				return ControllerHelperHandler.listRegHelperHandler(HttpStatus.OK, "Registered List not available for current date", listReceived, body, response);
			}
		} catch (FirebaseAuthException e) {
			logger.error("Firebase Exception : "+e.getMessage());
			String msg="Firebase Internal Error";
			return ControllerExceptionHandler.FirebaseInternalExceptionHandler(body, response, msg);
		} catch (ExecutionException | InterruptedException e) {
			logger.error("Internal Exception : "+e.getMessage());
			String msg="Internal server error";
			return ControllerExceptionHandler.InternalExceptionHandler(body, response, msg);
		} 
	}
}
