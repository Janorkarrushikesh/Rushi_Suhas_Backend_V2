package com.syntiaro_pos_system.controller.v2;

import com.syntiaro_pos_system.entity.v1.VendorInventory;
import com.syntiaro_pos_system.entity.v2.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(path = "/v2/sys/invoice")
public interface VendorInventoryController {

    @PostMapping("/")
    ResponseEntity<ApiResponse> saveInvoice(@RequestBody VendorInventory vendorInventory);

    @GetMapping("/id/{SerialNo}")
    ResponseEntity<ApiResponse> getInvoiceById(@PathVariable Integer SerialNo);

    @GetMapping("/store/{storeId}")
    ResponseEntity<ApiResponse> getInvoiceByStoreId(@PathVariable Integer storeId);

    @PatchMapping("/id/{SerialNo}")
    ResponseEntity<ApiResponse> updateInvoice(@PathVariable Integer SerialNo, @RequestBody VendorInventory vendorInventory);

    @DeleteMapping("/{SerialNo}")
    ResponseEntity<ApiResponse> deleteInvoiceById(@PathVariable Integer SerialNo);

    @GetMapping("storePage/{storeId}")
    ResponseEntity<ApiResponse> getByStore(@PathVariable Integer storeId,
                                           @RequestParam(required = false) Integer page,
                                           @RequestParam(required = false) Integer size,
                                           @RequestParam(required = false) String startDate,
                                           @RequestParam(required = false) String endDate);


}
