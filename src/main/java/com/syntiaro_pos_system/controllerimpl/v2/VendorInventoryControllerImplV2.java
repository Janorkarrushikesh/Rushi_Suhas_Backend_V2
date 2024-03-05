package com.syntiaro_pos_system.controllerimpl.v2;

import com.syntiaro_pos_system.controller.v2.VendorInventoryController;
import com.syntiaro_pos_system.entity.v1.VendorInventory;
import com.syntiaro_pos_system.entity.v2.ApiResponse;
import com.syntiaro_pos_system.service.v2.VendorInventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VendorInventoryControllerImplV2 implements VendorInventoryController {

    @Autowired
    VendorInventoryService vendorInventoryService;

    @Override
    public ResponseEntity<ApiResponse> saveInvoice(VendorInventory vendorInventory) {
        return vendorInventoryService.saveInvoice(vendorInventory);
    }

    @Override
    public ResponseEntity<ApiResponse> getInvoiceById(Integer SerialNo) {
        return new ResponseEntity<>(vendorInventoryService.getInvoiceById(SerialNo), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ApiResponse> getInvoiceByStoreId(Integer storeId) {
        return new ResponseEntity<>(vendorInventoryService.getInvoiceByStoreId(storeId), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ApiResponse> updateInvoice(Integer SerialNo, VendorInventory vendorInventory) {
        return vendorInventoryService.updateInvoice(SerialNo, vendorInventory);
    }

    @Override
    public ResponseEntity<ApiResponse> deleteInvoiceById(Integer SerialNo) {
        return vendorInventoryService.deleteInvoiceByid(SerialNo);
    }

    @Override
    public ResponseEntity<ApiResponse> getByStore(Integer storeId, Integer page, Integer size, String startDate, String endDate) {
        return vendorInventoryService.getStore(storeId, page, size, startDate, endDate);
    }


}
