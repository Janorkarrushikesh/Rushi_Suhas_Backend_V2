package com.syntiaro_pos_system.serviceimpl.v2;

import com.syntiaro_pos_system.entity.v1.Country;
import com.syntiaro_pos_system.entity.v2.ApiResponse;
import com.syntiaro_pos_system.repository.v2.CountryRepositoryV2;
import com.syntiaro_pos_system.service.v2.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CountryServiceImpl implements CountryService {

    @Autowired
    CountryRepositoryV2 countryRepositoryV2;
    @Override
    public ResponseEntity<ApiResponse> saveCountryAndState(List<Country> countries) {
        try {
            List<String> countryCodes = countries.stream().map(Country::getCountryCode).collect(Collectors.toList());
            List<Country> existingCountries = countryRepositoryV2.findByCountryCodeIn(countryCodes);
            if (existingCountries.isEmpty()) {
                countryRepositoryV2.saveAll(countries);
                return ResponseEntity.ok().body( new ApiResponse(countries, true, 200));
            } else {
                return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body(new ApiResponse(null, false, "Countries already exist", 208));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(null, false, "...", 500));
        }
    }

}
