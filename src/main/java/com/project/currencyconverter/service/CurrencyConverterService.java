package com.project.currencyconverter.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.currencyconverter.exception.InvalidCalculationException;
import com.project.currencyconverter.model.CurrencyInformation;
import com.project.currencyconverter.model.TransactionInformation;
import com.project.currencyconverter.repository.CurrencyConverterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
public class CurrencyConverterService {

    @Autowired
    private CurrencyConverterRepository currencyConverterRepository;
    @Autowired
    private CurrencyInformationService currencyInformationService;
    @Autowired
    private UserService userService;


    public TransactionInformation performConversion(String currencyFrom, String currencyTo, Double amount) {
        final CurrencyInformation currencyInformation = currencyInformationService.getCurrencyInformation();
        Double taxRate = calculateTaxRate(getCurrencyTax(currencyInformation, currencyFrom), getCurrencyTax(currencyInformation, currencyTo));
        Double finalAmount = amount * taxRate;
        return currencyConverterRepository.saveAndFlush(buildTransactionInformation(currencyFrom, currencyTo, amount, taxRate, finalAmount));
    }

    private TransactionInformation buildTransactionInformation(String currencyFrom, String currencyTo, Double amount, Double taxRate, Double finalAmount) {
        return TransactionInformation
                .builder()
                .user(userService.getUser())
                .fromCurrency(currencyFrom)
                .originValue(amount)
                .toCurrency(currencyTo)
                .finalValue(finalAmount)
                .conversionTax(taxRate)
                .date(new Date())
                .build();
    }

    private Double getCurrencyTax(CurrencyInformation currencyInformation, String currency) {
        ObjectMapper mapper = new ObjectMapper();
        if(currency.equals(currencyInformation.getBase())){
            return new Double("1.00");
        }
        Map<String, Double> currencyTax = mapper.convertValue(currencyInformation.getRates(), new TypeReference<Map<String, Double>>() {});
        return currencyTax.get(currency);

    }

    private Double calculateTaxRate(Double amountFrom, Double amountTo) {
        try {
            return amountTo / amountFrom;
        } catch (Exception e) {
            throw new InvalidCalculationException("Invalid calculation: " + amountTo + " / " + amountFrom);
        }
    }
}
