package com.syntiaro_pos_system.controller.v2;

import com.syntiaro_pos_system.entity.v2.ApiResponse;
import com.syntiaro_pos_system.entity.v1.CustomerDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(path = "/v2/sys/customer/")
public interface CustomerController {

    @PostMapping("/")
    public ResponseEntity<ApiResponse> saveCustomer(@RequestBody CustomerDetails customer);

    @GetMapping("/{SerialNo}")
    public ResponseEntity<ApiResponse> getById(@PathVariable Integer SerialNo);

    @GetMapping("/store/{storeId}")
    public ResponseEntity<ApiResponse> getByStoreId(@PathVariable Integer storeId , @RequestParam Integer size , @RequestParam Integer page);


}
