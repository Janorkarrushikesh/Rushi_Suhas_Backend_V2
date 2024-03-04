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
    public String saveVendor(@RequestBody Vendor vendor);

    @GetMapping(path = "/allVendor")
    public List<Vendor> getVendor();

    @PutMapping(path = "/updatevendor")
    public Vendor updatevendor(@RequestBody Vendor vendor);

    @DeleteMapping(path = "/deletes/{Serial_no}")
    public ResponseEntity<HttpStatus> deleteVendor(@PathVariable Long Serial_no);

    @PatchMapping(path = "/updatevendor/{vendor_id}")
    public ResponseEntity<Vendor> updateVendors(
            @PathVariable("vendor_id") Integer vendor_id,
            @RequestBody Vendor vendor);

    @PostMapping("/getvendorlist/{store_id}")
    public ResponseEntity<byte[]> generateExcelByStoreId(@PathVariable Integer store_id);

    @GetMapping("/vendor/{storeId}")
    public List<Vendor> getVendorByStoreId(@PathVariable Integer storeId);

    @GetMapping("/{id}")
    public ResponseEntity<Vendor> fetchDetailsById(@PathVariable Long id) ;

    @PostMapping("/excelvendor/")
    public ResponseEntity<byte[]> generateExcelByStoreIdWithDateRange(
            @RequestParam Integer store_id,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") String startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") String endDate) ;

    @PostMapping("/generate-pdf-vendor/")
    public ResponseEntity<?> generatePDF(
            @RequestParam Integer store_id,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") String startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") String endDate) throws DocumentException ;

    @PostMapping("/generate-pdf-vendor/{store_id}")
    public ResponseEntity<?> generatePDFByStoreid(
            @PathVariable Integer store_id) throws DocumentException ;

    @PatchMapping("/vendors/{vendor_id}")
    public ResponseEntity<?> updateVendor(@PathVariable("vendor_id") Long vendor_id, @RequestBody Vendor vendor) ;


}