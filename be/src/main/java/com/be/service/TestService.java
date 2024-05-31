package com.be.service;

import com.be.entity.Test;

import java.util.List;

public interface TestService {
    List<Test> getAllTest();
    void saveTest(Test testSave);

    Test findTestById(String testId);
}
