package com.be.repository;

import com.be.entity.Test;
import com.be.entity.TestUser;
import com.be.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestRepository extends JpaRepository<Test, String> {
    Test findTestById(String testId);
    @Query("SELECT tu FROM TestUser tu WHERE tu.user.id = :userId")
    List<TestUser> findAllByUserId(String userId);
}
