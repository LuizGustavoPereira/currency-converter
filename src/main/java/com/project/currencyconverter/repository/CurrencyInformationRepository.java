package com.project.currencyconverter.repository;

import com.project.currencyconverter.model.CurrencyInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyInformationRepository extends JpaRepository<CurrencyInformation, Long> {

    CurrencyInformation findByBase(String base);
}
