package com.syntiaro_pos_system.controller.v2;

import com.syntiaro_pos_system.entity.v2.ApiResponse;
import com.syntiaro_pos_system.entity.v1.Vendor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(path = "/v2/sys/vendor")
public interface VendorController {

    @PostMapping(path = "/")
    public ResponseEntity<ApiResponse> saveVendor(@RequestBody Vendor vendor);

    @GetMapping("/id/{serialNo}")
    public ResponseEntity<ApiResponse> getVendorById(@PathVariable Long serialNo);

    @GetMapping("/Store/{storeId}")
    public ResponseEntity<ApiResponse> getByStoreId(@PathVariable Integer storeId , @RequestParam Integer page , @RequestParam Integer size);

    @PatchMapping("/id/{serialNo}")
    public ResponseEntity<ApiResponse> updateById(@PathVariable Long serialNo , @RequestBody Vendor vendor);

    @DeleteMapping("/{serialNo}")
    public ResponseEntity<ApiResponse> deleteVendorById(@PathVariable Long serialNo);
}
