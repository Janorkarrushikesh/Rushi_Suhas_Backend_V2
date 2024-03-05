package com.syntiaro_pos_system.controllerimpl.v2;

import com.syntiaro_pos_system.controller.v2.StorePaymentController;
import com.syntiaro_pos_system.entity.v1.StorePayment;
import com.syntiaro_pos_system.entity.v2.ApiResponse;
import com.syntiaro_pos_system.service.v2.StorePaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class StorePaymentV2ControllerImpl implements StorePaymentController {

    @Autowired
    StorePaymentService paymentService;

    @Override
    public ResponseEntity<ApiResponse> saveStorePayment(StorePayment storePayment) {
        return paymentService.saveStorePayment(storePayment);
    }

    @Override
    public ResponseEntity<ApiResponse> getById(Long SerialNo) {
        return paymentService.getById(SerialNo);
    }

    @Override
    public ResponseEntity<ApiResponse> getByStoreId(Long storeId) {
        return paymentService.getByStoreId(storeId);
    }

    @Override
    public ResponseEntity<byte[]> generateQRCode(String text, HttpServletResponse response) throws IOException {
        return paymentService.generateQRCode(text, response);
    }

    @Override
    public ResponseEntity<ApiResponse> updateStorePayment(Long SerialNo, StorePayment payment) {
        return paymentService.updateStorePayment(SerialNo, payment);
    }

    @Override
    public ResponseEntity<ApiResponse> deleteById(Long SerialNo) {
        return paymentService.deleteById(SerialNo);
    }
}
