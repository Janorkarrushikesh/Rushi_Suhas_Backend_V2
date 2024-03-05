package com.syntiaro_pos_system.controllerimpl.v2;

import com.syntiaro_pos_system.controller.v2.CategoryBtnController;
import com.syntiaro_pos_system.entity.v1.CategoryButton;
import com.syntiaro_pos_system.entity.v2.ApiResponse;
import com.syntiaro_pos_system.service.v2.CategoryBtnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CategoryBtnControllerImplV2 implements CategoryBtnController {

    @Autowired
    CategoryBtnService categoryBtnService;

    @Override
    public ResponseEntity<ApiResponse> getButtonByStoreId(String storeId) {
        return categoryBtnService.getButtonByStoreId(storeId);
    }

    @Override
    public ResponseEntity<ApiResponse> getButtonById(Long id) {
        return categoryBtnService.getButtonById(id);
    }

    @Override
    public ResponseEntity<ApiResponse> saveButton(CategoryButton button) {
        return categoryBtnService.saveButton(button);

    }

    @Override
    public ResponseEntity<ApiResponse> updatecategoryById(Long id, CategoryButton categoryButton) {
        return categoryBtnService.updatecategoryById(id, categoryButton);
    }

    @Override
    public ResponseEntity<ApiResponse> deleteById(Long id) {
        return categoryBtnService.deleteById(id);
    }

}
