package com.example.exceptionhandlingservice1.service;

import com.example.exceptionhandlingservice1.dto.UserRequest;
import com.example.exceptionhandlingservice1.entity.User;
import com.example.exceptionhandlingservice1.exception.UserNotFoundException;
import com.example.exceptionhandlingservice1.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Heshan Karunaratne
 */
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User saveUser(UserRequest userRequest) {
        User user = User.
                build(0, userRequest.getName(), userRequest.getEmail(),
                        userRequest.getMobile(), userRequest.getGender(), userRequest.getAge(), userRequest.getNationality());
        return userRepository.save(user);
    }

    public List<User> getALlUsers() {
        return userRepository.findAll();
    }

    public User getUser(int id) throws UserNotFoundException {
        User user = userRepository.findByUserId(id);
        if (user == null) throw new UserNotFoundException("User not exist with id: " + id);

        return user;
    }

}
