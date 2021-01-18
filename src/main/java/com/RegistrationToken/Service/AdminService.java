package com.RegistrationToken.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.RegistrationToken.Models.Admin;
import com.RegistrationToken.Models.GeneralAuthentication;
import com.RegistrationToken.Models.RegisterdStudent;
import com.RegistrationToken.Models.Staff;
import com.RegistrationToken.Models.Student;
import com.RegistrationToken.Models.StudentExcel;
import com.RegistrationToken.Models.VerifiyingCoupons;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.cloud.FirestoreClient;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

@Service
@Configuration
@EnableScheduling
public class AdminService {

	private final String collectionStudent = "StudentList";
	private final String collectionStaff = "StaffList";
	private final String collectionAdmin="AdminData";
	private String regStudentList="RegStudData";
	
	private Firestore getFirestoreConnection() {
		Firestore fs = FirestoreClient.getFirestore();
		return fs;
	}
	
	private BCryptPasswordEncoder getBcryptEncoder() {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(4);
		return encoder;
	}	
	
	@Scheduled(cron = "0 30 15 * * MON-SAT",zone="GMT+5:30")
	private void stringData() {
		regStudentList=null;
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");	
		ZoneId zoneId = ZoneId.of("Asia/Kolkata");
		ZonedDateTime zone = ZonedDateTime.now(zoneId);
		regStudentList ="RegStudentList"+dtf.format(zone);
		System.out.println(regStudentList);
	}
	
