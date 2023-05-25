package com.example.jparelationships.subject;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Heshan Karunaratne
 */
@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {}
