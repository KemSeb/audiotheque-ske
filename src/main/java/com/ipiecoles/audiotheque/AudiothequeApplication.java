package com.ipiecoles.audiotheque;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.persistence.EntityManagerFactory;

@SpringBootApplication
public class AudiothequeApplication {
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private EntityManagerFactory entityManagerFactory;
	public static void main(String[] args) {
		SpringApplication.run(AudiothequeApplication.class, args);
	}

}
