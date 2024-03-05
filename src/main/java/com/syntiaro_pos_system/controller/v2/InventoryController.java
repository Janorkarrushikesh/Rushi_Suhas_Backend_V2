package com.syntiaro_pos_system.controller.v2;

import com.syntiaro_pos_system.entity.v1.Inventory;
import com.syntiaro_pos_system.entity.v2.ApiResponse;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(path = "/v2/sys/inventory")
public interface InventoryController {

    @GetMapping(path = "/store/{storeId}")
    ResponseEntity<ApiResponse> getStoreId(@PathVariable String storeId,
                                           @RequestParam(name = "page", required = false) Integer page,
                                           @RequestParam(name = "size", required = false) Integer size,
                                           @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") String startDate,
                                           @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") String endDate);


    @PostMapping(path = "/")
    ResponseEntity<ApiResponse> saveInventory(@RequestBody Inventory inventory);

    @GetMapping(path = "/id/{SerialNo}")
    ResponseEntity<ApiResponse> getInventoryById(@PathVariable Integer SerialNo);

    @PatchMapping("/id/{SerialNo}")
    ResponseEntity<ApiResponse> updateInventoryById(@PathVariable Integer SerialNo, @RequestBody Inventory inventory);

    @GetMapping("/inventoryMsg/{storeId}")
    ResponseEntity<ApiResponse> getMessage(@PathVariable String storeId);

    @DeleteMapping("/{SerialNo}")
    ResponseEntity<ApiResponse> deleteById(@PathVariable Integer SerialNo);
}
