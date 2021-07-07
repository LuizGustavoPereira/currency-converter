package com.project.currencyconverter.service;

import com.project.currencyconverter.exception.NoCurrencyInformationException;
import com.project.currencyconverter.model.CurrencyInformation;
import com.project.currencyconverter.repository.CurrencyInformationRepository;
import org.hibernate.PersistentObjectException;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static com.project.currencyconverter.util.ObjectMock.buildCurrencyInformation;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(JUnitPlatform.class)
public class CurrencyInformationTest {

    @Mock
    private final CurrencyInformationRepository currencyInformationRepository = mock(CurrencyInformationRepository.class);

    @InjectMocks
    private final CurrencyInformationService currencyInformationService = new CurrencyInformationService(currencyInformationRepository);


    @Test
    public void updateCurrencyInformationSuccessTest() throws NoSuchMethodException {
        when(currencyInformationRepository.saveAndFlush(any())).thenReturn(buildCurrencyInformation());

        currencyInformationService.updateCurrencyInformation();

        verify(currencyInformationRepository,times(1)).saveAndFlush(any());
    }

    @Test
    public void updateCurrencyInformationFailTest() {
        when(currencyInformationRepository.saveAndFlush(any())).thenThrow(new PersistentObjectException("Sorry! Could not find any currency information"));

        assertThrows(PersistentObjectException.class, () -> currencyInformationService.updateCurrencyInformation());

    }

    @Test
    public void getCurrencyInformationSuccessTest() {
        when(currencyInformationRepository.findByBase(any())).thenReturn(buildCurrencyInformation());

        CurrencyInformation currencyInformation = currencyInformationService.getCurrencyInformation();

        assertEquals("EUR", currencyInformation.getBase());
        assertEquals(null, currencyInformation.getErrorString());
        assertEquals(null, currencyInformation.getError());
        assertNotEquals(null, currencyInformation.getRatesString());
        assertNotEquals(null, currencyInformation.getRates());
    }

    @Test
    public void getCurrencyInformationFailTest() {
        when(currencyInformationRepository.findByBase(any())).thenReturn(null);

        assertThrows(NoCurrencyInformationException.class, () -> currencyInformationService.getCurrencyInformation());
    }




}
