package com.syntiaro_pos_system.controllerimpl.v2;

import com.syntiaro_pos_system.controller.v2.FoodController;
import com.syntiaro_pos_system.entity.v1.Food;
import com.syntiaro_pos_system.entity.v2.ApiResponse;
import com.syntiaro_pos_system.service.v2.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Map;

@RestController
public class FoodControllerImpl implements FoodController {


    @Autowired
    private FoodService foodService;

    @Override
    public ResponseEntity<ApiResponse> saveFood(@Valid Food food) throws IOException {
        return foodService.saveFood(food);
    }

    @Override
    public ResponseEntity<ApiResponse> getAllFood(Food food) {
        return foodService.getAllFood(food);
    }

    @Override
    public ResponseEntity<ApiResponse> getFoodById(Integer serialNumber) {
        return foodService.getFoodById(serialNumber);
    }

    @Override
    public ResponseEntity<ApiResponse> getAllFoodByStoreId(String storeId) {
        return foodService.getAllFoodByStoreId(storeId);

    }

    @Override
    public ResponseEntity<ApiResponse> updateByserialNumber(Integer serialNumber, Map<String, Object> food) {
        return foodService.updateBySerialNumber(serialNumber, food);
    }

    @Override
    public ResponseEntity<ApiResponse> deleteBySerialNumber(Integer serialNumber) {
        return foodService.deleteBySerialNumber(serialNumber);
    }

    @Override
    public ResponseEntity<ApiResponse> UploadExcelFile(String storeId, MultipartFile file) throws IOException {
        return foodService.UploadExcelFile(storeId, file);
    }


    @Override
    public ResponseEntity<ApiResponse> downloadExcelfileByStoreId(String storeId) throws IOException {
        return foodService.downloadExcelfileByStoreId(storeId);
    }

    @Override
    public ResponseEntity<ApiResponse> getFoodsByStoreId(String storeId, Integer page, Integer size, String startDate, String endDate) {
        return foodService.getFoodsByStoreId(storeId, page, size, startDate, endDate);
    }

    @Override
    public ResponseEntity<ApiResponse> addonlist(String storeId) {
        return foodService.addonlist(storeId);
    }


}
