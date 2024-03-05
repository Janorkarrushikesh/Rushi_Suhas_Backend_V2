package com.syntiaro_pos_system.controller.v2;


import com.syntiaro_pos_system.entity.v1.Tax;
import com.syntiaro_pos_system.entity.v2.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/v2/sys/tax")
public interface TaxController {
    @PostMapping("/")
    ResponseEntity<ApiResponse> saveTax(@RequestBody Tax tax);

    @GetMapping("/{SerialNo}")
    ResponseEntity<ApiResponse> getTaxById(@PathVariable Long SerialNo);

    @GetMapping("/store/{storeid}")
    ResponseEntity<ApiResponse> getTaxByStoreId(@PathVariable String storeid);

    @PatchMapping("/id/{SerialNo}")
    ResponseEntity<ApiResponse> updateTaxById(@PathVariable Long SerialNo, @RequestBody Tax tax);

    @DeleteMapping("/{SerialNo}")
    ResponseEntity<ApiResponse> deletetaxById(@PathVariable Long SerialNo);

}
