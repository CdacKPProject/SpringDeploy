package com.RegistrationToken.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.RegistrationToken.Models.Student;
import com.RegistrationToken.Models.GeneralAuthentication;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.cloud.FirestoreClient;

@Service
public class StudentService {
	
	@Autowired
	AdminService adminService;
	
	public final String collectionStudent="StudentList";
	
	private Firestore getFirestoreConnection() {
		Firestore fs = FirestoreClient.getFirestore();
		return fs;
	}
	
	private BCryptPasswordEncoder getBcryptEncoder() {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(4);
		return encoder;
	}
	public String updatePassword(String oldPassword,String newPassword,String header) throws FirebaseAuthException,Exception
	{
		Firestore fs=getFirestoreConnection();
		BCryptPasswordEncoder encoder=getBcryptEncoder();
		Student stu = null;
		String prnNumber=checkHeaderAuthentication(header);
		if(!(prnNumber.isEmpty())) {
			DocumentReference documentReference = fs.collection(collectionStudent).document(prnNumber);//To get document of a specific user
			ApiFuture<DocumentSnapshot> doc = documentReference.get();
			try {
				DocumentSnapshot d=doc.get();
				if(d.exists()) {
					stu=d.toObject(Student.class);//map to student class object
					//System.out.println(stu.getPassword());
					boolean passwordMatched =encoder.matches(oldPassword, stu.getPassword());//checking password
					if(passwordMatched) {
						String sd=encoder.encode(newPassword);
						ApiFuture<WriteResult>doca=fs.collection(collectionStudent).document(prnNumber).update("password",sd);//setting data in collection
						return "Sucess";
					}else {
						return "Password doesn't match";
					}
				}else {
					return "Document doesnot exists";
				}
			} catch (InterruptedException | ExecutionException e) {
				throw(e);
			}
		}else {
			return "prnNumber invalid";
		}
		
	}
	
	public boolean profileUpdate(Student newProfile, String header) throws FirebaseAuthException {
		Firestore fs=getFirestoreConnection();
		boolean status = false;
		String uid=checkHeaderAuthentication(header);
		DocumentReference documentReference = fs.collection(collectionStudent).document(uid);//To get document of a specific user	
		ApiFuture<WriteResult> result =documentReference.update("name",newProfile.getName());//Update logic
		if(result.isCancelled()) {
			return status;
		}
		return status=true;
	}
	
	private String checkHeaderAuthentication(String header) throws FirebaseAuthException {
		header= header.replace("Bearer ", "");
		FirebaseToken decodeToken = FirebaseAuth.getInstance().verifyIdToken(header);
		String uid = decodeToken.getUid();
		return uid;
	}
	
	public GeneralAuthentication checkAuthentication(Student student) throws Exception {
		Firestore fs=getFirestoreConnection();
		BCryptPasswordEncoder encoder=getBcryptEncoder();
		GeneralAuthentication sAuth = new GeneralAuthentication(false,null,null);
		Student stu = null;
		DocumentReference documentReference = fs.collection(collectionStudent).document(student.getPrnNumber());
		ApiFuture<DocumentSnapshot> doc = documentReference.get();
		DocumentSnapshot dsnap;
		try {
			dsnap = doc.get();
			if(dsnap.exists()) {
				stu=dsnap.toObject(Student.class);
				boolean passwordMatched =encoder.matches(student.getPassword(), stu.getPassword());
				if(passwordMatched) {
					String uid = stu.getPrnNumber();
					String customToken = FirebaseAuth.getInstance().createCustomToken(uid);
					sAuth.setTokenStatus(passwordMatched);
					sAuth.setfToken(customToken);
					sAuth.setMessage("Login Sucessful");
					return sAuth;
				}else {
					sAuth.setMessage("Password mismatch");
				}
	 		}else {
	 			sAuth.setMessage("prnNumber doesnot exist in the collection");
	 		}
		} catch (InterruptedException | ExecutionException e) {
			throw(e);

		} catch (FirebaseAuthException e) {
			throw(e);
		}
		return sAuth;
	}

	public Student getStudentData(String prn) throws InterruptedException, ExecutionException {
		Firestore fs=getFirestoreConnection();
		Student stu = null;
		DocumentReference documentReference = fs.collection(collectionStudent).document(prn);//To get document of a specific user
		ApiFuture<DocumentSnapshot> doc = documentReference.get();
		DocumentSnapshot dsnap = doc.get();
		if(dsnap.exists()) {
			stu=dsnap.toObject(Student.class);
			return stu;
		}else {
			return stu;
		}		
	}


	
	public GeneralAuthentication lunchRegistration(List<Student> student, String authorization) throws FirebaseAuthException, InterruptedException, ExecutionException,Exception {
		GeneralAuthentication gen=null;
		try {
			String uid = checkHeaderAuthentication(authorization);
			Student stu = getStudentData(uid);	
			gen=adminService.addStudentToLunchList(student,stu);
			return gen;
		} catch (FirebaseAuthException e) {
			throw(e);
		} catch (InterruptedException e) {
			throw(e);
		} catch (ExecutionException e) {
			throw(e);
		} catch (Exception e) {
			throw(e);
		}
	}

	public GeneralAuthentication getDataOfStudent(String prnNumber, String authorization) throws FirebaseAuthException, InterruptedException, ExecutionException {
		Student student = null;
		GeneralAuthentication gAuth=new GeneralAuthentication(student,false,null);
		try {
			String uid = checkHeaderAuthentication(authorization);
			if(!uid.isEmpty()) {
				 student=getStudentData(prnNumber); 
				 Student stu=new Student(student.getPrnNumber(),student.getName(), student.getCourse());
				 gAuth.setStudent(stu);
				 gAuth.setMessage("Student Data got sucessfully");
				 gAuth.setTokenStatus(true);
			}
			return gAuth;
		} catch (FirebaseAuthException e) {
			throw(e);
		} catch (InterruptedException e) {
			throw(e);
		} catch (ExecutionException e) {
			throw(e);
		}	
	}

	public boolean checkTime() {
		final String afterFour = "16:00:00";
		final String beforeTen = "10:00:00";
		boolean isValid=false;
		String timeNow=null;
		DateTimeFormatter time = DateTimeFormatter.ofPattern("HH:mm:ss");	
		ZoneId zoneId = ZoneId.of("Asia/Kolkata");
		ZonedDateTime zone = ZonedDateTime.now(zoneId);
		timeNow=time.format(zone);
		
		if((timeNow.compareTo(beforeTen)<0) && (timeNow.compareTo(afterFour)>0)) {
			return isValid=true;
		}else {
			return isValid;
		}
	}
}
