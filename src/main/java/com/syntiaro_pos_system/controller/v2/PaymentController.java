package com.syntiaro_pos_system.controller.v2;

import com.syntiaro_pos_system.entity.v2.ApiResponse;
import com.syntiaro_pos_system.entity.v1.Payment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(path = "/v2/sys/payment")
public interface PaymentController {

    @PostMapping("/")
    public ResponseEntity<ApiResponse> savePayment(@RequestBody Payment payment);

    @GetMapping("/store/{storeId}")
    public ResponseEntity<ApiResponse> getPaymentByStoreId(@PathVariable Integer storeId ,
                                                           @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                           @RequestParam(name = "size", defaultValue = "10") Integer size);

    @GetMapping("/id/{SerialNo}")
    public ResponseEntity<ApiResponse> getPaymenyById(@PathVariable Integer SerialNo);

    @GetMapping("/qrCode/{SerialNo}")
    public ResponseEntity<byte[]> QrCodeFor(@PathVariable Integer SerialNo, HttpServletResponse response);

    @DeleteMapping("/{SerialNo}")
    public ResponseEntity<ApiResponse> deletePaymenetById(@PathVariable Integer SerialNo);

    @PatchMapping("/{SerialNo}")
    public ResponseEntity<ApiResponse> updatePayment(@PathVariable Integer SerialNo, @RequestBody Payment payment) ;

}
