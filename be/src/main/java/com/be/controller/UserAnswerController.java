package com.be.controller;

import com.be.entity.Test;
import com.be.entity.UserAnswer;
import com.be.service.TestService;
import com.be.service.UserAnswerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/userAnswer")
public class UserAnswerController {
    private final UserAnswerService userAnswerService;

    @PostMapping("/create")
    public ResponseEntity<String> saveLine(@Validated @RequestBody UserAnswer userAnswer, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>("{\"message\": \"Vui lòng nhập đủ thông tin\"}", HttpStatus.BAD_REQUEST);
        } else {
            Date currentDate = new Date();
            UserAnswer answer = new UserAnswer();
            answer.setId(UUID.randomUUID().toString().substring(0, 36));
            answer.setQuestionId(userAnswer.getQuestionId());
            answer.setUserId(userAnswer.getUserId());
            answer.setChoiceId(userAnswer.getChoiceId());
            answer.setDateCreated(currentDate);
            answer.setTestId(userAnswer.getTestId());
            userAnswerService.save(answer);
            return new ResponseEntity<>("{\"message\": \"Thêm câu trả lời thành công\"}", HttpStatus.OK);
        }
    }

    @GetMapping("/getUserAnswersByTestId/{testId}/{userId}")
    public ResponseEntity<?> getUserAnswersByTestId(@PathVariable("testId") String testId, @PathVariable("userId") String userId) {
        List<UserAnswer> lstAnswer = userAnswerService.getAnswerByTestIdAndUserId(testId, userId);
        return new ResponseEntity<>(lstAnswer, HttpStatus.OK);
    }
}
