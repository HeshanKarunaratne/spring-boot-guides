package com.example.filestorageservice.service;

import com.example.filestorageservice.entity.ImageData;
import com.example.filestorageservice.repository.StorageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

/**
 * @author Heshan Karunaratne
 */
@Service
@RequiredArgsConstructor
public class StorageService {
    private final StorageRepository repository;

    public String uploadFile(MultipartFile file) throws IOException {

        ImageData imageData = repository.save(ImageData
                .builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
//                .imageData(ImageUtils.compressImage(file.getBytes()))
                .imageData(file.getBytes())
                .build());

        if (imageData != null)
            return "File Uploaded Successfully: " + file.getOriginalFilename();

        return null;
    }

    public byte[] downloadImage(String fileName) {
        Optional<ImageData> dbImageData = repository.findByName(fileName);
//        byte[] bytes = ImageUtils.decompressImage(dbImageData.get().getImageData());
        byte[] bytes = dbImageData.get().getImageData();
        return bytes;
    }
}
