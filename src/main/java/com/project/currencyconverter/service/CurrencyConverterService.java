package com.project.currencyconverter.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.currencyconverter.exception.InvalidCalculationException;
import com.project.currencyconverter.exception.UserNotFoundException;
import com.project.currencyconverter.model.ConversionInformation;
import com.project.currencyconverter.model.CurrencyInformation;
import com.project.currencyconverter.repository.CurrencyConverterRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class CurrencyConverterService {

    private final CurrencyConverterRepository currencyConverterRepository;
    private final CurrencyInformationService currencyInformationService;
    private final UserService userService;
    private final ObjectMapper mapper;


    public ConversionInformation performConversion(String currencyFrom, String currencyTo, Double amount) {
        return currencyConverterRepository.saveAndFlush(calculateConversion(currencyFrom, currencyTo, amount));
    }

    protected ConversionInformation calculateConversion(String currencyFrom, String currencyTo, Double amount) {
        final CurrencyInformation currencyInformation = currencyInformationService.getCurrencyInformation();
        Double taxRate = calculateTaxRate(getCurrencyTax(currencyInformation, currencyFrom), getCurrencyTax(currencyInformation, currencyTo));
        Double finalAmount = amount * taxRate;

        return buildTransactionInformation(currencyFrom, currencyTo, amount, taxRate, finalAmount);
    }

    private Double getCurrencyTax(CurrencyInformation currencyInformation, String currency) {
        if (currency.equals(currencyInformation.getBase())) {
            return 1.00;
        }
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

    public List<ConversionInformation> getConversionByUser(Long userId) {
        List<ConversionInformation> infoList = currencyConverterRepository.getAllByUserId(userId);
        if (infoList.isEmpty()) {
            throw new UserNotFoundException("Could not find user for Id: " + userId);
        }

        return infoList;
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
}
