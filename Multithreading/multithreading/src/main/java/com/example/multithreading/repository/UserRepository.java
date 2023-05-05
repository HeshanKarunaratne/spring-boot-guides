package com.example.multithreading.repository;

import com.example.multithreading.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Heshan Karunaratne
 */
public interface UserRepository extends JpaRepository<User, Integer> {
}
