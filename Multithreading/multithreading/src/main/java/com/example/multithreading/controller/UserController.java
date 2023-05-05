package com.example.multithreading.controller;

import com.example.multithreading.entity.User;
import com.example.multithreading.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author Heshan Karunaratne
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = "application/json")
    public ResponseEntity saveUsers(@RequestParam(value = "files") MultipartFile[] files) throws Exception {
        for (MultipartFile file : files)
            userService.saveUser(file);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping(produces = "application/json")
    public CompletableFuture<ResponseEntity> findUsers() {
        return userService.findAllUsers().thenApply(ResponseEntity::ok);
    }

    @GetMapping(value = "/multithread", produces = "application/json")
    public ResponseEntity getUsers() {
        CompletableFuture<List<User>> users1 = userService.findAllUsers();
        CompletableFuture<List<User>> users2 = userService.findAllUsers();
        CompletableFuture<List<User>> users3 = userService.findAllUsers();

        CompletableFuture.allOf(users1, users2, users3).join();
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
