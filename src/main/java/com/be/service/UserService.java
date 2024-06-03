package com.be.service;

import com.be.entity.Test;
import com.be.entity.Users;

import java.util.List;
import java.util.Map;

public interface UserService {
    List<Users> getAllUser();

    List<Users> getAllUserActivated();

    Users getUserById(String userId);

    void saveUser(Users userToUpdate);

    Users getUserByUserName(String user_name);

    Map<String, Long> getUserStatistics();

    boolean existsByUsername(String username);

    Map<String, Long> getExamCompletionStatistics();
}
