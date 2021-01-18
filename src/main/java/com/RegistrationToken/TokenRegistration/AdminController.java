package com.RegistrationToken.TokenRegistration;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.servlet.http.HttpServletResponse;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.RegistrationToken.Helpers.ControllerExceptionHandler;
import com.RegistrationToken.Helpers.ControllerHelperHandler;
import com.RegistrationToken.Models.Admin;
import com.RegistrationToken.Models.GeneralAuthentication;
import com.RegistrationToken.Models.PasswordChange;
import com.RegistrationToken.Models.Staff;
import com.RegistrationToken.Models.Student;

import com.RegistrationToken.Service.AdminService;

import com.google.firebase.auth.FirebaseAuthException;


@CrossOrigin(origins = "*")
@RestController
public class AdminController {

	@Autowired
	AdminService adminService;
	
	final static Logger logger = Logger.getLogger(AdminController.class);
	
	@PostMapping("/admin/uploadCSVStudentFile") 
	 public ResponseEntity<Object> uploadCSVFile(@RequestParam("file") MultipartFile file,@RequestHeader("Authorization") String Authorization) {
		ResponseEntity<Object> response=null;
		Map<String, Object> body = new LinkedHashMap<>();
			if(file.isEmpty()) {
				return ControllerHelperHandler.generalHelperHandler(HttpStatus.NOT_FOUND, "File is empty", false, body, response);
			 }else {
				 boolean res;
				try {
					res = adminService.parseCSVFile(file,Authorization);
					if(res) {
						 return ControllerHelperHandler.generalHelperHandler(HttpStatus.CREATED, "File uploaded sucessfully", res, body, response);
					 }
					 return ControllerHelperHandler.generalHelperHandler(HttpStatus.CONFLICT, "File uploaded failed", res, body, response);
				} catch (FirebaseAuthException e) {
					logger.error("Firebase Exception : "+e.getMessage());
					String msg="Firebase server error";
					return ControllerExceptionHandler.InternalExceptionHandler(body, response, msg);
				} catch (IOException e) {
					logger.error("Internal Exception : "+e.getMessage());
					String msg="CSV File Format error";
					return ControllerExceptionHandler.InternalExceptionHandler(body, response, msg);
				} 
			 }
	}
	
	@PostMapping("/admin/uploadCSVStaffFile") 
	 public ResponseEntity<Object> uploadCSVStaffFile(@RequestParam("file") MultipartFile file,@RequestHeader("Authorization") String Authorization) {
		ResponseEntity<Object> response=null;
		Map<String, Object> body = new LinkedHashMap<>();
		if(file.isEmpty()) {
			return ControllerHelperHandler.generalHelperHandler(HttpStatus.NOT_FOUND, "File is empty", false, body, response);
		 }else {
			 boolean res;
			 try {
					res= adminService.parseCSVStaffFile(file,Authorization);
					if(res) {
						return ControllerHelperHandler.generalHelperHandler(HttpStatus.CREATED, "File uploaded sucessfully", res, body, response);
					}
					return ControllerHelperHandler.generalHelperHandler(HttpStatus.CONFLICT, "File uploaded failed", res, body, response);
				} catch (FirebaseAuthException e) {
					logger.error("Firebase Exception : "+e.getMessage());
					String msg="Firebase server error";
					return ControllerExceptionHandler.InternalExceptionHandler(body, response, msg);
				} catch (IOException e) {
					logger.error("Internal Exception : "+e.getMessage());
					String msg="CSV File Format error";
					return ControllerExceptionHandler.InternalExceptionHandler(body, response, msg);
				} 
		 }
	}
	@PutMapping("/admin/resetStudentPassword")
	public ResponseEntity<Object> resetStudentPassword(@RequestBody Student student,@RequestHeader("Authorization") String Authorization) {
		ResponseEntity<Object> response=null;
		Map<String, Object> body = new LinkedHashMap<>();
	    try {
			GeneralAuthentication gAuth=adminService.resetStudent(student.getPrnNumber(),Authorization);
			if(gAuth.isTokenStatus()) {
				return ControllerHelperHandler.generalHelperHandler(HttpStatus.OK,gAuth.getMessage(), gAuth.isTokenStatus(), body, response);
			}
			return ControllerHelperHandler.generalHelperHandler(HttpStatus.OK,gAuth.getMessage(), gAuth.isTokenStatus(), body, response);
		} catch (FirebaseAuthException e) {
			logger.error("Firebase Exception : "+e.getMessage());
			String msg="Firebase server error";
			return ControllerExceptionHandler.InternalExceptionHandler(body, response, msg);
		} catch (ExecutionException | InterruptedException e) {
			logger.error("Internal Exception : "+e.getMessage());
			String msg="Internal server error";
			return ControllerExceptionHandler.InternalExceptionHandler(body, response, msg);
		} 
	}
	
	@PostMapping("/admin/addStudent")
	public ResponseEntity<Object> addStudent(@RequestBody Student student,@RequestHeader("Authorization") String Authorization) 
	{
		ResponseEntity<Object> response=null;
		Map<String, Object> body = new LinkedHashMap<>();
		try {
			GeneralAuthentication gAuth=adminService.addStudent(student,Authorization);
			if(gAuth.isTokenStatus()) {
				return ControllerHelperHandler.generalHelperHandler(HttpStatus.CREATED,gAuth.getMessage(), gAuth.isTokenStatus(), body, response);
			}
			return ControllerHelperHandler.generalHelperHandler(HttpStatus.OK,gAuth.getMessage(), gAuth.isTokenStatus(), body, response);
		} catch (FirebaseAuthException e) {
			logger.error("Firebase Exception : "+e.getMessage());
			String msg="Firebase server error";
			return ControllerExceptionHandler.InternalExceptionHandler(body, response, msg);
		}
	}
	
