package com.syntiaro_pos_system.controllerimpl.v2;

import com.syntiaro_pos_system.controller.v2.TaxController;
import com.syntiaro_pos_system.entity.v2.ApiResponse;
import com.syntiaro_pos_system.entity.v1.Tax;
import com.syntiaro_pos_system.service.v2.TaxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TaxControllerV2Impl implements TaxController {

    @Autowired
    TaxService taxService;
    @Override
    public ResponseEntity<ApiResponse> saveTax(Tax tax) {
        return taxService.saveTax(tax);
    }

    @Override
    public ResponseEntity<ApiResponse> getTaxById(Long SerialNo) {
        return taxService.getTaxById(SerialNo);
    }

    @Override
    public ResponseEntity<ApiResponse> getTaxByStoreId(String storeid) {
        return taxService.getTaxByStoreId(storeid);
    }

    @Override
    public ResponseEntity<ApiResponse> updateTaxById(Long SerialNo, Tax tax) {
        return taxService.updateTaxById(SerialNo,tax);
    }

    @Override
    public ResponseEntity<ApiResponse> deletetaxById(Long SerialNo) {
        return taxService.deletetaxById(SerialNo);
    }

}
