package com.syntiaro_pos_system.controller.v1;

import com.syntiaro_pos_system.entity.v1.Payment;

import com.syntiaro_pos_system.request.v1.paymentRequest;
import com.itextpdf.text.DocumentException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/v1/sys/Payment")
public interface  PaymentController {
    @PostMapping(path = "/postpayment")
    public String savePayment(@RequestBody Payment payment);
    @GetMapping(path = "/all_payment")
    List<Payment> getAllPayment();

    @PutMapping(path = "/updatepayment")
    public Payment updatedpayment(@RequestBody Payment payment);

    @PatchMapping(path = "/updatePayment/{payment_id}")
    public ResponseEntity<Payment> updatePayment(
            @PathVariable("payment_id") Integer srno,
            @RequestBody Payment payment);

    @PatchMapping(path = "/gatwayPayment/{payment_id}")
    public ResponseEntity<Payment> Paymentgatway(
            @PathVariable("payment_id") Integer payment_id,
            @RequestBody Payment payment);

    @PostMapping(path = "/payments")
    public Payment placepayment(@RequestBody paymentRequest request) ;

    @GetMapping("/payments/{storeId}")
    public List<Payment> getPaymentsByStoreId(@PathVariable Integer storeId) ;

    @GetMapping("/{Serial_no}")
    public ResponseEntity<Payment> getPaymentById(@PathVariable Integer Serial_no) ;

    @DeleteMapping("/{paymentId}")
    public ResponseEntity<String> deletePayment(@PathVariable Integer paymentId) ;

    @PostMapping("/excelpayment/{storeId}")
    public ResponseEntity<byte[]> generateExcelByStoreId(@PathVariable Integer storeId) ;

    @PostMapping("/excelpayment/")
    public ResponseEntity<byte[]> generateExcelByStoreIdWithDateRange(
            @RequestParam Integer store_id,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") String startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") String endDate) ;

    @GetMapping("/generateQRCode/{paymentid}")
    public ResponseEntity<byte[]> generateQRCode(@PathVariable Integer paymentid);

    @PostMapping("/generate-pdf-payment/")
    public ResponseEntity<?> generatePDF(
            @RequestParam Integer store_id,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") String startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") String endDate) throws DocumentException ;

    @PostMapping("/generate-pdf-payment/{store_id}")
    public ResponseEntity<?> generatePDFByStoreId(
            @PathVariable Integer store_id) throws DocumentException ;

    @PatchMapping("/vendorsPayment/{payment_id}")
    public ResponseEntity<?> updateVendorPayment(@PathVariable("payment_id") Long payment_id, @RequestBody Payment payment) ;

}
