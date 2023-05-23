package com.example.filestorageservice.service;

import com.example.filestorageservice.entity.FileData;
import com.example.filestorageservice.entity.ImageData;
import com.example.filestorageservice.repository.FileDataRepository;
import com.example.filestorageservice.repository.StorageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;

/**
 * @author Heshan Karunaratne
 */
@Service
@RequiredArgsConstructor
public class StorageService {
    private final StorageRepository repository;
    private final FileDataRepository fileDataRepository;
    private final String FOLDER_PATH = "C:/Users/hkarunaratne/Desktop/Files/MyFiles/";

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

    public String uploadImageToFileSystem(MultipartFile file) throws IOException {
        String filePath = FOLDER_PATH + file.getOriginalFilename();
        FileData fileData = fileDataRepository.save(FileData
                .builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .filePath(filePath)
                .build());

        file.transferTo(new File(filePath));

        if (fileData != null)
            return "File Uploaded Successfully: " + filePath;

        return null;
    }

    public byte[] downloadImageFromFileSystem(String fileName) throws IOException {
        Optional<FileData> dbImageData = fileDataRepository.findByName(fileName);
        String filePath = dbImageData.get().getFilePath();
        return Files.readAllBytes(new File(filePath).toPath());
    }
}
