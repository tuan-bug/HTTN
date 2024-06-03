package com.be.service.impl;

import com.be.dto.ChoiceDto;
import com.be.entity.Choice;
import com.be.entity.TestUser;
import com.be.repository.ChoiceRepository;
import com.be.repository.QuestionRepository;
import com.be.service.ChoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChoiceServiceImpl implements ChoiceService {
    private final ChoiceRepository choiceRepository;
    @Override
    public List<Choice> getChoiceByQuestionId(String questionId) {
        return choiceRepository.findAllByQuestionId(questionId);
    }

    @Override
    public void saveChoice(String questionId, List<ChoiceDto> choiceDtos) {
        for (ChoiceDto choiceDto : choiceDtos) {
            Choice choice = new Choice();
            String id = UUID.randomUUID().toString().substring(0, 36);
            choice.setId(id);
            choice.setContent(choiceDto.getContent());
            choice.setRightAnswer(choiceDto.isRightAnswer());
            choice.setQuestionId(questionId);
            choiceRepository.save(choice);
        }
    }

    public boolean checkAnswers(String questionId, List<String> selectedChoiceIds) {
        List<String> correctChoiceIds = choiceRepository.findByQuestionIdAndRightAnswerTrue(questionId)
                .stream()
                .map(Choice::getId)
                .collect(Collectors.toList());

        return selectedChoiceIds.containsAll(correctChoiceIds) && correctChoiceIds.containsAll(selectedChoiceIds);
    }

    @Override
    public void updateChoice(String questionId, List<Choice> choices) {
        // Lấy danh sách các Choice hiện có của câu hỏi
        List<Choice> existingChoices = choiceRepository.findAllByQuestionId(questionId);

        // Map các Choice hiện có bằng id để dễ dàng tìm kiếm
        Map<String, Choice> existingChoicesMap = existingChoices.stream()
                .collect(Collectors.toMap(Choice::getId, choice -> choice));

        // Tập hợp các ID của Choice mới
        List<String> newChoiceIds = choices.stream()
                .map(Choice::getId)
                .collect(Collectors.toList());

        for (Choice newChoice : choices) {
            if (existingChoicesMap.containsKey(newChoice.getId())) {
                // Nếu Choice mới nằm trong danh sách các Choice hiện có, cập nhật dữ liệu
                Choice existingChoice = existingChoicesMap.get(newChoice.getId());
                existingChoice.setContent(newChoice.getContent());
                existingChoice.setRightAnswer(newChoice.isRightAnswer());
                // Update Choice
                choiceRepository.save(existingChoice);
            } else {
                // Nếu Choice mới không nằm trong danh sách các Choice hiện có, thêm mới
                newChoice.setQuestionId(questionId);
                choiceRepository.save(newChoice);
            }
        }

        // Duyệt qua danh sách các Choice hiện có để xóa các Choice không nằm trong danh sách các Choice mới
        for (Choice existingChoice : existingChoices) {
            if (!newChoiceIds.contains(existingChoice.getId())) {
                choiceRepository.delete(existingChoice);
            }
        }
    }
    @Override
    public void deleteChoicesByQuestionId(String questionId) {
        // Lấy danh sách các lựa chọn theo questionId
        List<Choice> choices = choiceRepository.findAllByQuestionId(questionId);
        // Xóa các lựa chọn trong danh sách
        choiceRepository.deleteAll(choices);
    }

}
