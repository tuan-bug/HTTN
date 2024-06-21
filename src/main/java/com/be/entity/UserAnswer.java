package com.be.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_answer")
public class UserAnswer {
    @Id
    private String id;
    String questionId;
    String userId;
    String choiceId;
    Date dateCreated;
    String testId;
}
