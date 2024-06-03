package com.be.service.impl;

import com.be.entity.Test;
import com.be.entity.Users;
import com.be.repository.UserRepository;
import com.be.service.TestUserService;
import com.be.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final TestUserService testUserService;
    @Override
    public List<Users> getAllUser() {
        return userRepository.findAll();
    }

    @Override
    public List<Users> getAllUserActivated() {
        return userRepository.findAllByStatus(true);
    }

    @Override
    public Users getUserById(String userId) {
        return userRepository.getUsersById(userId);
    }

    @Override
    public void saveUser(Users userToUpdate) {
         userRepository.save(userToUpdate);
    }

    @Override
    public Users getUserByUserName(String user_name) {
        return userRepository.findByUsername(user_name);
    }

    @Override
    public Map<String, Long> getUserStatistics() {
        long approvedCount = userRepository.countByStatus(true);
        long unapprovedCount = userRepository.countByStatus(false);
        Map<String, Long> stats = new HashMap<>();
        stats.put("approved", approvedCount);
        stats.put("unapproved", unapprovedCount);
        return stats;
    }
    public Map<String, Long> getExamCompletionStatistics() {
        return testUserService.getTestCompletionStatistics(); // Use the TestUserService to get test statistics
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }
}
