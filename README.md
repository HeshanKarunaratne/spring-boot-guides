# SpringBoot
- https://spring.io/guides#tutorials

1 . Building a REST Web Service

- Add 'Spring Web' dependency
- Creating Greeting record
~~~java
package com.example.restservice;
public record Greeting(long id, String content) { }
~~~

- Create Controller
~~~java
package com.example.restservice;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

@RestController
public class GreetingController {
    private static final String TEMPLATE = "Hello, %s! %s!";
    private final AtomicLong counter = new AtomicLong();

    @GetMapping("/greeting")
    public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name, @RequestParam(value = "feel", defaultValue = "How are you") String feel) {
        return new Greeting(counter.incrementAndGet(), String.format(TEMPLATE, name, feel));
    }
}
~~~
- A key difference between a traditional MVC controller and the RESTful web service controller shown earlier is the way that the HTTP response body is created. Rather than relying on a view technology to perform server-side rendering of the greeting data to HTML, this RESTful web service controller populates and returns a Greeting object. The object data will be written directly to the HTTP response as JSON.
- This code uses Spring @RestController annotation, which marks the class as a controller where every method returns a domain object instead of a view. It is shorthand for including both @Controller and @ResponseBody.
- Because Jackson 2 is on the classpath, Spring’s MappingJackson2HttpMessageConverter is automatically chosen to convert the Greeting instance to JSON.
- @SpringBootApplication is a convenience annotation that adds all of the following:
    - @Configuration: Tags the class as a source of bean definitions for the application context.
    - @EnableAutoConfiguration: Tells Spring Boot to start adding beans based on classpath settings, other beans, and various property settings. For example, if spring-webmvc is on the classpath, this annotation flags the application as a web application and activates key behaviors, such as setting up a DispatcherServlet.
    - @ComponentScan: Tells Spring to look for other components, configurations, and services in the com/example package, letting it find the controllers.
    
~~~java
package com.example.restservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GreetingController.class)
public class GreetingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void greetingShouldReturnMessage() throws Exception {
        String name = "Alice";
        String feel = "Fine";
        long id = 1;
        String message = String.format("Hello, %s! %s!", name, feel);

        Greeting expectedGreeting = new Greeting(id, message);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/greeting")
                .param("name", name)
                .param("feel", feel))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.content").value(containsString(message)))
                .andReturn();

        String content = result.getResponse().getContentAsString();
        Greeting actualGreeting = new ObjectMapper().readValue(content, Greeting.class);

        Assert.assertEquals(expectedGreeting, actualGreeting);
    }
}
~~~

2 . Scheduling Tasks
- Adding awaitility dependency
~~~text
testImplementation 'org.awaitility:awaitility:3.1.2'
~~~

~~~java
package com.example.schedulingtasks;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {
    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Scheduled(fixedRate = 5000)
    public void reportCurrentTime() {
        log.info("The time is now {}", dateFormat.format(new Date()));
    }
}
~~~

- The @EnableScheduling annotation ensures that a background task executor is created. Without it, nothing gets scheduled.
~~~java
package com.example.schedulingtasks;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SchedulingTasksApplication {
	public static void main(String[] args) {
		SpringApplication.run(SchedulingTasksApplication.class, args);
	}
}
~~~

- Add Test class
~~~java
package com.example.schedulingtasks;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScheduledTasksTest {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Test
    public void reportCurrentTime() {
        ScheduledTasks scheduledtasks = Mockito.mock(ScheduledTasks.class);
        scheduledtasks.reportCurrentTime();
        Mockito.verify(scheduledtasks, Mockito.atLeast(1)).reportCurrentTime();
    }

    @Test
    public void reportCurrentTimeTest() throws InterruptedException {
        ScheduledTasks scheduledTasks = new ScheduledTasks();
        scheduledTasks.reportCurrentTime();
        Thread.sleep(5000);
        String currentTime = dateFormat.format(new Date());
        Assertions.assertEquals("The time is now " + currentTime, scheduledTasks.reportCurrentTime());
    }
}
~~~

3 . Consuming a REST Web Service
- RestTemplate makes interacting with most RESTful services a one-line incantation. And it can even bind that data to custom domain types.
- Simple Java record class is annotated with @JsonIgnoreProperties from the Jackson JSON processing library to indicate that any properties not bound in this type should be ignored.
- To directly bind your data to your custom types, you need to specify the variable name to be exactly the same as the key in the JSON document returned from the API. In case your variable name and key in JSON doc do not match, you can use @JsonProperty annotation to specify the exact key of the JSON document.

~~~java
package com.example.consumingrest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ConsumingRestApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConsumingRestApplication.class, args);
    }
}
~~~

~~~java
package com.example.consumingrest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Random;
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
~~~

~~~java
package com.example.consumingrest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class Config {
    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
~~~