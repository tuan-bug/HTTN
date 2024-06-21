package com.be.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@Entity
@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class Roles {
    @Id
    private String id;
    private String name;
    private Date dateCreated;
    private Date dateUpdated;
}
