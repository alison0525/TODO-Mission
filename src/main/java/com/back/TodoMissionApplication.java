package com.back;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class TodoMissionApplication {

    public static void main(String[] args) {
        SpringApplication.run(TodoMissionApplication.class, args);
    }

}
