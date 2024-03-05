package com.syntiaro_pos_system.service.v2;

import com.syntiaro_pos_system.entity.v1.Food;
import com.syntiaro_pos_system.entity.v2.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;


public interface FoodService {
    ResponseEntity<ApiResponse> saveFood(Food food) throws IOException;

    ResponseEntity<ApiResponse> getAllFood(Food food);

    ResponseEntity<ApiResponse> getFoodById(Integer serialNumber);

    ResponseEntity<ApiResponse> getAllFoodByStoreId(String storeId);

    ResponseEntity<ApiResponse> updateBySerialNumber(Integer serialNumber, Map<String, Object> food);

    ResponseEntity<ApiResponse> deleteBySerialNumber(Integer serialNumber);

    ResponseEntity<ApiResponse> UploadExcelFile(String storeId, MultipartFile file) throws IOException;

    ResponseEntity<ApiResponse> downloadExcelfileByStoreId(String storeId);

    ResponseEntity<ApiResponse> getFoodsByStoreId(String storeId, Integer page, Integer size, String startDate, String endDate);

    ResponseEntity<ApiResponse> addonlist(String storeId);
}
