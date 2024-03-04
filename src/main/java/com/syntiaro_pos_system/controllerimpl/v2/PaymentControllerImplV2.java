package com.syntiaro_pos_system.controllerimpl.v2;

import com.syntiaro_pos_system.controller.v2.PaymentController;
import com.syntiaro_pos_system.entity.v1.Payment;
import com.syntiaro_pos_system.entity.v2.ApiResponse;
import com.syntiaro_pos_system.service.v2.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
public class PaymentControllerImplV2 implements PaymentController {

    @Autowired
    PaymentService paymentService;

    @Override
    public ResponseEntity<ApiResponse> getPaymentByStoreId(Integer storeId , Integer page,Integer size) {
        return paymentService.getPaymentByStoreId(storeId , page ,size);
    }

    @Override
    public ResponseEntity<ApiResponse> getPaymenyById(Integer SerialNo) {
        return paymentService.getPaymenyById(SerialNo);
    }

    @Override
    public ResponseEntity<byte[]> QrCodeFor(Integer SerialNo ,  HttpServletResponse response) {
        return paymentService.qrCodeForPayment(SerialNo,response);
    }

    @Override
    public ResponseEntity<ApiResponse> savePayment(Payment payment) {

        return new ResponseEntity<>(paymentService.savePayment(payment), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ApiResponse> deletePaymenetById(Integer SerialNo) {
        return paymentService.deletePaymentById(SerialNo);
    }

    @Override
    public ResponseEntity<ApiResponse> updatePayment(Integer SerialNo, Payment payment) {
        return paymentService.updatePayment(SerialNo,payment);
    }
}
