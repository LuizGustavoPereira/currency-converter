package com.project.currencyconverter.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.project.currencyconverter.exception.NoCurrencyInformationException;
import com.project.currencyconverter.model.CurrencyInformation;
import com.project.currencyconverter.repository.CurrencyInformationRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.PersistentObjectException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ServerErrorException;

import javax.annotation.PostConstruct;

import static com.project.currencyconverter.util.JsonUtil.fileToObjectClass;
import static com.project.currencyconverter.util.JsonUtil.jsonToObject;

@Service
@Transactional
@Slf4j
@AllArgsConstructor
public class CurrencyInformationService {

    private final CurrencyInformationRepository currencyInformationRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    @Scheduled(cron = "0 0 1 * * ?")
    @PostConstruct
    public void updateCurrencyInformation() {
        try {
            log.info("Trying to update currency information.");
            currencyInformationRepository.saveAndFlush(getExternalInformation());
        } catch (Exception e) {
            throw new PersistentObjectException("Error when trying to persist information, cause = " + e.getCause());
        }
    }

    public CurrencyInformation getCurrencyInformation() {
        CurrencyInformation information = currencyInformationRepository.findByBase("EUR");
        if (information == null) {
            throw new NoCurrencyInformationException("Sorry! Could not find any currency information");
        }
        return information;
    }

    private CurrencyInformation getExternalInformation() {
        try {
            CurrencyInformation currencyInformation =  jsonToObject(restTemplate.getForEntity("http://data.fixer.io/api/latest?access_key=5dbfdfd415dcae0fd60f6a8f67297e86&base=EUR", String.class).getBody());
            currencyInformation.setId(1L);
            return currencyInformation;
        } catch (Exception e) {
            throw new ServerErrorException("Could not reach out to fixer API", e.getCause());
        }
    }
}
