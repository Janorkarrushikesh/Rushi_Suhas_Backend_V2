package com.syntiaro_pos_system.controller.v1;

import com.syntiaro_pos_system.entity.v1.Country;
import com.syntiaro_pos_system.response.CountryResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/v1/sys/countries")
public interface CountryController {


    @GetMapping("/countries")
    List<Country> getAllCountries();

    @GetMapping("/country-names")
    List<Object> getAllCountryNames();

    // THIS METHOD IS USE FOR GET COUNTRY BY ID
    @GetMapping("/{id}")
    ResponseEntity<Country> getCountryById(@PathVariable Long id);

    // THIS METHOD IS USE FOR GIVE INFO COUNTRY WISE
    @GetMapping("/info/{countryName}")
    ResponseEntity<CountryResponse> getCountryInfo(@PathVariable String countryName);

}
