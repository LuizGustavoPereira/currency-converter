package com.project.currencyconverter.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CurrencyConverterController {

    @RequestMapping("/home")
    public String home() {
        return "EAI CUZÃO";
    }
}
