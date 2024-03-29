package com.syntiaro_pos_system.controller.v2;

import com.syntiaro_pos_system.entity.v1.Payment;
import com.syntiaro_pos_system.entity.v2.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(path = "/v2/sys/payment")
public interface PaymentController {

    @PostMapping("/")
    ResponseEntity<ApiResponse> savePayment(@RequestBody Payment payment);

    @GetMapping("/store/{storeId}")
    ResponseEntity<ApiResponse> getPaymentByStoreId(@PathVariable Integer storeId,

                                                    @RequestParam(name = "page", required = false) Integer page,
                                                    @RequestParam(name = "size", required = false) Integer size,
                                                    @RequestParam(required = false) String startDate,
                                                    @RequestParam(required = false) String endDate);

    @GetMapping("/id/{SerialNo}")
    ResponseEntity<ApiResponse> getPaymenyById(@PathVariable Integer SerialNo);

    @GetMapping("/qrCode/{SerialNo}")
    ResponseEntity<byte[]> QrCodeFor(@PathVariable Integer SerialNo, HttpServletResponse response);

    @DeleteMapping("/{SerialNo}")
    ResponseEntity<ApiResponse> deletePaymenetById(@PathVariable Integer SerialNo);

    @PatchMapping("/{SerialNo}")
    ResponseEntity<ApiResponse> updatePayment(@PathVariable Integer SerialNo, @RequestBody Payment payment);

}
