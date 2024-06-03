package com.be.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "question")
public class Question {
    @Id
    private String id;
    @Column(name = "test_id")
    private String testId;
    private String name;
    private String file;
    private String user_created;
    private String user_updated;
    private Date date_created;
    private Date date_updated;
    private boolean is_deleted;

    public Question(String name, String file, String testId) {
        this.name = name;
        this.file = file;
        this.testId = testId;
    }

    public Question(String name, String file) {
        this.name = name;
        this.file = file;
    }
}
