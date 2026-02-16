package com.flat.flat_management_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude =
		org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration.class)
public class FlatManagementBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(FlatManagementBackendApplication.class, args);
	}

}
