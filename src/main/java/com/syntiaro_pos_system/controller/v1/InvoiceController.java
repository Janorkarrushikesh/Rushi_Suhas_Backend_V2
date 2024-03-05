package com.syntiaro_pos_system.controller.v1;

import com.itextpdf.text.DocumentException;
import com.syntiaro_pos_system.entity.v1.VendorInventory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(path = "/v1/sys/vendorinvoice")
public interface InvoiceController {
    @PostMapping(path = "/postinvoice")
    String saveInvoice(@RequestBody VendorInventory vendorInventory);

    @PutMapping(path = "/updatess")
    VendorInventory updateinvoice(@RequestBody VendorInventory vendorInventory);

    @GetMapping(path = "/allinvoice")
    List<VendorInventory> getInvoice();

    @PatchMapping(path = "/updateinvoice/{invoice_id}")
    ResponseEntity<VendorInventory> updateInvoice(
            @PathVariable("invoice_id") Integer invoice_id,
            @RequestBody VendorInventory vendorInventory);

    @GetMapping("/invoices/{storeId}")
    List<VendorInventory> getInvoicesByStoreId(@PathVariable Integer storeId);

    @GetMapping("/getInvoiceByID/{id}")
    ResponseEntity<?> fetchDetailsById(@PathVariable Integer id);

    // THIS METHOD IS USE FOR DELETE INVOICE
    @DeleteMapping("/{invoiceId}")
    ResponseEntity<String> deleteInvoice(@PathVariable Integer invoiceId);

    @PostMapping("/excelinvoice/{storeId}")
    ResponseEntity<byte[]> generateExcelByStoreId(@PathVariable Integer storeId);

    @PostMapping("/excelinvoicedate/")
    ResponseEntity<byte[]> generateExcelByStoreId(
            @RequestParam Integer store_id,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") String startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") String endDate);

    @PostMapping("/generate-pdf-invoice/")
    ResponseEntity<?> generatePDF(
            @RequestParam Integer store_id,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") String startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") String endDate) throws DocumentException;

    @PostMapping("/generate-pdf-invoice/{store_id}")
    ResponseEntity<?> generatePDFbystoreid(
            @PathVariable Integer store_id) throws DocumentException;

}
