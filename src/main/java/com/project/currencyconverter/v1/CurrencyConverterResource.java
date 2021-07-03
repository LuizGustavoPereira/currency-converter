package com.project.currencyconverter.v1;

import com.project.currencyconverter.model.TransactionInformation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

public interface CurrencyConverterResource {

    @GetMapping(value = "/findTransactionsByUserId/{userId}")
    List<TransactionInformation> getTransactionByUser(@PathVariable Long userId);

    @PostMapping(value = "/convertCurrency/{currencyFrom}/{currencyTo}")
    TransactionInformation postTransaction(@PathVariable String currencyFrom, @PathVariable String currencyTo);

}
