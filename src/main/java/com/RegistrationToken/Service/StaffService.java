package com.RegistrationToken.Service;

import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.RegistrationToken.Models.GeneralAuthentication;
import com.RegistrationToken.Models.Staff;
import com.RegistrationToken.Models.Student;
import com.RegistrationToken.Models.VerifiyingCoupons;
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
public class StaffService {

		@Autowired
		AdminService adminService;
	
	  public final String collectionStaff = "StaffList";
	  
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
			Staff st=null;
			String staffId=checkHeaderAuthentication(header);
			if(!(staffId.isEmpty())) {
				DocumentReference documentReference = fs.collection(collectionStaff).document(staffId);//To get document of a specific user
				ApiFuture<DocumentSnapshot> doc = documentReference.get();
				try {
					DocumentSnapshot d=doc.get();
					if(d.exists()) {
						st=d.toObject(Staff.class);//map to student class object
						boolean passwordMatched =encoder.matches(oldPassword, st.getPassword());//checking password
						if(passwordMatched) {
							String sd=encoder.encode(newPassword);
							ApiFuture<WriteResult>doca=fs.collection(collectionStaff).document(staffId).update("password",sd);//setting data in collection
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
		
		public boolean profileUpdate(Staff newProfile, String header) throws FirebaseAuthException {
			Firestore fs=getFirestoreConnection();
			boolean status = false;
			String uid=checkHeaderAuthentication(header);
			DocumentReference documentReference = fs.collection(collectionStaff).document(uid);//To get document of a specific user	
			ApiFuture<WriteResult> result =documentReference.update("staffName",newProfile.getStaffName());//Update logic
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

		public GeneralAuthentication checkAuthentication(Staff staff) throws FirebaseAuthException,Exception {
			Firestore fs=getFirestoreConnection();
			BCryptPasswordEncoder encoder=getBcryptEncoder();
			GeneralAuthentication sAuth = new GeneralAuthentication(false,null,null);
			Staff st=null;
			DocumentReference documentReference = fs.collection(collectionStaff).document(staff.getStaffId());
			ApiFuture<DocumentSnapshot> doc = documentReference.get();
			DocumentSnapshot dsnap;
			try {
				dsnap = doc.get();
				if(dsnap.exists()) {
					st=dsnap.toObject(Staff.class);
					boolean passwordMatched =encoder.matches(staff.getPassword(), st.getPassword());
					if(passwordMatched) {
						String uid = st.getStaffId();
						String customToken = FirebaseAuth.getInstance().createCustomToken(uid);
						sAuth.setTokenStatus(passwordMatched);
						sAuth.setfToken(customToken);
						sAuth.setMessage("Login Sucessful");
						return sAuth;
					}else {
						sAuth.setMessage("Password mismatch");
					}
		 		}else {
		 			sAuth.setMessage("staffId doesnot exist in the collection");
		 		}
			} catch (InterruptedException | ExecutionException e) {
				throw(e);

			} catch (FirebaseAuthException e) {
				throw(e);
			}
			return sAuth;
		}


		public GeneralAuthentication checkForLunch(VerifiyingCoupons verCoupons, String authorization) throws Exception {
			GeneralAuthentication genAuth=null;
			try {
				String uid = checkHeaderAuthentication(authorization);
				if(!(uid.isEmpty())) {
					genAuth=adminService.checkInDbForLunch(verCoupons.getPrnNumber());
				}
				return genAuth;
			} catch (FirebaseAuthException e) {
				throw(e);
			} catch (Exception e) {
				throw(e);
			}
		}

		public GeneralAuthentication checkForTea(VerifiyingCoupons verCoupons, String authorization) throws FirebaseAuthException, ExecutionException, InterruptedException {
			GeneralAuthentication genAuth=null;
			try {
				String uid = checkHeaderAuthentication(authorization);
				if(!(uid.isEmpty())) {
					genAuth=adminService.checkInDbForTea(verCoupons);
				}
				return genAuth;
			} catch (FirebaseAuthException e) {
				throw(e);
			} catch (InterruptedException e) {
				throw(e);
			} catch (ExecutionException e) {
				throw(e);
			}
		}
		

}
