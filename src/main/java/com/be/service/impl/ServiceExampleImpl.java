package com.be.service.impl;


import com.be.entity.Examples;
import com.be.repository.RepositoryExample;
import com.be.service.ServiceExample;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceExampleImpl implements ServiceExample {
    private final RepositoryExample repoExample;

    @Override
    public List<Examples> getExamples() {
        return repoExample.findAll();
    }

    @Override
    public Examples getExampleById(int id) {
        return repoExample.findById(id).orElse(null);
    }

    @Override
    public Examples saveExample(Examples example) {
        return repoExample.save(example);
    }
}
