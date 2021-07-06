package com.project.currencyconverter.util;

import com.project.currencyconverter.model.CurrencyInformation;
import com.fasterxml.jackson.core.type.TypeReference;

import static com.project.currencyconverter.util.JsonUtil.fileToObjectClass;

public class ObjectMock {

    public static CurrencyInformation buildCurrencyInformation() {
        return fileToObjectClass("currency.json", new TypeReference<CurrencyInformation>(){});
    }

}
