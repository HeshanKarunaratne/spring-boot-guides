package com.example.consumingrest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Random;

/**
 * @author Heshan Karunaratne
 */
@Service
public class QuoteService {

    private final RestTemplate restTemplate;
    private static final Logger log = LoggerFactory.getLogger(QuoteService.class);

    @Autowired
    public QuoteService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Scheduled(fixedRate = 5000)
    public void callRestEndpoint() {
        int random = new Random().nextInt(0,100);

        User user = restTemplate.getForObject(
                "https://jsonplaceholder.typicode.com/posts/" + random, User.class);
        log.info(user.toString());
    }
}
