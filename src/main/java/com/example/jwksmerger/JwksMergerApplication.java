package com.example.jwksmerger;

import com.example.jwksmerger.configuration.JwksMergerProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties(JwksMergerProperties.class)
public class JwksMergerApplication {

    public static void main(String[] args) {
        SpringApplication.run(JwksMergerApplication.class, args);
    }
}
