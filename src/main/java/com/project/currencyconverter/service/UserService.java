package com.project.currencyconverter.service;

import com.project.currencyconverter.model.User;
import com.project.currencyconverter.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class UserService {

    @Autowired
    UserRepository userRepository;

    @PostConstruct
    public void saveUser() {
        userRepository.saveAndFlush(buildUser());
    }

    public User getUser() {
        return userRepository.findAll().get(0);
    }

    public User buildUser(){
        return User
                .builder()
                .email("teste@teste.com")
                .name("User Test")
                .build();
    }
}
