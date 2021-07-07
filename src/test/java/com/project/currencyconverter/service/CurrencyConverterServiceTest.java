package com.project.currencyconverter.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.currencyconverter.exception.InvalidCalculationException;
import com.project.currencyconverter.exception.UserNotFoundException;
import com.project.currencyconverter.model.ConversionInformation;
import com.project.currencyconverter.repository.CurrencyConverterRepository;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static com.project.currencyconverter.util.ObjectMock.buildCurrencyInformation;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(JUnitPlatform.class)
public class CurrencyConverterServiceTest {


    private final ObjectMapper mapper = new ObjectMapper();
    @Mock
    private final CurrencyConverterRepository currencyConverterRepository = mock(CurrencyConverterRepository.class);
    @Mock
    private final CurrencyInformationService currencyInformationService = mock(CurrencyInformationService.class);
    @Mock
    private final UserService userService = mock(UserService.class);

    private final CurrencyConverterService converterService = new CurrencyConverterService(currencyConverterRepository, currencyInformationService, userService, mapper);

    public void setup() {
        when(currencyInformationService.getCurrencyInformation()).thenReturn(buildCurrencyInformation());
    }

    @Test
    void performValidConversion() {
        setup();
        ConversionInformation conversionInformation = converterService.calculateConversion("USD", "BRL", 5.00, "userTest");

        assertEquals(conversionInformation.getFromCurrency(), "USD");
        assertEquals(conversionInformation.getToCurrency(), "BRL");
        assertEquals(conversionInformation.getOriginValue(), 5.00);
        assertEquals(conversionInformation.getConversionTax(), 5.058670880741676);
        assertEquals(conversionInformation.getFinalValue(), 25.29335440370838);
    }

    @Test
    void performValidConversionForCurrencyEqualsBase() {
        setup();
        ConversionInformation conversionInformation = converterService.calculateConversion("EUR", "BRL", 5.00, "userTest");

        assertEquals(conversionInformation.getConversionTax(), 6.002113);
        assertEquals(conversionInformation.getFinalValue(), 30.010565);

    }

    @Test
    void performInvalidConversion() {
        setup();
        Exception exception = assertThrows(InvalidCalculationException.class, () -> converterService.performConversion("USD", "BRT", 5.00, "userTest"));
    }

    @Test
    void getConversionByUser() {
        when(currencyConverterRepository.getAllByUserName(any())).thenReturn(buildListConversion(true));

        ConversionInformation conversionInformation = converterService.getConversionByUser("userTest").get(0);

        assertEquals(conversionInformation.getId(), 1l);
    }

    @Test
    void throwExceptionWhenGettingConversionByUser() {
        when(currencyConverterRepository.getAllByUserName(any())).thenReturn(buildListConversion(false));

        Exception exception = assertThrows(UserNotFoundException.class, () -> converterService.getConversionByUser("userTest"));
    }

    private List<ConversionInformation> buildListConversion(boolean isValid) {
        if (isValid) {
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
