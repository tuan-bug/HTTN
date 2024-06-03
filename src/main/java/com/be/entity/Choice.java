package com.be.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "choice")
public class Choice {
    @Id
    private String id;
    @Column(name = "question_id")
    private String questionId;
    private String content;
    private boolean rightAnswer;
}
