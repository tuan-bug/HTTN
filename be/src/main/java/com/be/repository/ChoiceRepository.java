package com.be.repository;

import com.be.entity.Choice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChoiceRepository extends JpaRepository<Choice, String> {
    List<Choice> findAllByQuestionId(String questionId);
    List<Choice> findByQuestionIdAndRightAnswerTrue(String questionId);
}
