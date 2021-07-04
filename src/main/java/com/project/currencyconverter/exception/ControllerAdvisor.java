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
import static org.springframework.http.ResponseEntity.status;

@ControllerAdvice
@Slf4j
public class ControllerAdvisor {

    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<StandardError> invalidInput(InvalidInputException e, HttpServletRequest request) {
        StandardError err = new StandardError(currentTimeMillis(), BAD_REQUEST.value(), "Invalid Input", e.getMessage(), neutralize(request.getRequestURI()));
        log.info("InvalidInputException: {}", neutralize(e.getMessage()));
        return status(BAD_REQUEST).body(err);
    }

    @ExceptionHandler(InvalidCalculationException.class)
    public ResponseEntity<StandardError> invalidInput(InvalidCalculationException e, HttpServletRequest request) {
        StandardError err = new StandardError(currentTimeMillis(), INTERNAL_SERVER_ERROR.value(), "Invalid Calculation, please review the input", e.getMessage(), neutralize(request.getRequestURI()));
        log.info("InvalidCalculationException: {}", neutralize(e.getMessage()));
        return status(INTERNAL_SERVER_ERROR).body(err);
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
