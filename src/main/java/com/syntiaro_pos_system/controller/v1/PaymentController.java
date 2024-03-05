package com.syntiaro_pos_system.controller.v1;

import com.itextpdf.text.DocumentException;
import com.syntiaro_pos_system.entity.v1.Payment;
import com.syntiaro_pos_system.request.v1.paymentRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/v1/sys/Payment")
public interface PaymentController {
    @PostMapping(path = "/postpayment")
    String savePayment(@RequestBody Payment payment);

    @GetMapping(path = "/all_payment")
    List<Payment> getAllPayment();

    @PutMapping(path = "/updatepayment")
    Payment updatedpayment(@RequestBody Payment payment);

    @PatchMapping(path = "/updatePayment/{payment_id}")
    ResponseEntity<Payment> updatePayment(
            @PathVariable("payment_id") Integer srno,
            @RequestBody Payment payment);

    @PatchMapping(path = "/gatwayPayment/{payment_id}")
    ResponseEntity<Payment> Paymentgatway(
            @PathVariable("payment_id") Integer payment_id,
            @RequestBody Payment payment);

    @PostMapping(path = "/payments")
    Payment placepayment(@RequestBody paymentRequest request);

    @GetMapping("/payments/{storeId}")
    List<Payment> getPaymentsByStoreId(@PathVariable Integer storeId);

    @GetMapping("/{Serial_no}")
    ResponseEntity<Payment> getPaymentById(@PathVariable Integer Serial_no);

    @DeleteMapping("/{paymentId}")
    ResponseEntity<String> deletePayment(@PathVariable Integer paymentId);

    @PostMapping("/excelpayment/{storeId}")
    ResponseEntity<byte[]> generateExcelByStoreId(@PathVariable Integer storeId);

    @PostMapping("/excelpayment/")
    ResponseEntity<byte[]> generateExcelByStoreIdWithDateRange(
            @RequestParam Integer store_id,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") String startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") String endDate);

    @GetMapping("/generateQRCode/{paymentid}")
    ResponseEntity<byte[]> generateQRCode(@PathVariable Integer paymentid);

    @PostMapping("/generate-pdf-payment/")
    ResponseEntity<?> generatePDF(
            @RequestParam Integer store_id,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") String startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") String endDate) throws DocumentException;

    @PostMapping("/generate-pdf-payment/{store_id}")
    ResponseEntity<?> generatePDFByStoreId(
            @PathVariable Integer store_id) throws DocumentException;

    @PatchMapping("/vendorsPayment/{payment_id}")
    ResponseEntity<?> updateVendorPayment(@PathVariable("payment_id") Long payment_id, @RequestBody Payment payment);

}
