package com.project.currencyconverter.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class User {

    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private Long id;
    private String name;
    private String email;
}
