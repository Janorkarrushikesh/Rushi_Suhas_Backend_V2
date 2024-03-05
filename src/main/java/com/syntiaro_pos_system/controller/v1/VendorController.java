package com.syntiaro_pos_system.controller.v1;

import com.itextpdf.text.DocumentException;
import com.syntiaro_pos_system.entity.v1.Vendor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/v1/sys/vendor")
@CrossOrigin(origins = "*", maxAge = 3600)
public interface VendorController {

    @PostMapping(path = "/save")
    String saveVendor(@RequestBody Vendor vendor);

    @GetMapping(path = "/allVendor")
    List<Vendor> getVendor();

    @PutMapping(path = "/updatevendor")
    Vendor updatevendor(@RequestBody Vendor vendor);

    @DeleteMapping(path = "/deletes/{Serial_no}")
    ResponseEntity<HttpStatus> deleteVendor(@PathVariable Long Serial_no);

    @PatchMapping(path = "/updatevendor/{vendor_id}")
    ResponseEntity<Vendor> updateVendors(
            @PathVariable("vendor_id") Integer vendor_id,
            @RequestBody Vendor vendor);

    @PostMapping("/getvendorlist/{store_id}")
    ResponseEntity<byte[]> generateExcelByStoreId(@PathVariable Integer store_id);

    @GetMapping("/vendor/{storeId}")
    List<Vendor> getVendorByStoreId(@PathVariable Integer storeId);

    @GetMapping("/{id}")
    ResponseEntity<Vendor> fetchDetailsById(@PathVariable Long id);

    @PostMapping("/excelvendor/")
    ResponseEntity<byte[]> generateExcelByStoreIdWithDateRange(
            @RequestParam Integer store_id,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") String startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") String endDate);

    @PostMapping("/generate-pdf-vendor/")
    ResponseEntity<?> generatePDF(
            @RequestParam Integer store_id,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") String startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") String endDate) throws DocumentException;

    @PostMapping("/generate-pdf-vendor/{store_id}")
    ResponseEntity<?> generatePDFByStoreid(
            @PathVariable Integer store_id) throws DocumentException;

    @PatchMapping("/vendors/{vendor_id}")
    ResponseEntity<?> updateVendor(@PathVariable("vendor_id") Long vendor_id, @RequestBody Vendor vendor);


}