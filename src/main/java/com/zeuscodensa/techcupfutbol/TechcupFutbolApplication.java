package com.zeuscodensa.techcupfutbol;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;


@SpringBootApplication
@EnableAsync
public class TechcupFutbolApplication {

	public static void main(String[] args) {
		SpringApplication.run(TechcupFutbolApplication.class, args);
	}

}
