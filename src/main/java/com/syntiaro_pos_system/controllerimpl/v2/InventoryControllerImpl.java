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


@RestController
public class InventoryControllerImpl implements InventoryController {

    @Autowired
    InventoryService inventoryService;

    @Override
    public ResponseEntity<ApiResponse> getStoreId(String storeId , Integer page,Integer size) {
        return inventoryService.getStoreId(storeId,page,size);
    }

    @Override
    public ResponseEntity<ApiResponse> saveInventory(Inventory inventory) {
        try{
            return new ResponseEntity<>(new ApiResponse(inventoryService.saveInventory(inventory),true,200),HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(new ApiResponse(inventoryService.saveInventory(inventory),false,HttpStatus.BAD_REQUEST.toString(),400),HttpStatus.BAD_REQUEST);
        }

    }

    @Override
    public ResponseEntity<ApiResponse> getInventoryById(Integer SerialNo) {
        try {
            ApiResponse response = inventoryService.getInventoryById(SerialNo);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse(null, false, "Error occurred", 400), HttpStatus.BAD_REQUEST);
        }
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
