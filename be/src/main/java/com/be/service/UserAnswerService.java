package com.be.service;

import com.be.entity.UserAnswer;

import java.util.List;

public interface UserAnswerService {
    void save(UserAnswer answer);

    List<UserAnswer> getAnswerByTestIdAndUserId(String testId, String userId);
}
