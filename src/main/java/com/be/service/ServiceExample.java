package com.be.service;

import com.be.entity.Examples;

import java.util.List;

public interface ServiceExample {
    List<Examples> getExamples();

    Examples getExampleById(int id);

    Examples saveExample(Examples example);
}