	 public boolean parseCSVFile(MultipartFile file, String Authorization) throws FirebaseAuthException, IOException 
	  { 
		  boolean status = false; 
		  String header;
		try {
			header = checkHeaderAuthentication(Authorization);
			if(header.isEmpty()) {
				  return status;
			  }else {
				  Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream())); // create csv bean reader
				  CsvToBean<Student> csvToBean = new CsvToBeanBuilder(reader).withType(Student.class).withIgnoreLeadingWhiteSpace(true).build(); // convert `CsvToBean` object to list of records 
				  List<Student> records = csvToBean.parse(); 
				  String hashedPassword ; // save users in DB? 
				  for(int i = 0; i<records.size(); i++){
				  records.get(i).setPrnNumber(records.get(i).getPrnNumber().trim());
				  hashedPassword = getHashedPassword(records.get(i).getPrnNumber()); 
				  
				  Student student = new Student(records.get(i).getPrnNumber(),records.get(i).getName(),records.get(i).getBatch(),records.get(i).getCourse(),records.get(i).getMobileNumber(),hashedPassword);
				  status=addStudentData(student); 
				  } 
				  return status;
				  } 
		} 
		catch (FirebaseAuthException e) {
			throw(e);
		}
	    catch (IOException e)
		{
			throw(e);
		}
	 }

	private boolean addStudentData(Student student) 
	{ 
		Firestore fs = getFirestoreConnection();
		ApiFuture<WriteResult>result =fs.collection(collectionStudent).document(student.getPrnNumber()).set(student);
		if(result.isCancelled())
		{ 
		  return false; 
		}
	  return true;
	 }

	private String getHashedPassword(String prnNumber) {
		BCryptPasswordEncoder encoder = getBcryptEncoder();
		String passwordAfterHashed = encoder.encode(prnNumber);
		return passwordAfterHashed;
	}
	
	public GeneralAuthentication resetStudent(String prn, String authorization) throws FirebaseAuthException, ExecutionException, InterruptedException {
		Firestore fs = getFirestoreConnection();
		GeneralAuthentication gen=new GeneralAuthentication(false,null);
		try {
			String header=checkHeaderAuthentication(authorization);
			if(isStudentExists(prn)) {
			if(!header.isEmpty()) {
				    String restedValue = getHashedPassword(prn);
					ApiFuture<WriteResult> result =fs.collection(collectionStudent).document(prn).update("password",restedValue);//reset logic
					if(result.isCancelled()) {
						gen.setMessage("prnNumber is wrong");
						return gen;
					}
					gen.setMessage("password reseted sucessfully");
					gen.setTokenStatus(true);
					return gen;
				}
			  return gen;
			}
			gen.setMessage("Wrong prnNumber");
			return gen;
		} 
		catch (FirebaseAuthException e) {
			throw(e);
		} catch (InterruptedException e) {
			throw(e);
		} catch (ExecutionException e) {
			throw(e);
		}
		
	}
	
	private boolean isStudentExists(String prn) throws InterruptedException, ExecutionException {
		boolean status = false;
		List<Student> stuExists=new ArrayList<Student>();
		Firestore fs=getFirestoreConnection();
		ApiFuture<QuerySnapshot> future =fs.collection(collectionStudent).get();
		List<QueryDocumentSnapshot> documents;
		try {
			documents = future.get().getDocuments();
			for (DocumentSnapshot document : documents) {
				Student rs = document.toObject(Student.class);
				stuExists.add(rs);
				}
			for(Student lst : stuExists) {
				if(lst.getPrnNumber().equals(prn)) {
					return status=true;
				}
			}
			return status;
		} catch (InterruptedException |ExecutionException e) {
			throw(e);
		} 
	}
	

	public GeneralAuthentication addStudent(Student student, String authorization) throws FirebaseAuthException {
		Firestore fs = getFirestoreConnection();
		GeneralAuthentication gen=new GeneralAuthentication(false,null);
		try {
			String header=checkHeaderAuthentication(authorization);
			if(!header.isEmpty()) {
				String hashedPassword = getHashedPassword(student.getPrnNumber());
				student.setPassword(hashedPassword);
				ApiFuture<WriteResult> result =fs.collection(collectionStudent).document(student.getPrnNumber()).set(student);	
				if(result.isCancelled()) {
					gen.setMessage("PrnNumber is Invalid");
					return gen;
				}
				gen.setTokenStatus(true);
				gen.setMessage("Inserted sucessfully");
				return gen;
			}
			return gen;
			
		} catch (FirebaseAuthException e) {
			throw(e);
		}
		
		
	}

	public GeneralAuthentication resetStaff(String staffId, String authorization) throws Exception {
		Firestore fs = getFirestoreConnection();
		GeneralAuthentication gen=new GeneralAuthentication(false,null);
		try {
			String header=checkHeaderAuthentication(authorization);
			if(isStaffIdExists(staffId)) {
				if(!header.isEmpty()) {
					String restedValue = getHashedPassword(staffId);
					ApiFuture<WriteResult> result =fs.collection(collectionStaff).document(staffId).update("password",restedValue);//reset logic
					if(result.isCancelled()) {
						gen.setMessage("StaffId is wrong");
						return gen;
					}
					gen.setMessage("password reseted sucessfully");
					gen.setTokenStatus(true);
					return gen;
				}
				return gen;
			}
			gen.setMessage("Wrong StaffID");
			return gen;
		} 
		catch (FirebaseAuthException e) {
			throw(e);
		} catch (Exception e) {
			throw(e);
		}
	}

	private boolean isStaffIdExists(String staffId) throws Exception {
		boolean status = false;
		List<Staff> staffExists=new ArrayList<Staff>();
		Firestore fs=getFirestoreConnection();
		ApiFuture<QuerySnapshot> future =fs.collection(collectionStaff).get();
		List<QueryDocumentSnapshot> documents;
		try {
			documents = future.get().getDocuments();
			for (DocumentSnapshot document : documents) {
				Staff rs = document.toObject(Staff.class);
				staffExists.add(rs);
				}
			for(Staff lst : staffExists) {
				if(lst.getStaffId().equals(staffId)) {
					return status=true;
				}
			}
			return status;
		} catch (InterruptedException |ExecutionException e) {
			throw(e);
		} 
	}

	public GeneralAuthentication addStaff(Staff staff, String authorization) throws FirebaseAuthException {
		Firestore fs = getFirestoreConnection();
		GeneralAuthentication gen=new GeneralAuthentication(false,null);
		try {
			String header=checkHeaderAuthentication(authorization);
			if(!header.isEmpty()) {
				String hashedPassword = getHashedPassword(staff.getStaffId());
				staff.setPassword(hashedPassword);
				ApiFuture<WriteResult> result =fs.collection(collectionStaff).document(staff.getStaffId()).set(staff);	
				if(result.isCancelled()) {
					gen.setMessage("StaffId is Invalid");
					return gen;
				}
				gen.setTokenStatus(true);
				gen.setMessage("Inserted sucessfully");
				return gen;
			}
			return gen;
			
		} catch (FirebaseAuthException e) {
			throw(e);
		}
	}

	 public boolean parseCSVStaffFile(MultipartFile file, String Authorization) throws IOException, FirebaseAuthException 
	  { 
		 boolean status = false; 
		 String header;
		 
		  try{
			  header = checkHeaderAuthentication(Authorization);
			if(header.isEmpty()) {
					  return status;
			}else {
				Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream())); // create csv bean reader
				  CsvToBean<Staff> csvToBean = new CsvToBeanBuilder(reader).withType(Staff.class).withIgnoreLeadingWhiteSpace(true).build(); // convert `CsvToBean` object to list of records  
				  List<Staff> records = csvToBean.parse(); 
				  String hashedPassword ; // save users in DB? 
				  for(int i = 0; i<records.size(); i++){
					  records.get(i).setStaffId(records.get(i).getStaffId().trim());
				  hashedPassword = getHashedPassword(records.get(i).getStaffId());
				  Staff staff = new  Staff(records.get(i).getStaffId(),records.get(i).getStaffName(),hashedPassword,records.get(i).getMobileNumber());
				  status=addStaff(staff); 
				  } 
				  return status;
				  } 
			}
		  catch (IOException e)
		  {
			  throw(e); 
		  } catch (FirebaseAuthException e) {
				throw(e);
			}
	 
	 }
	 
		private boolean addStaff(Staff staff) 
		{ 
			Firestore fs = getFirestoreConnection();
			ApiFuture<WriteResult>result =fs.collection(collectionStaff).document(staff.getStaffId()).set(staff);
			if(result.isCancelled())
			{ 
			  return false; 
			}
		  return true;
		 }
	 public List<Student> getAllStudent(String authorization) throws InterruptedException,ExecutionException,FirebaseAuthException {
		 List<Student> lst = new ArrayList<Student>();
		 try {
				String uid = checkHeaderAuthentication(authorization);
				if(!uid.isEmpty()) {
					
					Firestore dbFirestore = FirestoreClient.getFirestore();
					CollectionReference stu = dbFirestore.collection(collectionStudent);
					ApiFuture<QuerySnapshot> qs = stu.get();
					for (DocumentSnapshot doc : qs.get().getDocuments()) {
						Student s = doc.toObject(Student.class);
						Student studentToSend = new Student(s.getPrnNumber(),s.getName(),s.getBatch(),s.getCourse(),s.getMobileNumber(),s.foodCoupon,s.teaCofeeCoupon);
						lst.add(studentToSend);
					}
					return lst;
				}
				return lst;
			} catch (FirebaseAuthException e) {
				throw(e);
			}catch (InterruptedException | ExecutionException e) {
				throw(e);
			} 
			
		}

		public GeneralAuthentication checkAuthentication(Admin admin) throws FirebaseAuthException,Exception {
			Firestore fs=getFirestoreConnection();
			BCryptPasswordEncoder encoder=getBcryptEncoder();
			GeneralAuthentication sAuth = new GeneralAuthentication(false,null,null);
			Admin ad=null;
			DocumentReference documentReference = fs.collection(collectionAdmin).document(admin.getAdminId());
			ApiFuture<DocumentSnapshot> doc = documentReference.get();
			DocumentSnapshot dsnap;
			try {
				dsnap = doc.get();
				if(dsnap.exists()) {
					ad=dsnap.toObject(Admin.class);
					boolean passwordMatched =encoder.matches(admin.getPassword(), ad.getPassword());
					if(passwordMatched) {
						String uid = ad.getAdminId();
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
		
		public boolean profileUpdate(Admin newProfile, String header) throws FirebaseAuthException {
			Firestore fs=getFirestoreConnection();
			boolean status = false;
			String uid=checkHeaderAuthentication(header);
			DocumentReference documentReference = fs.collection(collectionAdmin).document(uid);//To get document of a specific user	
			ApiFuture<WriteResult> result =documentReference.update("adminName",newProfile.getAdminName(),"mobileNumber",newProfile.getMobileNumber());//Update logic
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
		
		public String updatePassword(String oldPassword,String newPassword,String header) throws FirebaseAuthException,Exception
		{
			Firestore fs=getFirestoreConnection();
			BCryptPasswordEncoder encoder=getBcryptEncoder();
			Admin ad = null;
			String adminId=checkHeaderAuthentication(header);
			if(!(adminId.isEmpty())) {
				DocumentReference documentReference = fs.collection(collectionAdmin).document(adminId);//To get document of a specific user
				ApiFuture<DocumentSnapshot> doc = documentReference.get();
				try {
					DocumentSnapshot d=doc.get();
					if(d.exists()) {
						ad=d.toObject(Admin.class);//map to student class object
						boolean passwordMatched =encoder.matches(oldPassword, ad.getPassword());//checking password
						if(passwordMatched) {
							String sd=encoder.encode(newPassword);
							ApiFuture<WriteResult>doca=fs.collection(collectionAdmin).document(adminId).update("password",sd);//setting data in collection
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

	
		private boolean isRegisterd(String prnNumber) throws Exception {
			boolean status = false;
			List<RegisterdStudent> regStu=new ArrayList<RegisterdStudent>();
			Firestore fs=getFirestoreConnection();
			ApiFuture<QuerySnapshot> future =fs.collection(regStudentList).get();
			List<QueryDocumentSnapshot> documents;
			try {
				documents = future.get().getDocuments();
				for (DocumentSnapshot document : documents) {
					RegisterdStudent rs = document.toObject(RegisterdStudent.class);
					regStu.add(rs);
					}
				for(RegisterdStudent lst : regStu) {
					if(lst.getPrnNumber().equals(prnNumber)) {
						return status=true;
					}
				}
				return status;
			} catch (InterruptedException e) {
				throw(e);
			} catch (ExecutionException e) {
				throw(e);
			}
			
		}

		public GeneralAuthentication downloadFile(HttpServletResponse response, String date, String authorization) throws FirebaseAuthException, ExecutionException, IOException, InterruptedException {
			GeneralAuthentication gen = new GeneralAuthentication(false, null);
			try {
				String uid=checkHeaderAuthentication(authorization);
				if(!(uid.isEmpty())){
				String findCollection="RegisertedStudentList"+date;
				boolean findColStatus=isPresent(findCollection);
				if(findColStatus) {
				startDownloading(response,findCollection);
				gen.setTokenStatus(true);
				gen.setMessage("file downloaded sucessfully");
				return gen;
				}else {
					gen.setMessage("collection not found");
					return gen;
				}
				}
				gen.setMessage("UnAuthorized");
				return gen;
			} catch (FirebaseAuthException e) {
				throw(e);
			} catch (ExecutionException e) {
				throw(e);
			} catch (IOException e) {
				throw(e);
			} catch (InterruptedException e) {
				throw(e);
			}
			
		}

		
		private void startDownloading(HttpServletResponse response, String findCollection) throws ExecutionException, IOException, InterruptedException  {
			response.setContentType("application/octet-stream");
			String headerkey="Content-Disposition";		
			String filename =findCollection+".xlsx";	
			String headerVaalue="attachment;filename="+filename;
			response.setHeader(headerkey, headerVaalue);
			List<RegisterdStudent> lst;
			try {
				lst = getDownloadData(findCollection);
				StudentExcel ex = new StudentExcel(lst); 
				ex.exportDataExcel(response);
			} catch (InterruptedException e) {
				throw(e);
			} catch (IOException e) {
				throw(e);
			} catch (ExecutionException e) {
				throw(e);
			}
			
		}

		
		private List<RegisterdStudent> getDownloadData(String findCollection) throws InterruptedException, ExecutionException {
			List<RegisterdStudent> lst = new ArrayList<RegisterdStudent>();
			Firestore dbFirestore = FirestoreClient.getFirestore();
			CollectionReference stu = dbFirestore.collection(findCollection);
			ApiFuture<QuerySnapshot> qs = stu.get();
			try {
				for (DocumentSnapshot doc : qs.get().getDocuments()) {
					RegisterdStudent s = doc.toObject(RegisterdStudent.class);
					lst.add(s);
				}
			} catch (InterruptedException e) {
				throw(e);
			} catch (ExecutionException e) {
				throw(e);
			}
			return lst;
		}

		private boolean isPresent(String findCollection) {
			Firestore fs=getFirestoreConnection();
			Iterable<CollectionReference> allCol = fs.listCollections();
			List<String> result = new ArrayList<String>(); 
		    for (CollectionReference str : allCol) {
		    	System.out.println(str.getPath());
		        result.add(str.getPath());
		    }
		    for(String str:result) {
		    	if(findCollection.equals(str)) {
		    		return true;
		    	}
		    }
			return false;
		}

		public GeneralAuthentication checkInDbForLunch(String prnNumber) throws Exception  {
			Firestore fs=getFirestoreConnection();
			RegisterdStudent regStudent=null;
			GeneralAuthentication gAuth = new GeneralAuthentication(false, null);
			try {
				boolean isRegStatus=isRegisterd(prnNumber);
				if(isRegStatus) {
					regStudent=getRegStudentObj(prnNumber);
					if(!regStudent.isEatUpStatus()) {
						fs.collection(regStudentList).document(prnNumber).update("eatUpStatus",true);
						gAuth.setTokenStatus(true);
						gAuth.setMessage("Issuue lunch");
					}else {
						gAuth.setMessage("Lunch Already Taken");
					}
					return gAuth;
				}
				gAuth.setMessage("Student Not Registered");
				return gAuth;
			} catch (Exception e) {
				throw(e);
			}
			
		}
		
		private RegisterdStudent getRegStudentObj(String prn) throws InterruptedException, ExecutionException {
			Firestore fs=getFirestoreConnection();
			RegisterdStudent stu = null;
			DocumentReference documentReference = fs.collection(regStudentList).document(prn);//To get document of a specific user
			ApiFuture<DocumentSnapshot> doc = documentReference.get();
			DocumentSnapshot dsnap;
			try {
				dsnap = doc.get();
				if(dsnap.exists()) {
					stu=dsnap.toObject(RegisterdStudent.class);
					return stu;
				}else {
					return stu;
				}	
			} catch (InterruptedException e) {
				throw(e);
			} catch (ExecutionException e) {
				throw(e);
			}
				
		}
		
		private Student getStudentObj(String prn) throws InterruptedException, ExecutionException {
			Firestore fs=getFirestoreConnection();
			Student stu = null;
			DocumentReference documentReference = fs.collection(collectionStudent).document(prn);//To get document of a specific user
			ApiFuture<DocumentSnapshot> doc = documentReference.get();
			DocumentSnapshot dsnap;
			try {
				dsnap = doc.get();
				if(dsnap.exists()) {
					stu=dsnap.toObject(Student.class);
					return stu;
				}else {
					return stu;
				}	
			} catch (InterruptedException e) {
				throw(e);
			} catch (ExecutionException e) {
				throw(e);
			}
				
		}
		

		public GeneralAuthentication checkInDbForTea(VerifiyingCoupons verCoupons) throws InterruptedException, ExecutionException {
			Firestore fs=getFirestoreConnection();
			GeneralAuthentication gAuth = new GeneralAuthentication(false, null);
			try {
				Student student = getStudentObj(verCoupons.getPrnNumber());
				if(student.getTeaCofeeCoupon()>=verCoupons.getNoOfCups()) {
					student.setTeaCofeeCoupon(student.getTeaCofeeCoupon()-verCoupons.getNoOfCups());
					fs.collection(collectionStudent).document(student.getPrnNumber()).update("teaCofeeCoupon",student.getTeaCofeeCoupon());
					gAuth.setTokenStatus(true);
					gAuth.setMessage("Give "+verCoupons.getNoOfCups()+" Tea/Coffee cups");
				}
				else {
					gAuth.setMessage("Insufficient coupons");
				}
				return gAuth;
			} catch (InterruptedException e) {
				throw(e);
			} catch (ExecutionException e) {
				throw(e);
			}
		}


		public GeneralAuthentication addStudentToLunchList(List<Student> student, Student stu) throws Exception {
			Firestore fs=getFirestoreConnection();
			GeneralAuthentication gAuth = new GeneralAuthentication(false, null);
			List<Student> notRegisteredForLunch = new ArrayList<Student>();
			List<Student> RegisteredForLunch = new ArrayList<Student>();
			if(stu.getFoodCoupon()>=student.size()) {
				for(Student checkStudent:student) {
					boolean regStatus=isRegisterd(checkStudent.getPrnNumber());
					if(!regStatus) {
						notRegisteredForLunch.add(checkStudent);
					}else {
						RegisteredForLunch.add(checkStudent);
					}
				}
				for(Student addToLunchReg :notRegisteredForLunch ) {
					registerForLunchDb(addToLunchReg);
				}
				stu.setFoodCoupon(stu.getFoodCoupon()-notRegisteredForLunch.size());
				fs.collection(collectionStudent).document(stu.getPrnNumber()).update("foodCoupon",stu.getFoodCoupon());
				if(student.size()== notRegisteredForLunch.size()) {
					String getDate = getTheValidDate();
					gAuth.setMessage("students registered for lunch sucessfully "+getDate);
					gAuth.setTokenStatus(true);	
				}else {
					String getDate = getTheValidDate();
					String msg="";
					for(Student alredyRegStudent:RegisteredForLunch) {
						msg += alredyRegStudent.getPrnNumber()+ " ";
					}
					gAuth.setTokenStatus(true);
					gAuth.setMessage("Registered Sucessfully for lunch "+getDate+" , except for the following Prn's "+msg+ " since they are already registered");
				}
				return gAuth;
			}else {
				gAuth.setMessage("Insufficient Coupons");
				return gAuth;
			}
		}

		private String getTheValidDate() {
			final String afterFour = "16:00:00";
			final String beforeTen = "10:00:00";
			String dateNow = null;
			String timeNow=null;
			DateTimeFormatter date = DateTimeFormatter.ofPattern("dd-MM-yyyy");
			DateTimeFormatter time = DateTimeFormatter.ofPattern("HH:mm:ss");	
			ZoneId zoneId = ZoneId.of("Asia/Kolkata");
			ZonedDateTime zone = ZonedDateTime.now(zoneId);
			timeNow=time.format(zone);
			dateNow=date.format(zone);	
			if((timeNow.compareTo(afterFour)>0) ) {
				zone=zone.plusDays(1);
				String[] trimDate=zone.toString().split("T");
				dateNow=date.format(zone);
				return dateNow;
			}else if(timeNow.compareTo(beforeTen)<0) {
				return dateNow;
			}
			return null;
		}

		private void registerForLunchDb(Student addToLunchReg) throws InterruptedException, ExecutionException {
			Firestore fs=getFirestoreConnection();
			Student getStudentDetails=getStudentObj(addToLunchReg.getPrnNumber());
			RegisterdStudent regStud = new RegisterdStudent(getStudentDetails.getPrnNumber(),getStudentDetails.getName(),getStudentDetails.getCourse(),false);
			ApiFuture<WriteResult>doc=fs.collection(regStudentList).document(regStud.getPrnNumber()).set(regStud);
		}

		
}
