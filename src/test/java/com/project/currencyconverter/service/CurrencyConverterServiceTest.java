package com.project.currencyconverter.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.project.currencyconverter.exception.InvalidCalculationException;
import com.project.currencyconverter.exception.UserNotFoundException;
import com.project.currencyconverter.model.ConversionInformation;
import com.project.currencyconverter.model.CurrencyInformation;
import com.project.currencyconverter.repository.CurrencyConverterRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.shadow.com.univocity.parsers.conversions.Conversion;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

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


    public void setup() {
        when(currencyInformationService.getCurrencyInformation()).thenReturn(fileToObjectClass("currency.json", new TypeReference<CurrencyInformation>() {
        }));
    }

    @Test
    public void performValidConversion() {
        setup();
        ConversionInformation conversionInformation = converterService.calculateConversion("USD", "BRL", 5.00);

        assertEquals(conversionInformation.getConversionTax(), 5.058670880741676);
        assertEquals(conversionInformation.getFinalValue(), 25.29335440370838);
    }

    @Test
    public void performValidConversionForCurrencyEqualsBase() {
        setup();
        ConversionInformation conversionInformation = converterService.calculateConversion("EUR", "BRL", 5.00);

        assertEquals(conversionInformation.getConversionTax(), 6.002113);
        assertEquals(conversionInformation.getFinalValue(), 30.010565);

    }

    @Test
    public void performInvalidConversion() {
        setup();
        Exception exception = assertThrows(InvalidCalculationException.class, () -> converterService.performConversion("USD", "BRT", 5.00));
    }

    @Test
    public void getConversionByUser() {
        when(currencyConverterRepository.getAllByUserId(any())).thenReturn(buildListConversion(true));

        ConversionInformation conversionInformation = converterService.getConversionByUser(2l).get(0);

        assertEquals(conversionInformation.getId(), 1l);
    }

    @Test
    public void thorwExceptionWhenGettingConversionByUser() {
        when(currencyConverterRepository.getAllByUserId(any())).thenReturn(buildListConversion(false));

        Exception exception = assertThrows(UserNotFoundException.class, () -> converterService.getConversionByUser(3l));
    }

    private List<ConversionInformation> buildListConversion(boolean isValid) {
        if(isValid){
            return Arrays.asList(
                    ConversionInformation
                        .builder()
                        .id(1l)
                        .conversionTax(1.00)
                        .fromCurrency("EUR")
                        .toCurrency("BRL")
                        .finalValue(6.00)
                        .date(new Date())
                        .originValue(1.00)
                        .build()
            );
        }

        return Collections.emptyList();
    }

}
