package com.example.filestorageservice.repository;

import com.example.filestorageservice.entity.FileData;
import com.example.filestorageservice.entity.ImageData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author Heshan Karunaratne
 */
public interface FileDataRepository extends JpaRepository<FileData, Integer> {
    Optional<FileData> findByName(String fileName);
}
