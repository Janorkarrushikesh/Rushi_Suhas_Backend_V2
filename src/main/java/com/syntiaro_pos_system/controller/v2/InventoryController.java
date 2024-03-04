package com.syntiaro_pos_system.controller.v2;

import com.syntiaro_pos_system.entity.v1.Inventory;
import com.syntiaro_pos_system.entity.v2.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(path = "/v2/sys/inventory")
public interface InventoryController {

    @GetMapping(path = "/store/{storeId}")
    public ResponseEntity<ApiResponse> getStoreId(@PathVariable String storeId ,
                                                  @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                  @RequestParam(name = "size", defaultValue = "10") Integer size);

    @PostMapping(path = "/")
    public ResponseEntity<ApiResponse>saveInventory(@RequestBody Inventory inventory);

    @GetMapping(path="/id/{SerialNo}")
    public ResponseEntity<ApiResponse> getInventoryById(@PathVariable Integer SerialNo );

    @PatchMapping("/id/{SerialNo}")
    public ResponseEntity<ApiResponse> updateInventoryById(@PathVariable Integer SerialNo,@RequestBody Inventory inventory);

    @GetMapping("/inventoryMsg/{storeId}")
    public ResponseEntity<ApiResponse> getMessage(@PathVariable String storeId);

    @DeleteMapping("/{SerialNo}")
    public ResponseEntity<ApiResponse> deleteById(@PathVariable Integer SerialNo);
}
