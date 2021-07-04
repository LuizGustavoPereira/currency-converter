package com.project.currencyconverter.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.currencyconverter.exception.InvalidCalculationException;
import com.project.currencyconverter.exception.UserNotFoundException;
import com.project.currencyconverter.model.CurrencyInformation;
import com.project.currencyconverter.model.ConversionInformation;
import com.project.currencyconverter.repository.CurrencyConverterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class CurrencyConverterService {

    @Autowired
    private CurrencyConverterRepository currencyConverterRepository;
    @Autowired
    private CurrencyInformationService currencyInformationService;
    @Autowired
    private UserService userService;


    public ConversionInformation performConversion(String currencyFrom, String currencyTo, Double amount) {
        return currencyConverterRepository.saveAndFlush(calculateConversion(currencyFrom, currencyTo, amount));
    }

    protected ConversionInformation calculateConversion (String currencyFrom, String currencyTo, Double amount) {
        final CurrencyInformation currencyInformation = currencyInformationService.getCurrencyInformation();
        Double taxRate = calculateTaxRate(getCurrencyTax(currencyInformation, currencyFrom), getCurrencyTax(currencyInformation, currencyTo));
        Double finalAmount = amount * taxRate;

        return buildTransactionInformation(currencyFrom, currencyTo, amount, taxRate, finalAmount);
    }

    private ConversionInformation buildTransactionInformation(String currencyFrom, String currencyTo, Double amount, Double taxRate, Double finalAmount) {
        return ConversionInformation
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

    public List<ConversionInformation> getConversionByUser(Long userId) {
        List<ConversionInformation> infoList = currencyConverterRepository.getAllByUserId(userId);
        if (infoList.isEmpty()) {
            throw new UserNotFoundException("Could not find user for Id: " + userId);
        }

        return infoList;
    }
}
