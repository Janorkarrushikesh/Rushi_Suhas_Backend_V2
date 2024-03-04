package com.syntiaro_pos_system.controllerimpl.v2;

import com.syntiaro_pos_system.controller.v2.InventoryController;
import com.syntiaro_pos_system.entity.v1.Inventory;
import com.syntiaro_pos_system.entity.v2.ApiResponse;
import com.syntiaro_pos_system.service.v2.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.sql.In;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;


@RestController
public class InventoryControllerImpl implements InventoryController {

    @Autowired
    InventoryService inventoryService;

    @Override
    public ResponseEntity<ApiResponse> getStoreId(String storeId , Integer page,Integer size,String startDate,String endDate) {
        return inventoryService.getStoreId(storeId,page,size,startDate,endDate);
    }


    @Override
    public ResponseEntity<ApiResponse> saveInventory(Inventory inventory) {

        return inventoryService.saveInventory(inventory);

    }

    @Override
    public ResponseEntity<ApiResponse> getInventoryById(Integer SerialNo) {
        return   inventoryService.getInventoryById(SerialNo);
    }

    @Override
    public ResponseEntity<ApiResponse> updateInventoryById(Integer SerialNo, Inventory inventory) {
        return inventoryService.updateinventoryById(SerialNo,inventory);
    }

    @Override
    public ResponseEntity<ApiResponse> getMessage(String storeId) {
        return inventoryService.getMessage(storeId);
    }

    public ResponseEntity<ApiResponse> deleteById(Integer SerialNo){
        return inventoryService.deleteById(SerialNo);
    }


}
