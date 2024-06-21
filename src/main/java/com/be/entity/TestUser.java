package com.be.entity;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.catalina.User;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "test_user")
public class TestUser {
    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "test_id", referencedColumnName = "id")
    private Test test;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private Users user;

    private String result;
    private int timeWork;

}