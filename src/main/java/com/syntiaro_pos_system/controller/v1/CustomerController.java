package com.syntiaro_pos_system.controller.v1;

import com.itextpdf.text.DocumentException;
import com.syntiaro_pos_system.entity.v1.CustomerDetails;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/v1/sys/customer")
public interface CustomerController {


    @PostMapping("/savecustomer")
    public ResponseEntity<?> addCustomer(@RequestBody CustomerDetails customerDetails);


    @GetMapping("/getcustomer/{storeId}")
    public ResponseEntity<List<CustomerDetails>> getCustomerByStoreId(@PathVariable Integer storeId);

    @PostMapping("/generate-pdf-customer/")
    public ResponseEntity<?> generatePDF(
            @RequestParam Integer store_id,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") String startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") String endDate) throws DocumentException;


    @PostMapping("/generate-pdf-customer/{store_id}")
    public ResponseEntity<?> generatePDFbyStoreid(
            @PathVariable Integer store_id) throws DocumentException;


}
