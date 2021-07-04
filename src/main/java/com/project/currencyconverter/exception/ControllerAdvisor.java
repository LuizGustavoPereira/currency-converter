package com.project.currencyconverter.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;

import static java.lang.System.currentTimeMillis;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.ResponseEntity.status;

@ControllerAdvice
@Slf4j
public class ControllerAdvisor {

   @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<StandardError> userNotFound(UserNotFoundException e, HttpServletRequest request) {
        StandardError err = new StandardError(currentTimeMillis(), NOT_FOUND.value(), "User not found", e.getMessage(), neutralize(request.getRequestURI()));
        log.info("UserNotFoundException: {}", neutralize(e.getMessage()));
        return status(NOT_FOUND).body(err);
    }

    private String neutralize(String value) {
        if (value == null) {
            return null;
        } else {
            try {
                return URLEncoder.encode(value);
            } catch (Exception e) {
                return null;
            }
        }
    }
}