	@PutMapping("/admin/resetStaffPassword")
	public ResponseEntity<Object> resetStaffPassword(@RequestBody Staff staff,@RequestHeader("Authorization") String Authorization) {
		ResponseEntity<Object> response=null;
		Map<String, Object> body = new LinkedHashMap<>();
	    try {
			GeneralAuthentication gAuth=adminService.resetStaff(staff.getStaffId(),Authorization);
			if(gAuth.isTokenStatus()) {
				return ControllerHelperHandler.generalHelperHandler(HttpStatus.OK,gAuth.getMessage(), gAuth.isTokenStatus(), body, response);
			}
			return ControllerHelperHandler.generalHelperHandler(HttpStatus.OK,gAuth.getMessage(), gAuth.isTokenStatus(), body, response);
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
	
	@PostMapping("/admin/addStaff")
	public ResponseEntity<Object> addStaff(@RequestBody Staff staff,@RequestHeader("Authorization") String Authorization) 
	{
		ResponseEntity<Object> response=null;
		Map<String, Object> body = new LinkedHashMap<>();
		try {
			GeneralAuthentication gAuth=adminService.addStaff(staff,Authorization);
			if(gAuth.isTokenStatus()) {
				return ControllerHelperHandler.generalHelperHandler(HttpStatus.CREATED,gAuth.getMessage(), gAuth.isTokenStatus(), body, response);
			}
			return ControllerHelperHandler.generalHelperHandler(HttpStatus.OK,gAuth.getMessage(), gAuth.isTokenStatus(), body, response);
		} catch (FirebaseAuthException e) {
			logger.error("Firebase Exception : "+e.getMessage());
			String msg="Firebase server error";
			return ControllerExceptionHandler.InternalExceptionHandler(body, response, msg);
		}
	}
	
	@GetMapping("/export")//check with frontend mandatorly
	public ResponseEntity<Object> exportData(HttpServletResponse res,@RequestParam("date") String date,@RequestHeader("Authorization") String Authorization){
		ResponseEntity<Object> response = null;
		Map<String, Object> body = new LinkedHashMap<>();
		GeneralAuthentication sAuth=null;
		try {
			sAuth=adminService.downloadFile(res,date,Authorization);
			if(sAuth.isTokenStatus()) {
				return ControllerHelperHandler.generalHelperHandler(HttpStatus.CREATED,sAuth.getMessage(), sAuth.isTokenStatus(), body, response);
			}
			return ControllerHelperHandler.generalHelperHandler(HttpStatus.OK,sAuth.getMessage(), sAuth.isTokenStatus(), body, response);
		} catch (FirebaseAuthException e) {
			logger.error("Firebase Exception : "+e.getMessage());
			String msg="Firebase server error";
			return ControllerExceptionHandler.InternalExceptionHandler(body, response, msg);
		} catch (IOException |InterruptedException |ExecutionException e) {
			logger.error("Internal Exception : "+e.getMessage());
			String msg="Internal server error";
			return ControllerExceptionHandler.InternalExceptionHandler(body, response, msg);
		} 
	}

	@PostMapping("/admin/adminLogin")
	public ResponseEntity<Object> adminLogin(@RequestBody Admin admin){
		ResponseEntity<Object> response = null;
		Map<String, Object> body = new LinkedHashMap<>();
			GeneralAuthentication sAuth;
			try {
				sAuth = adminService.checkAuthentication(admin);
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
	
	@PutMapping("/admin/updateProfile")
	public ResponseEntity<Object> updateAdminProfile(@RequestBody Admin newProfile,@RequestHeader("Authorization") String Authorization) {
		ResponseEntity<Object> response=null;
		Map<String, Object> body = new LinkedHashMap<>();
		try {
			boolean res=adminService.profileUpdate(newProfile,Authorization);
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
	
	@PutMapping("/admin/updatePassword")
	public ResponseEntity<Object> updateStaffPassword(@RequestBody PasswordChange spc,@RequestHeader("Authorization") String Authorization) {
		ResponseEntity<Object> response=null;
		Map<String, Object> body = new LinkedHashMap<>();
		try {
			String res=adminService.updatePassword(spc.getOldPassword(),spc.getNewPassword(),Authorization);
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
	
	@GetMapping("/admin/getAllStudents")
	public ResponseEntity<Object> getAllStudentsData(@RequestHeader("Authorization") String Authorization){
		ResponseEntity<Object> response=null;
		Map<String, Object> body = new LinkedHashMap<>();
		try {
			List<Student> list =adminService.getAllStudent(Authorization);
			if(list.size()!=0) {
				return ControllerHelperHandler.generalHelperHandler(HttpStatus.OK, "List of Students", list, body, response);
			}
			return ControllerHelperHandler.generalHelperHandler(HttpStatus.CONFLICT, "Error", list, body, response);
		} catch (InterruptedException | ExecutionException e) {
			logger.error("Internal Exception : "+e.getMessage());
			String msg="Internal server error";
			return ControllerExceptionHandler.InternalExceptionHandler(body, response, msg);
		} catch (FirebaseAuthException e) {
			e.printStackTrace();logger.error("Firebase Exception : "+e.getMessage());
			String msg="Firebase Internal Error";
			return ControllerExceptionHandler.InternalExceptionHandler(body, response, msg);
		}
	}

}
