package com.project.currencyconverter.controller;

import com.project.currencyconverter.model.ConversionInformation;
import com.project.currencyconverter.service.CurrencyConverterService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class CurrencyConverterController {

    private final CurrencyConverterService currencyConverterService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = ""),
            @ApiResponse(responseCode = "404", description = ""),
            @ApiResponse(responseCode = "500", description = ""),
    })
    @GetMapping(value = "/findTransactionsByUserId/{userId}")
    public ResponseEntity<List<ConversionInformation>> getConversionsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(currencyConverterService.getConversionByUser(userId));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = ""),
            @ApiResponse(responseCode = "404", description = ""),
            @ApiResponse(responseCode = "500", description = ""),
    })
    @GetMapping(value = "/convertCurrency/{currencyFrom}/{currencyTo}/{amount}")
    public ResponseEntity<ConversionInformation> postConversion(@PathVariable String currencyFrom, @PathVariable String currencyTo, @PathVariable Double amount) {
        return ResponseEntity.ok(currencyConverterService.performConversion(currencyFrom, currencyTo, amount));
    }
}
