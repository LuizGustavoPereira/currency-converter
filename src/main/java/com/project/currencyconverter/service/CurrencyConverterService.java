package com.project.currencyconverter.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.currencyconverter.exception.InvalidCalculationException;
import com.project.currencyconverter.model.CurrencyInformation;
import com.project.currencyconverter.model.TransactionInformation;
import com.project.currencyconverter.repository.CurrencyConverterRepository;
import com.project.currencyconverter.util.CurrencyInformationUtil;
import com.project.currencyconverter.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CurrencyConverterService {

    @Autowired
    private CurrencyConverterRepository currencyConverterRepository;
    @Autowired
    private CurrencyInformationUtil currencyInformationUtil;
    @Autowired
    private UserUtil userUtil;


    public TransactionInformation performConversion(String currencyFrom, String currencyTo, Double amount) {
        final CurrencyInformation currencyInformation = currencyInformationUtil.getCurrencyInformation();
        Double taxRate = calculateTaxRate(getCurrencyTax(currencyInformation, currencyFrom), getCurrencyTax(currencyInformation, currencyTo));
        Double finalAmount = amount * taxRate;
        return currencyConverterRepository.saveAndFlush(buildTransactionInformation(currencyFrom, currencyTo, amount, taxRate, finalAmount));
    }

    private TransactionInformation buildTransactionInformation(String currencyFrom, String currencyTo, Double amount, Double taxRate, Double finalAmount) {
        return TransactionInformation
                .builder()
                .user(userUtil.getUser())
                .originValue(amount)
                .toCurrency(currencyTo)
                .fromCurrency(currencyFrom)
                .finalValue(finalAmount)
                .conversionTax(taxRate)
                .build();
    }

    private Double getCurrencyTax(CurrencyInformation currencyInformation, String currency) {
        ObjectMapper mapper = new ObjectMapper();

        Map<String, Double> currencyTax = mapper.convertValue(currencyInformation.getRates(), new TypeReference<Map<String, Double>>() {
        });
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
