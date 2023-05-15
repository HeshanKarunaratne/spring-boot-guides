package com.example.transactional.repo;

import com.example.transactional.entity.PassengerInfo;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Heshan Karunaratne
 */
public interface PassengerInfoRepository extends JpaRepository<PassengerInfo, Long> {
}
