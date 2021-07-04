package com.project.currencyconverter.controller;

import com.project.currencyconverter.exception.InvalidCalculationException;
import com.project.currencyconverter.exception.UserNotFoundException;
import com.project.currencyconverter.service.CurrencyConverterService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CurrencyConverterControllerTest {

    @Mock
    private CurrencyConverterService currencyConverterService;

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @BeforeAll
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void performSuccessConversion() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/convertCurrency/{currencyFrom}/{currencyTo}/{amount}", "USD", "BRL", 5.00))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.finalValue").value(25))
                .andExpect(MockMvcResultMatchers.jsonPath("$.conversionTax").value(5));
    }

    @Test
    public void performFailConversion() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/convertCurrency/{currencyFrom}/{currencyTo}/{amount}", "USD", "BLR", 5.00))
                .andDo(print()).andExpect(status().isInternalServerError())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof InvalidCalculationException));
    }

    @Test
    public void getConversionsByUserFail() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/findTransactionsByUserId/{userId}", 5))
                .andDo(print()).andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof UserNotFoundException));
    }
}
