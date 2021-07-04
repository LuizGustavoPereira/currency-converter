package com.project.currencyconverter.repository;

import com.project.currencyconverter.model.ConversionInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CurrencyConverterRepository extends JpaRepository<ConversionInformation, Long> {

    @Query("SELECT c FROM ConversionInformation c where c.user.id = :userId")
    public List<ConversionInformation> getAllByUserId(Long userId);
}
