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
    public ResponseEntity<Tax> createTax(@RequestBody Tax tax) ;

    // second method update tax use this for update tax ...
    @PatchMapping("/update/{taxId}")
    public ResponseEntity<Tax> updateTax(@PathVariable Long taxId, @RequestBody Tax updatedTax) ;



    // THIS METHOD IS USE FOR GET ALL TAX WITH STORE
    @GetMapping("/stores")
    public ResponseEntity<List<Store>> getAllStoresWithTax() ;

    @GetMapping("/gettaxbyid/{taxid}")
    public Optional<Tax> getid(@PathVariable Long taxid) ;

    // THIS METHOD IS USE FOR DELETE TAX WITH STORE
    @DeleteMapping("/taxes/{taxId}")
    public ResponseEntity<String> deleteTax(@PathVariable Long taxId) ;

    // THIS METHOD IS USE FOR GET TAXES BY STORE ID
    @GetMapping("/stores/{storeId}/taxes")
    public ResponseEntity<List<Tax>> getTaxesByStoreId(@PathVariable Long storeId) ;


}
