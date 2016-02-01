package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.example.dao.LockableEntityRepositoryImpl;

@SpringBootApplication
@EnableJpaRepositories(repositoryBaseClass = LockableEntityRepositoryImpl.class)
public class TestconfigurationApplication {

	public static void main(String[] args) {
		SpringApplication.run(TestconfigurationApplication.class, args);
	}
}
