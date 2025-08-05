package com.cts.trainers_application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TrainersApplication {

	public static void main(String[] args) {
		SpringApplication.run(TrainersApplication.class, args);

		System.out.println("🚀 Trainers Application Started Successfully!");
		System.out.println("📋 Access the application at: http://localhost:9098");
		System.out.println("🔗 API Base URL: http://localhost:9098/api/trainers");
	}
}
