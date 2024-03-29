package com.syntiaro_pos_system.controller.v2;


import com.syntiaro_pos_system.entity.v1.CustomerDetails;
import com.syntiaro_pos_system.entity.v2.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(path = "/v2/sys/customer/")
public interface CustomerController {

    @PostMapping("/")
    ResponseEntity<ApiResponse> saveCustomer(@RequestBody CustomerDetails customer);

    @GetMapping("/{SerialNo}")
    ResponseEntity<ApiResponse> getById(@PathVariable Integer SerialNo);

    @GetMapping("/store/{storeId}")
    ResponseEntity<ApiResponse> getByStoreId(@PathVariable Integer storeId, @RequestParam(required = false) Integer size,
                                             @RequestParam(required = false) Integer page,
                                             @RequestParam(required = false) String startDate,
                                             @RequestParam(required = false) String endDate);


}
