package com.project.currencyconverter.exception;

public class NoCurrencyInformationException extends RuntimeException {
    public NoCurrencyInformationException(String message) {
        super(message);
    }
}
