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
