package com.teamdevsolution.webreactive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@SpringBootApplication
@EnableReactiveMongoRepositories
public class WebReactiveApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebReactiveApplication.class, args);
	}

}
