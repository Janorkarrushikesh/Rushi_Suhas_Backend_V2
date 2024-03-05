package com.syntiaro_pos_system.service.v2;


import com.syntiaro_pos_system.entity.v1.Payment;
import com.syntiaro_pos_system.entity.v2.ApiResponse;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletResponse;

public interface PaymentService {

    ResponseEntity<ApiResponse> savePayment(Payment payment);

    ResponseEntity<ApiResponse> getPaymentByStoreId(Integer storeId, Integer page, Integer size, String startDate, String endDate);

    ResponseEntity<ApiResponse> getPaymenyById(Integer serialNo);

    ResponseEntity<byte[]> qrCodeForPayment(Integer serialNo, HttpServletResponse response);

    ResponseEntity<ApiResponse> deletePaymentById(Integer serialNo);

    ResponseEntity<ApiResponse> updatePayment(Integer serialNo, Payment payment);
}
