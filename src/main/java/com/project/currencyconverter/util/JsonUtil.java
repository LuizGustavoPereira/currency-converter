package com.project.currencyconverter.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import com.project.currencyconverter.model.CurrencyInformation;
import org.springframework.boot.json.JsonParseException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;

@Component
public class JsonUtil {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static <T> T fileToObjectClass(String file, TypeReference<T> typeRef) {
        URL jsonResource = Resources.getResource(file);
        try {
            return mapper.readValue(jsonResource, typeRef);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static CurrencyInformation jsonToObject(String responseEntity) {
        try {
            return mapper.readValue(responseEntity, CurrencyInformation.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new JsonParseException();
        }
    }
}
