package com.syntiaro_pos_system.service.v2;


import com.syntiaro_pos_system.entity.v2.ApiResponse;
import com.syntiaro_pos_system.entity.v1.Payment;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletResponse;

public interface PaymentService {
    public ResponseEntity<ApiResponse> savePayment(Payment payment);

    public ResponseEntity<ApiResponse> getPaymentByStoreId(Integer storeId , Integer page , Integer size,String startDate,String endDate);

    public ResponseEntity<ApiResponse> getPaymenyById(Integer serialNo);

    ResponseEntity<byte[]> qrCodeForPayment(Integer serialNo , HttpServletResponse response);

    ResponseEntity<ApiResponse> deletePaymentById(Integer serialNo);

    ResponseEntity<ApiResponse> updatePayment(Integer serialNo, Payment payment);
}
