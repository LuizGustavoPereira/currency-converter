package com.project.currencyconverter.service;

import com.project.currencyconverter.model.User;
import com.project.currencyconverter.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User saveUser(User userRequest) {
        return userRepository.saveAndFlush(userRequest);
    }

    public User getUser(String userName) {
        return userRepository.findByUserName(userName);
    }
}
