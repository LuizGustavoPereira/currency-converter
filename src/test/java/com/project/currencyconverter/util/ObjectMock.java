package com.project.currencyconverter.util;

import com.project.currencyconverter.model.ConversionInformation;
import com.project.currencyconverter.model.CurrencyInformation;
import com.fasterxml.jackson.core.type.TypeReference;
import com.project.currencyconverter.model.User;

import java.util.Date;

import static com.project.currencyconverter.util.JsonUtil.fileToObjectClass;

public class ObjectMock {

    public static CurrencyInformation buildCurrencyInformation() {
        return fileToObjectClass("currency.json", new TypeReference<CurrencyInformation>(){});
    }

    public static ConversionInformation buildInformationResponse() {
        return ConversionInformation
                .builder()
                .id(1l)
                .conversionTax(5.00)
                .toCurrency("BRL")
                .fromCurrency("USD")
                .originValue(5.00)
                .date(new Date())
                .finalValue(25.00)
                .user(new User())
                .build();
    }

    public static User buildUser() {
        return User
                .builder()
                .id(1l)
                .email("teste@teste.com")
                .userName("UserTest")
                .build();
    }

}
