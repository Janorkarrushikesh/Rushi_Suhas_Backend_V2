package com.syntiaro_pos_system.service.v2;

import com.syntiaro_pos_system.entity.v1.VendorInventory;
import com.syntiaro_pos_system.entity.v2.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface VendorInventoryService {

    public ApiResponse getInvoiceById(Integer SerialNo);

    ApiResponse getInvoiceByStoreId(Integer storeId);

    ResponseEntity<ApiResponse> saveInvoice(VendorInventory vendorInventory);

    public ResponseEntity<ApiResponse> updateInvoice(Integer SerialNo, VendorInventory vendorInventory);

    ResponseEntity<ApiResponse> deleteInvoiceByid(Integer serialNo);

    ResponseEntity<ApiResponse> getStore(Integer storeId, Integer page, Integer size, String startDate, String endDate);
}
