package com.syntiaro_pos_system.controller.v2;

import com.syntiaro_pos_system.entity.v1.StorePayment;
import com.syntiaro_pos_system.entity.v2.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/v2/sys/storePayment")
public interface StorePaymentController {
    @PostMapping("/")
    ResponseEntity<ApiResponse> saveStorePayment(@RequestBody StorePayment storePayment);

    @GetMapping("/{SerialNo}")
    ResponseEntity<ApiResponse> getById(@PathVariable Long SerialNo);

    @GetMapping("/store/{storeId}")
    ResponseEntity<ApiResponse> getByStoreId(@PathVariable Long storeId);

    @GetMapping("/generateQRCode")
    ResponseEntity<byte[]> generateQRCode(@RequestParam String text, HttpServletResponse response) throws IOException;

    @PatchMapping("/id/{SerialNo}")
    ResponseEntity<ApiResponse> updateStorePayment(@PathVariable Long SerialNo, @RequestBody StorePayment payment);

    @DeleteMapping("/{SerialNo}")
    ResponseEntity<ApiResponse> deleteById(@PathVariable Long SerialNo);
}
