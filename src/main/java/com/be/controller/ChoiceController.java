package com.be.controller;

import com.be.entity.Choice;
import com.be.entity.Question;
import com.be.service.ChoiceService;
import com.be.service.impl.QuestionServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/choice")
public class ChoiceController {
    private final ChoiceService choiceService;

    @GetMapping("/getChoiceByQuestionId/{questionId}")
    public ResponseEntity<?> getChoicesByQuestionId(@PathVariable("questionId") String questionId) {
        int countAnswers = 0;
        List<Choice> lstQuestion = choiceService.getChoiceByQuestionId(questionId);

        for (Choice choice : lstQuestion) {
            if (choice.isRightAnswer()) {
                countAnswers++;
            }
        }
        List<Choice> lstChoice = new ArrayList<>();
        for (Choice choice : lstQuestion) {
            Choice choiceShow = new Choice();
            choiceShow.setId(choice.getId());
            choiceShow.setContent(choice.getContent());
            choiceShow.setQuestionId(choice.getQuestionId());
            // Các trường khác bạn có thể thêm vào đây nếu cần

            lstChoice.add(choiceShow);
        }
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("choices", lstChoice);
        responseMap.put("countAnswers", countAnswers);
        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }

    @GetMapping("/getChoices/{questionId}")
    public ResponseEntity<?> getChoices(@PathVariable("questionId") String questionId) {
        List<Choice> lstQuestion = choiceService.getChoiceByQuestionId(questionId);

        return new ResponseEntity<>(lstQuestion, HttpStatus.OK);
    }

    @PostMapping("/check")
    public ResponseEntity<Map<String, Boolean>> checkAnswers(@RequestBody Map<String, Object> payload) {
        String questionId = (String) payload.get("question_id");
        List<String> selectedChoiceIds = (List<String>) payload.get("selected_choices");

        boolean isCorrect = choiceService.checkAnswers(questionId, selectedChoiceIds);

        return new ResponseEntity<>(Map.of("correct", isCorrect), HttpStatus.OK);
    }
}
