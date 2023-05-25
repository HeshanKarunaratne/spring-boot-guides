package com.example.jparelationships.student;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Heshan Karunaratne
 */
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
}