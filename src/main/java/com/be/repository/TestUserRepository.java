package com.be.repository;

import com.be.entity.Test;
import com.be.entity.TestUser;
import com.be.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestUserRepository extends JpaRepository<TestUser, String> {
    boolean existsByUserAndTest(Users user, Test test);

    List<TestUser> getTestUserByTestId(String testId);

    TestUser findByTestAndUser(Test test, Users user);
    long countByResultIsNotNull();
    long countByResultIsNull();
}
