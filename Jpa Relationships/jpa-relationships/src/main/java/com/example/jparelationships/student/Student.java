package com.example.jparelationships.student;


import com.example.jparelationships.subject.Subject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Heshan Karunaratne
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    private String name;

    @JsonIgnore // to stop iterating
    @ManyToMany(mappedBy = "enrolledStudents") // mapped by fieldName in Subject
    private Set<Subject> subjects = new HashSet<>();
}