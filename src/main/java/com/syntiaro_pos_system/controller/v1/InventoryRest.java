package com.syntiaro_pos_system.controller.v1;

import com.itextpdf.text.DocumentException;
import com.syntiaro_pos_system.entity.v1.Inventory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(path = "/v1/sys/inventory")
public interface InventoryRest {
    @PostMapping("/save")
    ResponseEntity<String> addInventoryItem(@RequestBody Inventory newItem);

    @GetMapping(path = "/gets")
    List<Inventory> getinvo();

    @PutMapping(path = "/updates/{id}")
    Inventory updateInventory(@RequestBody Inventory inventory);

    @DeleteMapping(path = "/deleteinventory/{id}")
    ResponseEntity<HttpStatus> deleteinventory(@PathVariable String id);

    @GetMapping("/getInventoryByID/{id}")
    Inventory fetchDetailsById(@PathVariable int id);

    @PatchMapping(path = "/updateinventory/{id}")
    ResponseEntity<Inventory> updateInventory(@PathVariable String id,
                                              @RequestBody Inventory inventory);

    @PostMapping("/generateExcel/{store_id}")
    ResponseEntity<byte[]> generateExcelByStoreId(@PathVariable Integer store_id);

    @PostMapping("/generate-pdf-inventory/{store_id}")
    ResponseEntity<?> generatePDFByStoreId(
            @PathVariable Integer store_id) throws DocumentException;

    @PostMapping("/generate-pdf-inventory/")
    ResponseEntity<?> generatePDF(
            @RequestParam String storeid,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") String startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") String endDate) throws DocumentException;

    @PostMapping("/excelInventory/")
    ResponseEntity<byte[]> generateExcelByStoreIdWithDateRange(
            @RequestParam String storeid,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") String startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") String endDate);

    @GetMapping("/minilevel/{storeId}")
    ResponseEntity<List<String>> getMinileve(@PathVariable String storeId);

    @GetMapping("/inventory/{storeId}")
    ResponseEntity<List<Inventory>> getInventoryByStoreId(@PathVariable String storeId);


}
