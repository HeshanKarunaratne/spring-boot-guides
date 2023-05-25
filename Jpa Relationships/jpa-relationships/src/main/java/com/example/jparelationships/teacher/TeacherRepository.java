package com.example.jparelationships.teacher;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Heshan Karunaratne
 */
@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {}