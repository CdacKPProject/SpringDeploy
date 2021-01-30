package com.RegistrationToken.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

@Service
public class FirebaseIntializer {

	final static Logger logger = Logger.getLogger(FirebaseIntializer.class);
	
	@PostConstruct
	public void Intialize() {
		InputStream firebaseConnection;
		try {
                        String token = System.getenv("GOOGLE_CREDENTIALS");
                        InputStream is = new ByteArrayInputStream(token.getBytes());
			firebaseConnection = is;
			FirebaseOptions.Builder builder = FirebaseOptions.builder();
		
			FirebaseOptions options = builder
					  .setCredentials(GoogleCredentials.fromStream(firebaseConnection))
					  .build();
		
			FirebaseApp.initializeApp(options);
		}  catch (IOException e) {
			logger.error("IOException"+e.getMessage());
		}
	}
	
	
}
