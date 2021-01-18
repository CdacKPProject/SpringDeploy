package com.RegistrationToken.TokenRegistration;


import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan(basePackages = {"com.RegistrationToken"})
public class TokenRegistrationApplication {

	final static Logger logger = Logger.getLogger(TokenRegistrationApplication.class);
	public static void main(String[] args) {
		logger.info("Start");
		SpringApplication.run(TokenRegistrationApplication.class, args);
	}

}
