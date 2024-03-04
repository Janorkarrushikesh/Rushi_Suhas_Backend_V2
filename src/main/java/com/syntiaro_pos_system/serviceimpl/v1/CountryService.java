package com.syntiaro_pos_system.serviceimpl.v1;

import com.syntiaro_pos_system.entity.v1.Country;
import com.syntiaro_pos_system.repository.v1.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CountryService {

    private final CountryRepository countryRepository;

    @Autowired
    public CountryService (CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    public Country getCountryInfo(String countryName) {
        return countryRepository.findByCountry(countryName);
    }
}

