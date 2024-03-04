package com.syntiaro_pos_system.service.v2;

import com.syntiaro_pos_system.entity.v1.Inventory;
import com.syntiaro_pos_system.entity.v2.ApiResponse;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface InventoryService {

    ResponseEntity<ApiResponse> getStoreId(String storeId ,Integer page , Integer size);

    Inventory saveInventory(Inventory inventory);

    ApiResponse getInventoryById(Integer SerialNo);

    ResponseEntity<ApiResponse> updateinventoryById(Integer serialNo, Inventory inventory);

    ResponseEntity<ApiResponse> getMessage(String storeId);

    ResponseEntity<ApiResponse> deleteById(Integer serialNo);
}
