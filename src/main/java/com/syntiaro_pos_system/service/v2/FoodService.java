package com.syntiaro_pos_system.service.v2;

import com.syntiaro_pos_system.entity.v2.ApiResponse;
import com.syntiaro_pos_system.entity.v1.Food;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;


public interface FoodService {
    ApiResponse saveFood(Food food) throws IOException;

    ApiResponse getAllFood(Food food);

    ApiResponse getFoodById(Integer serialNumber);

    ApiResponse getAllFoodByStoreId(String storeId);

    ApiResponse updateBySerialNumber(Integer serialNumber, Map<String, Object> food);

    ApiResponse deleteBySerialNumber(Integer serialNumber);

    ApiResponse UploadExcelFile(String storeId, MultipartFile file) throws IOException;

    ResponseEntity<byte[]> downloadExcelfileByStoreId(String storeId);

   ResponseEntity<ApiResponse> getFoodsByStoreId(String storeId , Integer page, Integer size);

    ResponseEntity<ApiResponse> addonlist(String storeId);
}
