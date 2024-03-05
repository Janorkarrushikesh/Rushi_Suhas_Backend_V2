package com.syntiaro_pos_system.service.v2;

import com.syntiaro_pos_system.entity.v1.StorePayment;
import com.syntiaro_pos_system.entity.v2.ApiResponse;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletResponse;

public interface StorePaymentService {
    ResponseEntity<ApiResponse> saveStorePayment(StorePayment storePayment);

    ResponseEntity<ApiResponse> getById(Long serialNo);

    ResponseEntity<ApiResponse> getByStoreId(Long storeId);

    ResponseEntity<byte[]> generateQRCode(String text, HttpServletResponse response);

    ResponseEntity<ApiResponse> updateStorePayment(Long serialNo, StorePayment payment);

    ResponseEntity<ApiResponse> deleteById(Long serialNo);
}
