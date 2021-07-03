package com.project.currencyconverter.repository;

import com.project.currencyconverter.model.CurrencyInformation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyInformationRepository extends CrudRepository<CurrencyInformation, Long> {
}
