package com.project.currencyconverter.service;

import com.project.currencyconverter.model.User;
import com.project.currencyconverter.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;

    @BeforeEach
    public void beforeEach() {
        when(userRepository.findAll()).thenReturn(Arrays.asList(buildUser()));
    }

    @Test
    public void getUser() {
        User user = userService.getUser("UserTest");

        assertEquals(user.getEmail(), "teste@teste.com");
    }

    public static User buildUser() {
        return User
                .builder()
                .email("teste@teste.com")
                .userName("UserTest")
                .build();
    }
}
