package com.syntiaro_pos_system.controller.v2;

import com.syntiaro_pos_system.entity.v1.CategoryButton;
import com.syntiaro_pos_system.entity.v2.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(path = "/v2/sys/categorybutton")
public interface CategoryBtnController {

    @PostMapping("/")
    public ResponseEntity<ApiResponse> saveButton(@RequestBody CategoryButton button);

    @GetMapping("/store/{storeId}")
    public ResponseEntity<ApiResponse> getButtonByStoreId(@PathVariable String storeId);

    @GetMapping("/id/{id}")
    public ResponseEntity<ApiResponse> getButtonById(@PathVariable Long id);

    @PatchMapping("/id/{id}")
    public ResponseEntity<ApiResponse> updatecategoryById(@PathVariable Long id, @RequestBody CategoryButton categoryButton);

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteById(@PathVariable Long id);

}
