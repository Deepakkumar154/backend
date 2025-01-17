package org.tragoit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class TragoITApplication {
    public static void main(String... args) {
        SpringApplication.run(TragoITApplication.class, args);
    }
}
