package com.example.gatewayservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author Heshan Karunaratne
 */
@Configuration
public class AppConfig {

    @Bean
    public RestTemplate template() {
        return new RestTemplate();
    }
}
