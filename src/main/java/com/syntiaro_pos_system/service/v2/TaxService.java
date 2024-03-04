package com.syntiaro_pos_system.service.v2;

import com.syntiaro_pos_system.entity.v2.ApiResponse;
import com.syntiaro_pos_system.entity.v1.Tax;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public interface TaxService {
   public ApiResponse saveTax(Tax tax);


    public ResponseEntity<ApiResponse>getTaxById(Long serialNo);

    ResponseEntity<ApiResponse> getTaxByStoreId(String storeid);

    ResponseEntity<ApiResponse> updateTaxById(Long serialNo, Tax tax);

    ResponseEntity<ApiResponse> deletetaxById(Long serialNo);
}
