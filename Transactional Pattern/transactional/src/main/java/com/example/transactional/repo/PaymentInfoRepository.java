package com.example.transactional.repo;

import com.example.transactional.entity.PaymentInfo;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Heshan Karunaratne
 */
public interface PaymentInfoRepository extends JpaRepository<PaymentInfo, String> {
}
