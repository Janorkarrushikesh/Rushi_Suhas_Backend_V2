package com.syntiaro_pos_system.service.v2;

import com.syntiaro_pos_system.entity.v1.Inventory;
import com.syntiaro_pos_system.entity.v2.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface InventoryService {


    ResponseEntity<ApiResponse> getStoreId(String storeId, Integer page, Integer size, String startDate, String endDate);

    ResponseEntity<ApiResponse> saveInventory(Inventory inventory);

    ResponseEntity<ApiResponse> getInventoryById(Integer SerialNo);

    ResponseEntity<ApiResponse> updateinventoryById(Integer serialNo, Inventory inventory);

    ResponseEntity<ApiResponse> getMessage(String storeId);

    ResponseEntity<ApiResponse> deleteById(Integer serialNo);
}
