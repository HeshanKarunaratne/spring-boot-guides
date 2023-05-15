package com.example.springdataredis.service;

import com.example.springdataredis.entity.User;

import java.util.List;

/**
 * @author Heshan Karunaratne
 */
public interface UserService {
    boolean saveUser(User user);

    List<User> fetchAllUser();

    User fetchUserById(Long id);

    boolean deleteUser(Long id);

    boolean updateUser(User user, Long id);
}
