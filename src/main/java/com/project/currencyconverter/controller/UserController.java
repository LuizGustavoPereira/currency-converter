package com.project.currencyconverter.controller;

import com.project.currencyconverter.model.User;
import com.project.currencyconverter.service.UserService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User is registered and the information are returned."),
            @ApiResponse(responseCode = "400", description = "User already exists."),
            @ApiResponse(responseCode = "500", description = "Intern error when trying to register the user."),
    })
    @PostMapping("/register")
    public User registerUser(@RequestBody User userRequest) {
        return userService.saveUser(userRequest);
    }

}
