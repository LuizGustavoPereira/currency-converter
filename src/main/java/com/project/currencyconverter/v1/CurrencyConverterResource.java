package com.project.currencyconverter.v1;

import com.project.currencyconverter.model.TransactionInformation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

public interface CurrencyConverterResource {

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = ""),
            @ApiResponse(responseCode = "404", description = ""),
            @ApiResponse(responseCode = "500", description = ""),
    })
    @GetMapping(value = "/findTransactionsByUserId/{userId}")
    List<TransactionInformation> getTransactionByUser(@PathVariable Long userId);

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = ""),
            @ApiResponse(responseCode = "404", description = ""),
            @ApiResponse(responseCode = "500", description = ""),
    })
    @GetMapping(value = "/convertCurrency/{currencyFrom}/{currencyTo}/{amount}")
    TransactionInformation postTransaction(@PathVariable String currencyFrom, @PathVariable String currencyTo, @PathVariable Double amount);

}
