package com.syntiaro_pos_system.controller.v1;

import com.syntiaro_pos_system.entity.v1.Store;
import com.syntiaro_pos_system.entity.v1.Tax;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequestMapping(path = "/v1/sys/tax")
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public interface TaxController {


    @PostMapping("/save")
    ResponseEntity<Tax> createTax(@RequestBody Tax tax);

    // second method update tax use this for update tax ...
    @PatchMapping("/update/{taxId}")
    ResponseEntity<Tax> updateTax(@PathVariable Long taxId, @RequestBody Tax updatedTax);


    // THIS METHOD IS USE FOR GET ALL TAX WITH STORE
    @GetMapping("/stores")
    ResponseEntity<List<Store>> getAllStoresWithTax();

    @GetMapping("/gettaxbyid/{taxid}")
    Optional<Tax> getid(@PathVariable Long taxid);

    // THIS METHOD IS USE FOR DELETE TAX WITH STORE
    @DeleteMapping("/taxes/{taxId}")
    ResponseEntity<String> deleteTax(@PathVariable Long taxId);

    // THIS METHOD IS USE FOR GET TAXES BY STORE ID
    @GetMapping("/stores/{storeId}/taxes")
    ResponseEntity<List<Tax>> getTaxesByStoreId(@PathVariable Long storeId);


}
