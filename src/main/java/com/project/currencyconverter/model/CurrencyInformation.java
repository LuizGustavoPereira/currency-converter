package com.project.currencyconverter.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Transient;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class CurrencyInformation {
    @Id
    @GeneratedValue
    private Long id;
    @Transient
    private boolean success;
    @Transient
    private long timestamp;
    private String base;
    private Date date;
    @Lob
    private String ratesString;
    private String errorString;

    @Transient
    private JsonNode rates;

    @Transient
    private JsonNode error;


    public JsonNode getRates() {
        if (this.rates == null && this.ratesString != null) {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode value = null;
            try {
                value = mapper.readTree(this.ratesString);
                this.rates = value;
            } catch (JsonProcessingException e) {
                throw new IllegalArgumentException("Rates is not valid json", e);
            }
        }
        return this.rates;
    }

    public void setRates(JsonNode rates) {
        this.rates = rates;
        ObjectMapper mapper = new ObjectMapper();
        try {
            ratesString = mapper.writeValueAsString(rates);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Can  not serialize rates", e);
        }
    }

    public JsonNode getError() {
        if (this.error == null && this.errorString != null) {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode value = null;
            try {
                value = mapper.readTree(this.errorString);
                this.error = value;
            } catch (JsonProcessingException e) {
                throw new IllegalArgumentException("Rates is not valid json", e);
            }
        }
        return this.error;
    }

    public void setError(JsonNode error) {
        this.error = error;
        ObjectMapper mapper = new ObjectMapper();
        try {
            this.errorString = mapper.writeValueAsString(error);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Can  not serialize rates", e);
        }
    }
}
