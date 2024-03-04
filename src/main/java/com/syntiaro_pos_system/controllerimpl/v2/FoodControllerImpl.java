package com.syntiaro_pos_system.controllerimpl.v2;

import com.syntiaro_pos_system.controller.v2.FoodController;
import com.syntiaro_pos_system.entity.v2.ApiResponse;
import com.syntiaro_pos_system.entity.v1.Food;
import com.syntiaro_pos_system.service.v2.FoodService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
public class FoodControllerImpl implements FoodController {


    @Autowired
    private FoodService foodService;

    @Override
    public ResponseEntity<ApiResponse> saveFood(@Valid Food food) throws IOException {
        try{

            return new ResponseEntity<>(foodService.saveFood(food), HttpStatus.CREATED);
        }
        catch (Exception e){
            return new ResponseEntity<>(foodService.saveFood(food),HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }

    @Override
    public ResponseEntity<ApiResponse> getAllFood(Food food) {
        return new ResponseEntity<>(foodService.getAllFood(food), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ApiResponse> getFoodById(Integer serialNumber) {
        return new ResponseEntity<>(foodService.getFoodById(serialNumber), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ApiResponse> getAllFoodByStoreId(String storeId) {
        return new ResponseEntity<>(foodService.getAllFoodByStoreId(storeId), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ApiResponse> updateByserialNumber(Integer serialNumber, Map<String, Object> food) {
        return new ResponseEntity<>(foodService.updateBySerialNumber(serialNumber, food), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ApiResponse> deleteBySerialNumber(Integer serialNumber) {
        return new ResponseEntity<>(foodService.deleteBySerialNumber(serialNumber), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ApiResponse> UploadExcelFile(String storeId, MultipartFile file) throws IOException {
        return new ResponseEntity<>(foodService.UploadExcelFile(storeId,file),HttpStatus.OK);
    }

    @Override
    public ResponseEntity<byte[]> downloadExcelfileByStoreId(String storeId) throws IOException {
        return foodService.downloadExcelfileByStoreId(storeId);
    }

    @Override
    public ResponseEntity<ApiResponse> getFoodsByStoreId(String storeId , Integer page, Integer size){
        return  foodService.getFoodsByStoreId(storeId ,  page ,size);
    }

    @Override
    public ResponseEntity<ApiResponse> addonlist(String storeId) {
        return foodService.addonlist(storeId);
    }


}
