package com.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableAutoConfiguration
@SpringBootApplication
//@ComponentScan("com.springboot.*")
public class SpringbootLaunch {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootLaunch.class, args);
	}

}