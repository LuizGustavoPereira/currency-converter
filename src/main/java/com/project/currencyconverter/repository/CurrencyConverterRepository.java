package com.project.currencyconverter.repository;

import com.project.currencyconverter.model.TransactionInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyConverterRepository extends JpaRepository<TransactionInformation, Long> {
}
