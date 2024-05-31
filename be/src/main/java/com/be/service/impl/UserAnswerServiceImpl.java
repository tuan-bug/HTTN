package com.be.service.impl;

import com.be.entity.UserAnswer;
import com.be.repository.TestUserRepository;
import com.be.repository.UserAnswerRepository;
import com.be.service.UserAnswerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserAnswerServiceImpl implements UserAnswerService {
    private final UserAnswerRepository userAnswerRepository;
    @Override
    public void save(UserAnswer answer) {
        userAnswerRepository.save(answer);
    }

    @Override
    public List<UserAnswer> getAnswerByTestIdAndUserId(String testId, String userId) {
        return userAnswerRepository.findUserAnswerByTestIdAndUserId(testId,userId);
    }
}
