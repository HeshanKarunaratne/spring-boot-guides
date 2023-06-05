package com.example.graphQL.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Heshan Karunaratne
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Person {
    @Id
    private int id;
    private String name;
    private String mobile;
    private String email;
    private String[] address;
}
