package com.be.controller;


import com.be.config.jwt.JwtTokenProvider;
import com.be.entity.*;
import com.be.repository.QuestionRepository;
import com.be.repository.TestRepository;
import com.be.repository.TestUserRepository;
import com.be.repository.UserAnswerRepository;
import com.be.service.TestService;
import com.be.service.UserDetailService;
import com.be.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/test")
public class TestController {
    private final TestService testService;
    private final JwtTokenProvider jwtTokenProvider;
    private final TestUserRepository testUserRepository;
    private final UserService userService;
    private final TestRepository testRepository;
    private final QuestionRepository questionRepository;
    private final UserAnswerRepository userAnswerRepository;
    @GetMapping("/getUser")
    public ResponseEntity<?> getUserId(@RequestHeader("Authorization") String jwt) {
        String username = jwtTokenProvider.getUsernameFromJwt(jwt.substring(7));
        Users user = userService.getUserByUserName(username);
        return new ResponseEntity<>(user.getId(), HttpStatus.OK);
    }
    @GetMapping("/getUserById/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable("userId") String userId) {
        Users user = userService.getUserById(userId);
        String name_user = user.getFullName();
        return new ResponseEntity<>(name_user, HttpStatus.OK);
    }
    @GetMapping()
    public ResponseEntity<?> getAll() {
        List<Test> lstTest = testService.getAllTest();
        lstTest.sort(Comparator.comparing(Test::getDate_created).reversed());
        return new ResponseEntity<>(lstTest, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<String> saveLine(@Validated @RequestBody Test test, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>("{\"message\": \"Vui lòng nhập đủ thông tin\"}", HttpStatus.BAD_REQUEST);
        } else {
            Date currentDate = new Date();
            Test testSave = new Test();
            testSave.setId(UUID.randomUUID().toString().substring(0, 36));
            testSave.setName(test.getName());
            testSave.setCount_question(test.getCount_question());
            testSave.setTime_work(test.getTime_work());
            testSave.setTime_start(test.getTime_start());
            testSave.setTime_end(test.getTime_end());
            testSave.setUser_created(test.getUser_created());
            testSave.setUser_updated(test.getUser_updated());
            testSave.setDate_created(currentDate);
            testSave.setDate_updated(currentDate);
            testSave.setIs_deleted(false);
            testService.saveTest(testSave);
            return new ResponseEntity<>("{\"message\": \"Thêm bài thi thành công\"}", HttpStatus.OK);
        }
    }

    @GetMapping("/getTest/{testId}")
    public ResponseEntity<?> getTestById(@PathVariable("testId") String testId) {
        Test test = testService.findTestById(testId);
        if (test == null) {
            return new ResponseEntity<>("Test not found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(test, HttpStatus.OK);
    }

    @PutMapping("/edit/{testId}")
    public ResponseEntity<?> saveLine(@Validated @RequestBody Test test, BindingResult bindingResult, @PathVariable("testId") String testId) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>("{\"message\": \"Vui lòng nhập đủ thông tin\"}", HttpStatus.BAD_REQUEST);
        } else {
            Date currentDate = new Date();
            Test testSave = testService.findTestById(testId);
            System.out.println(testSave);
            testSave.setName(test.getName());
            testSave.setCount_question(test.getCount_question());
            testSave.setTime_work(test.getTime_work());
            testSave.setTime_start(test.getTime_start());
            testSave.setTime_end(test.getTime_end());
            testSave.setUser_created(test.getUser_created());
            testSave.setUser_updated(test.getUser_updated());
            testSave.setDate_created(currentDate);
            testSave.setDate_updated(currentDate);
            testSave.setIs_deleted(false);
            testService.saveTest(testSave);
            return new ResponseEntity<>("{\"message\": \"Sửa bài thi thành công\"}", HttpStatus.OK);
        }
    }


    @DeleteMapping("/deleteTest/{testId}")
    public ResponseEntity<?> deleteTestById(@PathVariable("testId") String testId) {
        Test test = testService.findTestById(testId);
        if (test == null) {
            return new ResponseEntity<>("Test not found", HttpStatus.NOT_FOUND);
        }
        List<TestUser> lstTestUser = testUserRepository.getTestUserByTestId(testId);
        if (lstTestUser != null && !lstTestUser.isEmpty()) {
            testUserRepository.deleteAll(lstTestUser);
        }

        List<Question> lstQuestion = questionRepository.findAllByTestId(testId);
        if (lstQuestion != null && !lstQuestion.isEmpty()){
            questionRepository.deleteAll(lstQuestion);
        }
        List<UserAnswer> lstUserAnswer = userAnswerRepository.findUserAnswersByTestId(testId);
        if (lstUserAnswer != null && !lstUserAnswer.isEmpty()){
            userAnswerRepository.deleteAll(lstUserAnswer);
        }
        testRepository.delete(test);
        return new ResponseEntity<>("{\"message\": \"Xóa bài thi thành công\"}", HttpStatus.OK);
    }
}
