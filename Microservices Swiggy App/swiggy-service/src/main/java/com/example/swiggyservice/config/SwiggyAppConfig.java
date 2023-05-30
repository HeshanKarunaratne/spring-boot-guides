package com.example.swiggyservice.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author Heshan Karunaratne
 */
@Configuration
public class SwiggyAppConfig {

    @Bean
    @LoadBalanced
    public RestTemplate template(){
        return new RestTemplate();
    }
}