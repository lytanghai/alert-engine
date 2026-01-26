package com.finance.alert_engine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AlertEngineApplication {

	public static void main(String[] args) {
		SpringApplication.run(AlertEngineApplication.class, args);
	}

}
