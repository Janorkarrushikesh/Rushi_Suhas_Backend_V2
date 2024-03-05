package com.syntiaro_pos_system.controllerimpl.v2;

import com.syntiaro_pos_system.controller.v2.CountryController;
import com.syntiaro_pos_system.entity.v1.Country;
import com.syntiaro_pos_system.entity.v2.ApiResponse;
import com.syntiaro_pos_system.service.v2.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CountryControllerImplV2 implements CountryController {

    @Autowired
    CountryService countryService;

    @Override
    public ResponseEntity<ApiResponse> saveCountryAndState(List<Country> country) {
        return countryService.saveCountryAndState(country);
    }


}
