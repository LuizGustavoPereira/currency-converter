package com.project.currencyconverter.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;

import java.io.IOException;
import java.net.URL;

public class JsonUtil {

    public static <T> T fileToObjectClass(String file, TypeReference<T> typeRef) {
        ObjectMapper mapper = new ObjectMapper();

        URL jsonResource = Resources.getResource(file);
        try {
            return mapper.readValue(jsonResource, typeRef);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
