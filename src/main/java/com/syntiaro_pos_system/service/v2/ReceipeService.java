package com.syntiaro_pos_system.service.v2;

import com.syntiaro_pos_system.entity.v1.Receipe;
import com.syntiaro_pos_system.entity.v2.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface ReceipeService {
    public ResponseEntity<ApiResponse> savereceipe(Receipe receipe);

    public ResponseEntity<ApiResponse> getById(Long SerialNo);

    ResponseEntity<ApiResponse> getByStoreId(String storeId);

    ResponseEntity<ApiResponse> updatereceipe(Long serialNo, Receipe updatedFoodItem);

    ResponseEntity<ApiResponse> deleteItemById(Long serialNo);
}
