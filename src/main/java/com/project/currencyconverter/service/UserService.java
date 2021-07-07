package com.project.currencyconverter.service;

import com.project.currencyconverter.exception.UserAlreadyExistException;
import com.project.currencyconverter.exception.UserNotFoundException;
import com.project.currencyconverter.model.User;
import com.project.currencyconverter.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    public User saveUser(User userRequest) {
        try{
            return userRepository.saveAndFlush(userRequest);
        } catch (Exception e) {
            log.info("Error: user already exists {}", userRequest.getUserName());
            throw new UserAlreadyExistException("This user already exists.");
        }
    }

    public User getUser(String userName) {
        User user = userRepository.findByUserName(userName);
        if(user == null){
            log.info("User not found {}", userName);
            throw new UserNotFoundException("User not found.");
        }
        return userRepository.findByUserName(userName);
    }
}
