package com.project.currencyconverter.controller;

import com.project.currencyconverter.model.ConversionInformation;
import com.project.currencyconverter.service.CurrencyConverterService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(path = "api", produces = MediaType.APPLICATION_JSON_VALUE)
public class CurrencyConverterController {

    private final CurrencyConverterService currencyConverterService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All conversions done by this user are returned"),
            @ApiResponse(responseCode = "404", description = "User not found."),
            @ApiResponse(responseCode = "500", description = "Internal error when trying to find conversions."),
    })
    @GetMapping(value = "/findTransactionsByUserName/{userName}")
    public ResponseEntity<List<ConversionInformation>> getConversionsByUser(@PathVariable String userName) {
        return ResponseEntity.ok(currencyConverterService.getConversionByUser(userName));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Conversion is done and information are returned."),
            @ApiResponse(responseCode = "404", description = "Currency not found"),
            @ApiResponse(responseCode = "500", description = "Internal error when trying to do the conversion"),
    })
    @PostMapping (value = "/convertCurrency/{currencyFrom}/{currencyTo}/{amount}")
    public ResponseEntity<ConversionInformation> postConversion(@PathVariable String currencyFrom, @PathVariable String currencyTo, @PathVariable Double amount, @RequestHeader String userName) {
        return ResponseEntity.ok(currencyConverterService.performConversion(currencyFrom, currencyTo, amount, userName));
    }
}
