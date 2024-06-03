package com.be.controller;

import com.be.dto.ChoiceDto;
import com.be.dto.QuestionAnswerDTO;
import com.be.entity.Choice;
import com.be.entity.Question;
import com.be.entity.Test;
import com.be.entity.Users;
import com.be.repository.ChoiceRepository;
import com.be.repository.QuestionRepository;
import com.be.service.ChoiceService;
import com.be.service.QuestionService;
import com.be.service.TestService;
import com.be.utils.UploadFile;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/question")
public class QuestionController {
    private final QuestionService questionService;
    private final ChoiceService choiceService;
    private final UploadFile uploadFile;
    private final QuestionRepository questionRepository;
    private  final ChoiceRepository choiceRepository;
    @PostMapping
    public ResponseEntity<?> saveQuestion(@RequestHeader("Authorization") String jwt,
                                          @RequestParam(value = "file", required = false) MultipartFile file,
                                          @RequestParam("name") String name,
                                          @RequestParam(value = "already_has_file", required = false) boolean alreadyHasFile,
                                          @RequestParam("test_id") String testId, @RequestParam("choices") String choicesJson) {
        //Xu li file trong TH cau hoi khong co anh
        String fileName = "";
        if (file != null && !file.isEmpty()) {
            fileName = uploadFile.uploadFile(file);
        }
        Question question = new Question(name, fileName, testId);
        questionService.createQuestion(jwt, question);

        // Parse the choices JSON string into a list of choice objects
        ObjectMapper objectMapper = new ObjectMapper();
        List<ChoiceDto> choices;
        try {
            choices = objectMapper.readValue(choicesJson, objectMapper.getTypeFactory().constructCollectionType(List.class, ChoiceDto.class));
            choiceService.saveChoice(question.getId(), choices);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Invalid choices format");
        }
        return ResponseEntity.ok("Question saved successfully");
    }

    @GetMapping("/getQuestionByTestId/{testId}/{count}")
    public ResponseEntity<?> getUserById(@PathVariable("testId") String testId, @PathVariable("count") Long count) {
        List<Question> lstQuestion = questionService.getQuestionByTestId(testId);

        // Shuffle the list to get random order
        Collections.shuffle(lstQuestion);
        int actualCount = Math.min(count.intValue(), lstQuestion.size());
        List<Question> randomQuestions = lstQuestion.subList(0, actualCount);

        return new ResponseEntity<>(randomQuestions, HttpStatus.OK);
    }

    @GetMapping("/getAllQuestions/{testId}")
    public ResponseEntity<?> getAllQuestions(@PathVariable String testId) {
        return new ResponseEntity<>(questionService.getQuestionByTestId(testId), HttpStatus.OK);
    }

    @PutMapping("/editQuestion/{questionId}")
    public ResponseEntity<?> editQuestion(@PathVariable String questionId, @RequestHeader("Authorization") String jwt,
                                          @RequestParam(value = "file", required = false) MultipartFile file,
                                          @RequestParam("name") String name,
                                          @RequestParam(value = "already_has_file", required = false) boolean alreadyHasFile,
                                          @RequestParam("choices") String choicesJson) {
        //Xu li file trong TH cau hoi khong co anh
        String fileName = "";
        if (file != null && !file.isEmpty()) {
            fileName = uploadFile.uploadFile(file);
        }
        Question question = new Question(name, fileName);
        questionService.updateQuestion(questionId, jwt, question, alreadyHasFile);

        // Parse the choices JSON string into a list of choice objects
        ObjectMapper objectMapper = new ObjectMapper();
        List<Choice> choices = new ArrayList<>();
        List<Choice> choicesWithId = new ArrayList<>();
        try {
            //Binding data
            List<JsonNode> choiceNodes = objectMapper.readValue(choicesJson, objectMapper.getTypeFactory().constructCollectionType(List.class, JsonNode.class));

            //Kiem tra xem ban ghi nao co id
            for (JsonNode node : choiceNodes) {
                Choice choice = new Choice();
                if (node.has("id")) {
                    choice.setId(node.get("id").asText());
                    choicesWithId.add(choice);  // Add choices with ID to a separate list
                } else {
                    //Neu ko co id thi se tao id de co the them vao List<Choice>
                    choice.setId(UUID.randomUUID().toString());
                }
                choice.setContent(node.get("content").asText());
                choice.setRightAnswer(node.get("rightAnswer").asBoolean());
                choice.setQuestionId(questionId);
                choices.add(choice);
            }
            choiceService.updateChoice(questionId, choices);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Invalid choices format");
        }
        return ResponseEntity.ok("Question saved successfully");
    }

    @GetMapping("/getQuestionById/{questionId}")
    public ResponseEntity<?> getQuestion(@PathVariable String questionId) {
        return new ResponseEntity<>(questionRepository.findById(questionId), HttpStatus.OK);
    }
    @GetMapping("/getCount")
    public ResponseEntity<?> getCount() {
        return new ResponseEntity<>(questionRepository.findAll(), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{questionId}")
    public ResponseEntity<?> deleteQuestion(@PathVariable String questionId) {
        try {
            // Kiểm tra xem câu hỏi có tồn tại không
            Optional<Question> optionalQuestion = questionRepository.findById(questionId);
            if (!optionalQuestion.isPresent()) {
                return ResponseEntity.notFound().build();
            }
            // Xóa câu trả lời của câu hỏi
            choiceService.deleteChoicesByQuestionId(questionId);
            // Xóa câu hỏi
            questionRepository.deleteById(questionId);
            return ResponseEntity.ok("Question deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting question: " + e.getMessage());
        }
    }

    @PostMapping("/import")
    public ResponseEntity<?> importQuestion(@RequestHeader("Authorization") String jwt, @Validated @RequestBody QuestionAnswerDTO questionAnswerDTO) {
        System.out.println( questionAnswerDTO);
        String testId = questionAnswerDTO.getTestId();
        String text = questionAnswerDTO.getText();
        String image = questionAnswerDTO.getImage();
        String fileName = "";
//        if (image != null && !image.isEmpty()) {
//            fileName = uploadFile.uploadFile(image);
//        }
        Question question = new Question(text, image, testId);
        questionService.createQuestion(jwt, question);

        List<String> choices = questionAnswerDTO.getChoices();
        List<Integer> answers = questionAnswerDTO.getAnswers();
        for (int i = 0; i < choices.size(); i++) {
            String item = choices.get(i);
            Choice choice;
            if (answers.contains(i + 1)) {
                // Nếu số thứ tự của lựa chọn hiện tại tồn tại trong danh sách câu trả lời
                choice = new Choice(UUID.randomUUID().toString().substring(0, 36), question.getId(), item, true);
            } else {
                // Nếu không, đặt rightAnswer của lựa chọn thành false
                choice = new Choice(UUID.randomUUID().toString().substring(0, 36), question.getId(), item, false);
            }
            choiceRepository.save(choice);
        }


        for (Integer answer : answers) {
            // Xử lý mỗi câu trả lời ở đây
            System.out.println("Answer: " + answer);
        }
        return ResponseEntity.ok("Question deleted successfully");
    }
}

