package com.syntiaro_pos_system.service.v2;

import com.syntiaro_pos_system.entity.v1.CategoryButton;
import com.syntiaro_pos_system.entity.v2.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface CategoryBtnService {
    ResponseEntity<ApiResponse> getButtonByStoreId(String storeId);

    ResponseEntity<ApiResponse> getButtonById(Long id);

    ResponseEntity<ApiResponse> saveButton(CategoryButton categoryButton);

    ResponseEntity<ApiResponse> updatecategoryById(Long id, CategoryButton categoryButton);

    ResponseEntity<ApiResponse> deleteById(Long id);

}
