package com.be.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "test")
public class Test {
    @Id
    private String id;
    private String name;
    private String count_question;
    private int time_work;
    private Date time_start;
    private Date time_end;
    private String user_created;
    private String user_updated;
    private Date date_created;
    private Date date_updated;
    private Boolean is_deleted;

    @OneToMany(mappedBy = "test", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TestUser> testUsers = new HashSet<>();
}
