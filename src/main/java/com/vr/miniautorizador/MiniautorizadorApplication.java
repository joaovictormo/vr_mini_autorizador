package com.vr.miniautorizador;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication()
@EnableMongoRepositories(basePackages = "com.vr.miniautorizador.repositories")
public class MiniautorizadorApplication {

  public static void main(final String[] args) {
    SpringApplication.run(MiniautorizadorApplication.class, args);
  }
}
