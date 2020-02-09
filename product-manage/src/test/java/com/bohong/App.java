package com.bohong;


import javax.sql.DataSource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;


@SpringBootApplication
public class App {

 

	@Bean
	@ConfigurationProperties(prefix = "website.datasource")
	public DataSource dataSource() {
		return DataSourceBuilder.create().build();
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

 

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}

}