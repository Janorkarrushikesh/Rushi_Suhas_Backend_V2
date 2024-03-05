package com.syntiaro_pos_system.controller.v2;

import com.syntiaro_pos_system.entity.v1.Receipe;
import com.syntiaro_pos_system.entity.v2.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/v2/sys/receipe")
public interface ReceipeController {

    @PostMapping("/")
    ResponseEntity<ApiResponse> saveFoodItem(@RequestBody Receipe receipe);


    @GetMapping("/{SerialNo}")
    ResponseEntity<ApiResponse> getById(@PathVariable Long SerialNo);

    @GetMapping("/store/{storeId}")
    ResponseEntity<ApiResponse> getByStoreId(@PathVariable String storeId);

    @PatchMapping("/id/{SerialNo}")
    ResponseEntity<ApiResponse> updateFoodItem(@PathVariable Long SerialNo,
                                               @RequestBody Receipe updatedFoodItem);

    @DeleteMapping("/{SerialNo}")
    ResponseEntity<ApiResponse> deleteItemByid(@PathVariable Long SerialNo);

}
