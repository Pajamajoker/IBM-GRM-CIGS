package com.ibm.cigs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@ComponentScan(basePackages = { "com.ibm.cigs" })
public class CigsApplication {
	public static void main(String[] args) {
		SpringApplication.run(CigsApplication.class, args);
	}

}
