package com.syntiaro_pos_system.service.v1;

import com.syntiaro_pos_system.entity.v1.Payment;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface PaymentService {

    List<Payment> getPayment();

    Payment updatedPayment(Payment payment);

    String addPayment(Payment payment);

    // THIS METHOD IS USE FOR UPDATE PAYMENT
    Payment updatePayment(Integer payment_id, Payment payment);

    Payment PaymentGatway(Integer payment_id, Payment payment);

    // THIS METHOD IS USE FOR FETCH PAYMENT BY STOREID
    List<Payment> fetchPaymentsByStoreId(Integer storeId);

    // THIS METHOD IS USE FOR FETCH PAYMENT BY ID
    Payment getPaymentById(Integer Serial_no);

    // THIS METHOD IS USE FOR UPDATE PAYMENT
    ResponseEntity<?> updateVendorPaymentWithValidation(Long payment_id, Payment payment);
}

