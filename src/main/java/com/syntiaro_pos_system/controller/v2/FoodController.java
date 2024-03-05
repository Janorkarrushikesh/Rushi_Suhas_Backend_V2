package com.syntiaro_pos_system.controller.v2;

import com.syntiaro_pos_system.entity.v1.Food;
import com.syntiaro_pos_system.entity.v2.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(path = "/v2/sys/food")
public interface FoodController {


    @PostMapping(path = "/")
    ResponseEntity<ApiResponse> saveFood(@Valid @RequestBody Food food) throws IOException;

    @GetMapping(path = "/fetchAll")
    ResponseEntity<ApiResponse> getAllFood(Food food);

    @GetMapping(path = "/Id/{serialNumber}")
    ResponseEntity<ApiResponse> getFoodById(@PathVariable Integer serialNumber);

    @GetMapping(path = "/store_all/{storeId}")
    ResponseEntity<ApiResponse> getAllFoodByStoreId(@PathVariable String storeId);

    @PatchMapping(path = "/{serialNumber}")
    ResponseEntity<ApiResponse> updateByserialNumber(@PathVariable Integer serialNumber, @RequestBody Map<String, Object> food);

    @DeleteMapping(path = "/id/{serialNumber}")
    ResponseEntity<ApiResponse> deleteBySerialNumber(@PathVariable Integer serialNumber);

    @PostMapping("/uploadexcel")
    ResponseEntity<ApiResponse> UploadExcelFile(@RequestParam("storeId") String storeId, @RequestParam("File") MultipartFile file) throws IOException;

    @PostMapping("/downloadexcel/{storeId}")
    ResponseEntity<ApiResponse> downloadExcelfileByStoreId(@PathVariable String storeId) throws IOException;

    @GetMapping("/store/{storeId}")
    ResponseEntity<ApiResponse> getFoodsByStoreId(@PathVariable String storeId, @RequestParam(name = "page", required = false) Integer page, @RequestParam(name = "size", required = false) Integer size, @RequestParam(required = false) String startDate, @RequestParam(required = false) String endDate);


    @GetMapping("/addon/{storeId}")
    ResponseEntity<ApiResponse> addonlist(@PathVariable String storeId);
}
