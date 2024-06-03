package com.be.controller;

import com.be.entity.Examples;
import com.be.service.ServiceExample;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/example")
public class ExampleController {
    private final ServiceExample serviceExample;

    @GetMapping("/all")
    public ResponseEntity<?> getAll() {
        return new ResponseEntity<>(serviceExample.getExamples(), HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getAll(@PathVariable int id) {
        return new ResponseEntity<>(serviceExample.getExampleById(id), HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createNewExample(@RequestHeader("Authorization") String jwt,@RequestBody Examples examples) {
        String token = jwt.substring(7);
        return new ResponseEntity<>(serviceExample.saveExample(examples), HttpStatus.CREATED);
    }
}
