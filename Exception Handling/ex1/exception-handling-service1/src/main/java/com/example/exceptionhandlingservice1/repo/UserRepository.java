package com.example.exceptionhandlingservice1.repo;

import com.example.exceptionhandlingservice1.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Heshan Karunaratne
 */
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUserId(int id);
}
