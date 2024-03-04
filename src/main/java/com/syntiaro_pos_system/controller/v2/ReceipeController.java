package com.syntiaro_pos_system.controller.v2;

import com.syntiaro_pos_system.entity.v2.ApiResponse;
import com.syntiaro_pos_system.entity.v1.Receipe;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/v2/sys/receipe")
public interface ReceipeController {

    @PostMapping("/")
    public ResponseEntity<ApiResponse> saveFoodItem(@RequestBody Receipe receipe);


    @GetMapping("/{SerialNo}")
    public ResponseEntity<ApiResponse> getById(@PathVariable Long SerialNo);

    @GetMapping("/store/{storeId}")
    public ResponseEntity<ApiResponse> getByStoreId(@PathVariable String storeId) ;

    @PatchMapping("/id/{SerialNo}")
    public ResponseEntity<ApiResponse>updateFoodItem(@PathVariable Long SerialNo,
                                                     @RequestBody Receipe updatedFoodItem) ;
    @DeleteMapping("/{SerialNo}")
    public ResponseEntity<ApiResponse> deleteItemByid(@PathVariable Long SerialNo);

}
