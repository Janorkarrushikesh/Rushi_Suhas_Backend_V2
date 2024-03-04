package com.syntiaro_pos_system.controller.v2;

import com.syntiaro_pos_system.entity.v2.ApiResponse;
import com.syntiaro_pos_system.entity.v1.StorePayment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/v2/sys/storePayment")
public interface StorePaymentController {
    @PostMapping("/")
    public ResponseEntity<ApiResponse> saveStorePayment(@RequestBody StorePayment storePayment);

    @GetMapping("/{SerialNo}")
    public ResponseEntity<ApiResponse> getById(@PathVariable Long SerialNo);

    @GetMapping("/store/{storeId}")
    public ResponseEntity<ApiResponse> getByStoreId(@PathVariable Long storeId);

    @GetMapping("/generateQRCode")
    public ResponseEntity<byte[]> generateQRCode(@RequestParam String text , HttpServletResponse response) throws IOException;

    @PatchMapping("/id/{SerialNo}")
    public ResponseEntity<ApiResponse> updateStorePayment(@PathVariable Long SerialNo,@RequestBody StorePayment payment);

    @DeleteMapping("/{SerialNo}")
    public ResponseEntity<ApiResponse> deleteById(@PathVariable Long SerialNo);
}
