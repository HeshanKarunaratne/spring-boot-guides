package com.example.springdataredis.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

/**
 * @author Heshan Karunaratne
 */
@Data
public class User implements Serializable {
    @Id
    private Long id;
    private String firstName;
    private String lastName;
    private String emailId;
    private int age;
}
