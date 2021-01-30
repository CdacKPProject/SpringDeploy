package com.RegistrationToken.TokenRegistration;


import java.util.Collections;

import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@SpringBootApplication
@ComponentScan(basePackages = {"com.RegistrationToken"})
public class TokenRegistrationApplication {

	final static Logger logger = Logger.getLogger(TokenRegistrationApplication.class);
	public static void main(String[] args) {
		logger.info("Start");
		SpringApplication.run(TokenRegistrationApplication.class, args);
	}
	
	@Bean
	public Docket swaggerConfig() {
		 
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.RegistrationToken"))
				.build()
				.apiInfo(apiDetails());
	}

	private ApiInfo apiDetails() {
		return new ApiInfo(
				"Online Lunch Registration RESTful Web Service docs",
				"This Page documents about web endpoints",
				"1.0",
				"https://lunch-check-app.herokuapp.com/v2/api-docs",
				new springfox.documentation.service.Contact("Admin","https://cdaclunchregistration.herokuapp.com/home/contact","cdaclunchkp@gmail.com"),
				"Apache 2.0",
				"http://www.apache.org/licenses/LICENSE-2.0",
				Collections.emptyList());
	}
}
