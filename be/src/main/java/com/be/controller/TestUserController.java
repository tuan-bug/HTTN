package com.be.controller;

import com.be.entity.Test;
import com.be.entity.TestUser;
import com.be.entity.Users;
import com.be.repository.TestRepository;
import com.be.repository.TestUserRepository;
import com.be.repository.UserRepository;
import com.be.service.TestUserService;
import com.be.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/testUser")
@RequiredArgsConstructor
public class TestUserController {
    private final TestUserService testUserService;
    private final UserService userService;
    private final TestRepository repositoryTest;
    private final UserRepository userRepository;
    @PostMapping("/addUserToTheTest/{testId}")
    public ResponseEntity<?> addUserToTheTest(@PathVariable String testId, @RequestBody Users[] users) {
        return new ResponseEntity<>(testUserService.addUserToTheTest(testId, users), HttpStatus.OK);
    }

    @GetMapping("/getUserEnrolledTest/{testId}")
    public ResponseEntity<?> getUserEnrolledTest(@PathVariable String testId) {
        return new ResponseEntity<>(testUserService.getUsersFromTheTest(testId), HttpStatus.OK);
    }

    @GetMapping("/getTestByUser/{userId}")
    public ResponseEntity<?> getTestByUser(@PathVariable String userId) {
        Users user = userService.getUserById(userId);
        List<TestUser> lstTestUser = testUserService.findAllByUser(userId);
        return new ResponseEntity<>(lstTestUser, HttpStatus.OK);
    }
    @GetMapping("/getTestUserByTestIdAndUserId/{testId}/{userId}")
    public ResponseEntity<?> getUserByTestIdAndUserId(@PathVariable String testId, @PathVariable String userId) {
        Test test = repositoryTest.findById(testId).orElse(null);
        Users user = userRepository.findById(userId).orElse(null);
        if (test == null || user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Test or User not found");
        }
        TestUser testUser = testUserService.findTestUserByTestIdAndUserId(test,user);
        return new ResponseEntity<>(testUser, HttpStatus.OK);
    }
    @GetMapping("/getUserByTest/{testId}")
    public ResponseEntity<?> getUserByTestId(@PathVariable String testId) {
        List<TestUser> lstTestUser = testUserService.findAllByTestId(testId);
        return new ResponseEntity<>(lstTestUser, HttpStatus.OK);
    }
    @PutMapping("/record")
    public ResponseEntity<?> record(@RequestBody Map<String, Object> resultQuiz) {
        String testId = (String) resultQuiz.get("test_id");
        String userId = (String) resultQuiz.get("user_id");
        String result = (String) resultQuiz.get("result");
        int timeWork = (int) resultQuiz.get("time_work");
        int time = (int) timeWork;
        Test test = repositoryTest.findById(testId).orElse(null);
        Users user = userRepository.findById(userId).orElse(null);
        if (test == null || user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Test or User not found");
        }
        TestUser testUser = testUserService.findTestUserByTestIdAndUserId(test, user);
        if (testUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("TestUser not found");
        }
        testUser.setResult(result);
        testUser.setUser(user);
        testUser.setTimeWork(time);
        testUserService.saveResult(testUser);

        return ResponseEntity.ok(testUser);
    }

    @GetMapping("/exams")
    public ResponseEntity<Map<String, Long>> getExamCompletionStatistics() {
        Map<String, Long> statistics = userService.getExamCompletionStatistics();
        return ResponseEntity.ok(statistics);
    }
}
