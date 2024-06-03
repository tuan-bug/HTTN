package com.be.repository;

import com.be.entity.UserAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserAnswerRepository extends JpaRepository<UserAnswer, String> {

    List<UserAnswer> findUserAnswerByTestIdAndUserId(String testId, String userId);

    List<UserAnswer> findUserAnswersByTestId(String testId);
}
