package com.be.service;

import com.be.entity.Question;

import java.util.List;

public interface QuestionService {
    List<Question> getQuestionByTestId(String testId);

    Question createQuestion(String jwt, Question question);

    Question updateQuestion(String questionId, String jwt, Question question, boolean alreadyHasFile);
}
