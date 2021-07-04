package com.project.currencyconverter.controller;

import com.project.currencyconverter.model.TransactionInformation;
import com.project.currencyconverter.service.CurrencyConverterService;
import com.project.currencyconverter.v1.CurrencyConverterResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CurrencyConverterController implements CurrencyConverterResource {

    @Autowired
    CurrencyConverterService currencyConverterService;

    @Override
    public List<TransactionInformation> getTransactionByUser(Long userId) {
        return null;
    }

    @Override
    public TransactionInformation postTransaction(String currencyFrom, String currencyTo, Double amount) {
        return currencyConverterService.performConversion(currencyFrom, currencyTo, amount);
    }
}
