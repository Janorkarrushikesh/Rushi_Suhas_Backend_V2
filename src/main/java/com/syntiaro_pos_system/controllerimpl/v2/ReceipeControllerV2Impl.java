package com.syntiaro_pos_system.controllerimpl.v2;

import com.syntiaro_pos_system.controller.v2.ReceipeController;
import com.syntiaro_pos_system.entity.v1.Receipe;
import com.syntiaro_pos_system.entity.v2.ApiResponse;
import com.syntiaro_pos_system.service.v2.ReceipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReceipeControllerV2Impl implements ReceipeController {
    @Autowired
    ReceipeService receipeService;

    @Override
    public ResponseEntity<ApiResponse> saveFoodItem(Receipe receipe) {

        return receipeService.savereceipe(receipe);

    }

    @Override
    public ResponseEntity<ApiResponse> getById(Long SerialNo) {
        return receipeService.getById(SerialNo);
    }

    @Override
    public ResponseEntity<ApiResponse> getByStoreId(String storeId) {
        return receipeService.getByStoreId(storeId);
    }

    @Override
    public ResponseEntity<ApiResponse> updateFoodItem(Long SerialNo, Receipe updatedFoodItem) {
        return receipeService.updatereceipe(SerialNo, updatedFoodItem);
    }

    @Override
    public ResponseEntity<ApiResponse> deleteItemByid(Long SerialNo) {
        return receipeService.deleteItemById(SerialNo);

    }
}