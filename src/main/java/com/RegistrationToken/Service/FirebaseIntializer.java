package com.RegistrationToken.Service;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

@Service
public class FirebaseIntializer {

	@PostConstruct
	public void Intialize() {
		InputStream firebaseConnection;
		try {
			String token = System.getenv("GOOGLE_CREDENTIALS");
			InputStream is = new ByteArrayInputStream(token.getBytes());
			
			firebaseConnection= is;
			FirebaseOptions.Builder builder = FirebaseOptions.builder();

			FirebaseOptions options = builder
					  .setCredentials(GoogleCredentials.fromStream(firebaseConnection))
					  .build();

			FirebaseApp.initializeApp(options);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	


}

