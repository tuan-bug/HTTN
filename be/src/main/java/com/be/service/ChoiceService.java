package com.be.service;

import com.be.dto.ChoiceDto;
import com.be.entity.Choice;
import com.be.entity.Question;

import java.util.List;

public interface ChoiceService {

    List<Choice> getChoiceByQuestionId(String questionId);

    void saveChoice(String questionId, List<ChoiceDto> choiceDtos);

    public boolean checkAnswers(String questionId, List<String> selectedChoiceIds);

    void updateChoice(String questionId, List<Choice> Choice);

    void deleteChoicesByQuestionId(String questionId);
}
