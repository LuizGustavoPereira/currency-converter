package com.project.currencyconverter.controller;

import com.project.currencyconverter.exception.InvalidCalculationException;
import com.project.currencyconverter.exception.UserNotFoundException;
import com.project.currencyconverter.service.CurrencyConverterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.util.NestedServletException;

import java.util.Arrays;

import static com.project.currencyconverter.util.ObjectMock.buildInformationResponse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
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

        mockMvc.perform(MockMvcRequestBuilders.get("/api/convertCurrency/{currencyFrom}/{currencyTo}/{amount}", "USD", "BRL", 5.00)
                .header("userName", "NameTest"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.finalValue").value(25.00))
                .andExpect(MockMvcResultMatchers.jsonPath("$.conversionTax").value(5.00));
    }

    @Test
    void performFailConversion() {
        when(currencyConverterService.performConversion(any(), any(), any(), any())).thenThrow(new InvalidCalculationException("Invalid calculation"));

        assertThrows(NestedServletException.class, () -> mockMvc.perform(MockMvcRequestBuilders.get("/api/convertCurrency/{currencyFrom}/{currencyTo}/{amount}", "USD", "BLR", 5.00)
                .header("userName", "NameTest"))
                .andDo(print()).andExpect(status().isBadRequest())
        );
    }

    @Test
    void getConversionsByUserFail() {
        when(currencyConverterService.getConversionByUser(any())).thenThrow(new UserNotFoundException("User not found"));

        assertThrows(NestedServletException.class, () -> mockMvc.perform(MockMvcRequestBuilders.get("/api/findTransactionsByUserId/{userName}", "userTest"))
                .andDo(print()).andExpect(status().isNotFound()));
    }

    @Test
    void getConversionsByUserSuccess() throws Exception {
        when(currencyConverterService.getConversionByUser(any())).thenReturn(Arrays.asList(buildInformationResponse()));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/findTransactionsByUserId/{userName}", "userTest"))
                .andDo(print()).andExpect(status().isOk());
    }

}
