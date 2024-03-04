package com.syntiaro_pos_system.controller.v2;

import com.syntiaro_pos_system.entity.v1.Country;
import com.syntiaro_pos_system.entity.v2.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/v2/sys/country")
public interface CountryController {

    @PostMapping(path = "/")
    public ResponseEntity<ApiResponse> saveCountryAndState(@RequestBody List<Country> country);

}
