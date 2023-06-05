package com.example.graphQL.repository;

import com.example.graphQL.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Heshan Karunaratne
 */
public interface PersonRepository extends JpaRepository<Person, Integer> {
    Person findByEmail(String email);
}
