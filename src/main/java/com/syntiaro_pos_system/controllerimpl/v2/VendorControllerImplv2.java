package com.syntiaro_pos_system.controllerimpl.v2;


import com.syntiaro_pos_system.controller.v2.VendorController;
import com.syntiaro_pos_system.entity.v2.ApiResponse;
import com.syntiaro_pos_system.entity.v1.Vendor;
import com.syntiaro_pos_system.service.v2.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VendorControllerImplv2 implements VendorController {

    @Autowired
    VendorService vendorService;
    @Override
    public ResponseEntity<ApiResponse> saveVendor(Vendor vendor) {
        return new ResponseEntity<>(vendorService.saveVendor(vendor), HttpStatus.OK);

    }

    @Override
    public ResponseEntity<ApiResponse> getVendorById(Long serialNo) {
        return new ResponseEntity<>(vendorService.getVendorById(serialNo),HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ApiResponse> getByStoreId(Integer storeId , Integer page , Integer size) {
        return vendorService.getByStoreId(storeId , page ,size);
    }

    @Override
    public ResponseEntity<ApiResponse> updateById(Long serialNo, Vendor vendor) {
        return vendorService.updateById(serialNo,vendor) ;
    }

    @Override
    public ResponseEntity<ApiResponse> deleteVendorById(Long serialNo) {
        return vendorService.deleteVendorById(serialNo);
    }
}

