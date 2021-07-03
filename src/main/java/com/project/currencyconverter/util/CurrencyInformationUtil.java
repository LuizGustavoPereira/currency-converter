package com.project.currencyconverter.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.currencyconverter.model.CurrencyInformation;
import com.project.currencyconverter.repository.CurrencyInformationRepository;
import org.hibernate.PersistentObjectException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParseException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ServerErrorException;

import javax.annotation.PostConstruct;

@Component
public class CurrencyInformationUtil {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper mapper = new ObjectMapper();
    @Autowired
    private CurrencyInformationRepository currencyInformationRepository;

    @Scheduled(cron = "0 0 1 * * ?")
    @PostConstruct
    public void UpdateCurrencyInformation() {
        try {
            currencyInformationRepository.save(getCurrencyInformation());
        } catch (Exception e) {
            throw new PersistentObjectException("Error when trying to persist information, cause = " + e.getCause());
        }
    }

    private CurrencyInformation getCurrencyInformation() {
        try {
            return jsonToObject(restTemplate.getForEntity("http://data.fixer.io/api/latest?access_key=5dbfdfd415dcae0fd60f6a8f67297e86&base=EUR&symbols=USD,CAD,BRL", String.class).getBody());

        } catch (Exception e) {
            throw new ServerErrorException("Could not reach out to fixer API", e.getCause());
        }
    }

    private CurrencyInformation jsonToObject(String responseEntity) {
        try {
            return mapper.readValue(responseEntity, CurrencyInformation.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new JsonParseException();
        }
    }
}
