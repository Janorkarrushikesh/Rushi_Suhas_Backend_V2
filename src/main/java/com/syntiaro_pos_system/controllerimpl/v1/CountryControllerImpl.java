package com.syntiaro_pos_system.controllerimpl.v1;

import com.syntiaro_pos_system.controller.v1.CountryController;
import com.syntiaro_pos_system.entity.v1.Country;
import com.syntiaro_pos_system.entity.v1.State;
import com.syntiaro_pos_system.repository.v1.CountryRepository;
import com.syntiaro_pos_system.response.CountryResponse;
import com.syntiaro_pos_system.response.StateResponse;
import com.syntiaro_pos_system.serviceimpl.v1.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class CountryControllerImpl implements CountryController {
    private final CountryRepository countryRepository;

    @Autowired
    CountryService countryService;
    @Autowired
    public CountryControllerImpl(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @Override
    public List<Country> getAllCountries() {
        return countryRepository.findAll();
    }

    @Override
    public List<Object> getAllCountryNames() {
        List<Country> countries = countryRepository.findAll();
        return countries.stream().map(Country::getCountryName).collect(Collectors.toList());
    }

    // THIS METHOD IS USE FOR GET COUNTRY BY ID
    @Override
    public ResponseEntity<Country> getCountryById(@PathVariable Long id) {
        Optional<Country> country = countryRepository.findById(id);
        return country.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // THIS METHOD IS USE FOR GIVE INFO COUNTRY WISE
    @Override
    public ResponseEntity<CountryResponse> getCountryInfo(@PathVariable String countryName) {
        Country countryInfo = countryService.getCountryInfo(countryName);
        if (countryInfo != null) {
            // Create a simplified CountryResponse object
            CountryResponse response = new CountryResponse();
            response.setCountry_id(countryInfo.getCountryId());
            response.setCurrency(countryInfo.getCurrency());
            response.setCountry(countryInfo.getCountry());
            response.setCountry_code(countryInfo.getCountryCode());
            response.setCurrency_code(countryInfo.getCurrencyCode());
            response.setCurrency_symbol(countryInfo.getCurrencySymbol());

            // Create a list of StateResponse objects
            List<StateResponse> stateResponses = new ArrayList<>();
            for (State state : countryInfo.getStates()) {
                StateResponse stateResponse = new StateResponse();
                stateResponse.setState_id(state.getStateId());
                stateResponse.setState_name(state.getStateName());
                stateResponses.add(stateResponse);
            }
            response.setStates(stateResponses);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
