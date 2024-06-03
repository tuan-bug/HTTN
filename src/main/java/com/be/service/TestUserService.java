package com.be.service;

import com.be.entity.Test;
import com.be.entity.TestUser;
import com.be.entity.Users;

import java.util.List;
import java.util.Map;

public interface TestUserService {
    String addUserToTheTest(String testId, Users[] users);

    List<Users> getUsersFromTheTest(String testId);

    List<TestUser> findAllByUser(String userId);

    List<TestUser> findAllByTestId(String testId);

    TestUser findTestUserByTestIdAndUserId(Test test, Users user);

    void saveResult(TestUser testUser);
    Map<String, Long> getTestCompletionStatistics();
}
