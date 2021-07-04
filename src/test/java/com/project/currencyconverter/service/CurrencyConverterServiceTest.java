package com.project.currencyconverter.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.project.currencyconverter.exception.InvalidCalculationException;
import com.project.currencyconverter.model.CurrencyInformation;
import com.project.currencyconverter.model.TransactionInformation;
import com.project.currencyconverter.model.User;
import com.project.currencyconverter.repository.CurrencyConverterRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.project.currencyconverter.util.JsonUtil.fileToObjectClass;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

// TODO make tests more real
@ExtendWith(MockitoExtension.class)
public class CurrencyConverterServiceTest {

    @InjectMocks
    CurrencyConverterService converterService = new CurrencyConverterService();
    @Mock
    private CurrencyConverterRepository currencyConverterRepository;
    @Mock
    private CurrencyInformationService currencyInformationService;
    @Mock
    private UserService userService;

    @BeforeEach
    public void beforeEach(){
        when(currencyInformationService.getCurrencyInformation()).thenReturn(fileToObjectClass("currency.json", new TypeReference<CurrencyInformation>() {
        }));
    }

    @Test
    public void performValidConversion() {
        when(currencyConverterRepository.saveAndFlush(any())).thenReturn(buildTransaction());

        TransactionInformation transactionInformation = converterService.performConversion("USD", "BRL", new Double("5.00"));

        assertEquals(transactionInformation.getFinalValue(), new Double("25.29335440370838"));
    }

    @Test
    public void performValidConversionForCurrencyEqualsBase() {
        when(currencyConverterRepository.saveAndFlush(any())).thenReturn(buildTransaction());

        TransactionInformation transactionInformation = converterService.performConversion("EUR", "BRL", new Double("5.00"));

        assertEquals(transactionInformation.getFinalValue(), new Double("25.29335440370838"));
    }

    @Test
    public void performInvalidConversion() {
        Exception exception  = assertThrows(InvalidCalculationException.class, () -> converterService.performConversion("USD", "BRT", new Double("5.00")));
    }

    public TransactionInformation buildTransaction() {
        return TransactionInformation
                .builder()
                .user(new User())
                .originValue(new Double("5.00"))
                .toCurrency("BRL")
                .fromCurrency("USD")
                .finalValue(new Double ("25.29335440370838"))
                .conversionTax(new Double("5.02"))
                .build();
    }
}
