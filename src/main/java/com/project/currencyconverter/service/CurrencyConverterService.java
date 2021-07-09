package com.project.currencyconverter.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.currencyconverter.exception.InvalidCalculationException;
import com.project.currencyconverter.exception.UserNotFoundException;
import com.project.currencyconverter.model.ConversionInformation;
import com.project.currencyconverter.model.CurrencyInformation;
import com.project.currencyconverter.repository.CurrencyConverterRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
@Slf4j
public class CurrencyConverterService {

    private final CurrencyConverterRepository currencyConverterRepository;
    private final CurrencyInformationService currencyInformationService;
    private final UserService userService;
    private final ObjectMapper mapper;


    public ConversionInformation performConversion(String currencyFrom, String currencyTo, Double amount, String userName) {
        log.info("Performing conversion: currencyFrom = {}, currencyTo = {}, amount = {}, userName = {}", currencyFrom, currencyTo, amount, userName);
        return currencyConverterRepository.saveAndFlush(calculateConversion(currencyFrom, currencyTo, amount, userName));
    }

    protected ConversionInformation calculateConversion(String currencyFrom, String currencyTo, Double amount, String userName) {
        final CurrencyInformation currencyInformation = currencyInformationService.getCurrencyInformation();
        Double taxRate = calculateTaxRate(getCurrencyTax(currencyInformation, currencyFrom), getCurrencyTax(currencyInformation, currencyTo));
        Double finalAmount = verifyAmount(amount) * taxRate;

        return buildTransactionInformation(currencyFrom, currencyTo, amount, taxRate, finalAmount, userName);
    }

    private Double getCurrencyTax(CurrencyInformation currencyInformation, String currency) {
        if (currency.equalsIgnoreCase(currencyInformation.getBase())) {
            return 1.00;
        }
        Map<String, Double> currencyTax = mapper.convertValue(currencyInformation.getRates(), new TypeReference<Map<String, Double>>() {
        });
        return currencyTax.get(currency);

    }

    private Double calculateTaxRate(Double taxFrom, Double taxTo) {
        try {
            return taxTo / taxFrom;
        } catch (Exception e) {
            log.info("Invalid calculation: {}/{}", taxTo, taxFrom);
            throw new InvalidCalculationException("Invalid calculation: " + taxTo + " / " + taxFrom);
        }
    }

    public List<ConversionInformation> getConversionByUser(String userName) {
        List<ConversionInformation> infoList = currencyConverterRepository.getAllByUserName(userName);
        if (infoList.isEmpty()) {
            throw new UserNotFoundException("Could not find user " + userName);
        }

        return infoList;
    }

    public Double verifyAmount(Double amount) {
        return amount <= 0.0 ? 0.0 : amount;
    }

    private ConversionInformation buildTransactionInformation(String currencyFrom, String currencyTo, Double amount, Double taxRate, Double finalAmount, String userName) {
        return ConversionInformation
                .builder()
                .user(userService.getUser(userName))
                .fromCurrency(currencyFrom)
                .originValue(amount)
                .toCurrency(currencyTo)
                .finalValue(finalAmount)
                .conversionTax(taxRate)
                .date(new Date())
                .build();
    }
}
