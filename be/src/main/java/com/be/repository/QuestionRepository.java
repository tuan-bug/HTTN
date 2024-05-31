package com.be.repository;

import com.be.entity.Question;
import com.be.entity.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, String> {
    @Query(value = "select q from Question q where q.testId = ?1 order by q.date_updated desc")
    List<Question> findAllByTestId(String testId);
}
