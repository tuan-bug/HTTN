package com.be.service.impl;

import com.be.entity.Test;
import com.be.repository.TestRepository;
import com.be.service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {
    private final TestRepository repositoryTest;
    @Override
    public List<Test> getAllTest() {
        return repositoryTest.findAll();
    }

    @Override
    public void saveTest(Test testSave) {
        repositoryTest.save(testSave);
    }

    @Override
    public Test findTestById(String testId) {
        return repositoryTest.findTestById(testId);
    }
}
