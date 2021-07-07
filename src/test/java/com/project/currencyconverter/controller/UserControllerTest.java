package com.project.currencyconverter.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.currencyconverter.exception.UserAlreadyExistException;
import com.project.currencyconverter.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.util.NestedServletException;

import static com.project.currencyconverter.util.ObjectMock.buildUser;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(JUnitPlatform.class)
public class UserControllerTest {

    @Mock
    UserService userService = mock(UserService.class);

    @InjectMocks
    UserController userController = new UserController(userService);

    MockMvc mockMvc;


    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }


    @Test
    public void registerUserTest() throws Exception {
        when(userService.saveUser(any())).thenReturn(buildUser());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/user/register")
                .content(new ObjectMapper().writeValueAsString(buildUser()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk());
    }

    @Test
    public void existentUserTest() {
        when(userService.saveUser(any())).thenThrow(new UserAlreadyExistException("User already exists."));

        assertThrows(NestedServletException.class, () -> mockMvc.perform(MockMvcRequestBuilders.post("/api/user/register")
                .content(new ObjectMapper().writeValueAsString(buildUser()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk()));
    }

}
