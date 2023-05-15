package com.example.springdataredis.service;

import com.example.springdataredis.entity.User;
import com.example.springdataredis.repository.UserDao;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Heshan Karunaratne
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    @Override
    public boolean saveUser(User user) {
        return userDao.saveUser(user);
    }

    @Override
    public List<User> fetchAllUser() {
        return userDao.fetchAllUser();
    }

    @Override
    @Cacheable(key = "#id", value = "USER", unless = "#result.age>50")
    public User fetchUserById(Long id) {
        return userDao.fetchUserById(id);
    }

    @Override
    @CacheEvict(key = "#id", value = "USER")
    public boolean deleteUser(Long id) {
        return userDao.deleteUser(id);
    }

    @Override
    @CachePut(key = "#id", value = "USER")
    public boolean updateUser(User user, Long id) {
        return userDao.updateUser(user, id);
    }
}
