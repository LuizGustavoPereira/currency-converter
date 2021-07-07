package com.project.currencyconverter.service;

import com.project.currencyconverter.exception.UserAlreadyExistException;
import com.project.currencyconverter.exception.UserNotFoundException;
import com.project.currencyconverter.model.User;
import com.project.currencyconverter.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static com.project.currencyconverter.util.ObjectMock.buildUser;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(JUnitPlatform.class)
public class UserServiceTest {

    @Mock
    UserRepository userRepository = mock(UserRepository.class);

    @InjectMocks
    UserService userService = new UserService(userRepository);

    @Test
    public void getUserTest() {
        when(userRepository.findByUserName(any())).thenReturn(buildUser());

        User user = userService.getUser("UserTest");

        assertEquals(user.getEmail(), "teste@teste.com");
        assertEquals(user.getUserName(), "UserTest");
    }

    @Test
    public void getUserTestFail() {
        when(userRepository.findByUserName(any())).thenReturn(null);

        assertThrows(UserNotFoundException.class, () -> userService.getUser("UserTest"));

    }

    @Test
    public void saveUserTest() {
        when(userRepository.saveAndFlush(any())).thenReturn(buildUser());

        User user = userService.saveUser(buildUser());

        assertEquals(user.getEmail(), "teste@teste.com");
        assertEquals(user.getUserName(), "UserTest");
    }

    @Test
    public void saveUserTestFail() {
        when(userRepository.saveAndFlush(any())).thenThrow(new UserAlreadyExistException("User already exists."));

        assertThrows(UserAlreadyExistException.class, () -> userService.saveUser(buildUser()));
    }
}


