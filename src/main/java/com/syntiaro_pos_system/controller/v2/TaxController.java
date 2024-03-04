package com.syntiaro_pos_system.controller.v2;


import com.syntiaro_pos_system.entity.v2.ApiResponse;
import com.syntiaro_pos_system.entity.v1.Tax;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/v2/sys/tax")
public interface TaxController {
    @PostMapping("/")
    public ResponseEntity<ApiResponse> saveTax(@RequestBody Tax tax);

    @GetMapping("/{SerialNo}")
    public ResponseEntity<ApiResponse> getTaxById(@PathVariable Long SerialNo);

    @GetMapping("/store/{storeid}")
    public ResponseEntity<ApiResponse> getTaxByStoreId(@PathVariable String storeid);

    @PatchMapping("/id/{SerialNo}")
    public ResponseEntity<ApiResponse> updateTaxById(@PathVariable Long SerialNo, @RequestBody Tax tax);

    @DeleteMapping("/{SerialNo}")
    public ResponseEntity<ApiResponse> deletetaxById(@PathVariable Long SerialNo);

}
