package com.project.currencyconverter.service;

import com.project.currencyconverter.model.User;
import com.project.currencyconverter.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
@AllArgsConstructor
public class UserService {

    UserRepository userRepository;

    @PostConstruct
    public void saveUser() {
        userRepository.saveAndFlush(buildUser());
    }

    public User getUser() {
        return userRepository.findAll().get(0);
    }

    public User buildUser() {
        return User
                .builder()
                .email("teste@teste.com")
                .name("User Test")
                .build();
    }
}
