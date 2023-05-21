package com.example.filestorageservice.repository;

import com.example.filestorageservice.entity.ImageData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author Heshan Karunaratne
 */
public interface StorageRepository extends JpaRepository<ImageData, Long> {
    Optional<ImageData> findByName(String fileName);
}
