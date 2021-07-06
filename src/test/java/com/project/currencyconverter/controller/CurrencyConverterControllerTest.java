package com.project.currencyconverter.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.currencyconverter.exception.InvalidCalculationException;
import com.project.currencyconverter.exception.UserNotFoundException;
import com.project.currencyconverter.model.ConversionInformation;
import com.project.currencyconverter.model.CurrencyInformation;
import com.project.currencyconverter.repository.CurrencyConverterRepository;
import com.project.currencyconverter.service.CurrencyConverterService;
import com.project.currencyconverter.service.CurrencyInformationService;
import com.project.currencyconverter.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static com.project.currencyconverter.util.JsonUtil.fileToObjectClass;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(JUnitPlatform.class)
class CurrencyConverterControllerTest {

    @Mock
    private final CurrencyConverterService currencyConverterService = mock(CurrencyConverterService.class);

    private final CurrencyConverterController currencyConverterController = new CurrencyConverterController(currencyConverterService);

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(currencyConverterController).build();
    }

    @Test
    void performSuccessConversion() throws Exception {
        when(currencyConverterService.performConversion(any(), any(), any(), any())).thenReturn(buildInformationResponse());

        mockMvc.perform(MockMvcRequestBuilders.get("/convertCurrency/{currencyFrom}/{currencyTo}/{amount}", "USD", "BRL", 5.00))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.finalValue").value(25.00))
                .andExpect(MockMvcResultMatchers.jsonPath("$.conversionTax").value(5.00));
    }

    @Test
    void performFailConversion() throws Exception {
        when(currencyConverterService.performConversion(any(), any(), any(), any())).thenThrow(InvalidCalculationException.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/convertCurrency/{currencyFrom}/{currencyTo}/{amount}", "USD", "BLR", 5.00))
                .andDo(print()).andExpect(status().isInternalServerError())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof InvalidCalculationException));
    }

    @Test
    void getConversionsByUserFail() throws Exception {
        when(currencyConverterService.performConversion(any(), any(), any(), any())).thenThrow(UserNotFoundException.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/findTransactionsByUserId/{userId}", 5))
                .andDo(print()).andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof UserNotFoundException));
    }

    private ConversionInformation buildInformationResponse(){
        return ConversionInformation
                .builder()
                .finalValue(25.00)
                .conversionTax(5.00)
                .build();
    }
}
