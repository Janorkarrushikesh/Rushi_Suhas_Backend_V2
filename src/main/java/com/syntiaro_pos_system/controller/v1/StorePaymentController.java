package com.syntiaro_pos_system.controller.v1;

import com.syntiaro_pos_system.entity.v1.StorePayment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/v1/sys/storepayments")
public interface StorePaymentController {

    @GetMapping("/getAll")
    public ResponseEntity<List<StorePayment>> getAllStorePayments() ;
    @GetMapping("/getupi/{storeId}")
    public ResponseEntity<String> getUpiByStoreId(@PathVariable Long storeId) ;
    @GetMapping("/{paymentId}")
    public ResponseEntity<StorePayment> getStorePaymentById(@PathVariable Long paymentId) ;

    @DeleteMapping("/deletestorepayment/{paymentId}")
    public ResponseEntity<String> deleteStorePayment(@PathVariable Long paymentId) ;

    @GetMapping("/storepayment/{storeId}")
    public ResponseEntity<List<StorePayment>> getStorePaymentsByStoreId(@PathVariable Long storeId) ;

    @GetMapping("/generateQRCode")
    public void generateQRCode(@RequestParam String text, HttpServletResponse response) throws IOException ;

    @PostMapping("/save")
    public ResponseEntity<?> createStorePayment(@RequestBody StorePayment storePayment) ;


    @PatchMapping("/updatestorepayment/{paymentId}")
    public ResponseEntity<String> updateStorePayment(@PathVariable Long paymentId,
                                                     @RequestBody StorePayment updatedPayment) ;

}
