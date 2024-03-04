package com.syntiaro_pos_system.controller.v2;

import com.syntiaro_pos_system.entity.v1.Invoice;
import com.syntiaro_pos_system.entity.v2.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(path = "/v2/sys/invoice")
public interface InvoiceController {

    @PostMapping("/")
    public ResponseEntity<ApiResponse> saveInvoice(@RequestBody Invoice invoice);

    @GetMapping("/id/{SerialNo}")
    public ResponseEntity<ApiResponse> getInvoiceById(@PathVariable Integer SerialNo );

    @GetMapping("/store/{storeId}")
    public ResponseEntity<ApiResponse> getInvoiceByStoreId(@PathVariable Integer storeId);

    @PatchMapping("/id/{SerialNo}")
    public ResponseEntity<ApiResponse> updateInvoice(@PathVariable Integer SerialNo,@RequestBody Invoice invoice);

    @DeleteMapping("/{SerialNo}")
    public ResponseEntity<ApiResponse> deleteInvoiceById(@PathVariable Integer SerialNo);


}
