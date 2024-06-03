package com.be.service.impl;

import com.be.config.jwt.JwtTokenProvider;
import com.be.entity.Question;
import com.be.entity.Users;
import com.be.repository.QuestionRepository;
import com.be.repository.TestRepository;
import com.be.service.QuestionService;
import com.be.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {
    private final QuestionRepository questionRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    @Override
    public List<Question> getQuestionByTestId(String testId) {
        return questionRepository.findAllByTestId(testId);
    }

    @Override
    public Question createQuestion(String jwt, Question question) {
        String username = jwtTokenProvider.getUsernameFromJwt(jwt.substring(7));
        Users user = userService.getUserByUserName(username);
        String id = UUID.randomUUID().toString().substring(0, 36);
        question.setId(id);
        question.setDate_created(new Date());
        question.setDate_updated(new Date());
        question.setUser_created(user.getId());
        question.setUser_updated(user.getId());
        return questionRepository.save(question);
    }

    @Override
    public Question updateQuestion(String questionId, String jwt, Question question, boolean alreadyHasFile) {
        String username = jwtTokenProvider.getUsernameFromJwt(jwt.substring(7));
        Users user = userService.getUserByUserName(username);

        Question existingQuestion = questionRepository.findById(questionId).get();
        if (existingQuestion == null) {
            throw new RuntimeException("Question not found");
        }
        existingQuestion.setName(question.getName());
        existingQuestion.setDate_updated(new Date());
        existingQuestion.setUser_updated(user.getId());
        if (alreadyHasFile) {
            existingQuestion.setFile(existingQuestion.getFile());
        } else {
            existingQuestion.setFile(question.getFile());
        }

        return questionRepository.save(existingQuestion);
    }

}
