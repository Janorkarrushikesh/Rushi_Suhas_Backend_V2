package com.syntiaro_pos_system.service.v2;

import com.syntiaro_pos_system.entity.v1.Country;
import com.syntiaro_pos_system.entity.v2.ApiResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CountryService {
    ResponseEntity<ApiResponse> saveCountryAndState(List<Country> country);
}
