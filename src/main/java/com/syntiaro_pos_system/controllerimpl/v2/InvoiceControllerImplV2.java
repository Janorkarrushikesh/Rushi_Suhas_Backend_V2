package com.syntiaro_pos_system.controllerimpl.v2;

import com.syntiaro_pos_system.controller.v2.InvoiceController;
import com.syntiaro_pos_system.entity.v1.Invoice;
import com.syntiaro_pos_system.entity.v2.ApiResponse;
import com.syntiaro_pos_system.service.v2.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InvoiceControllerImplV2 implements InvoiceController {

    @Autowired
    InvoiceService invoiceService;

    @Override
    public ResponseEntity<ApiResponse> saveInvoice(Invoice invoice) {
        return invoiceService.saveInvoice(invoice);
    }

    @Override
    public ResponseEntity<ApiResponse> getInvoiceById(Integer SerialNo) {
        return new ResponseEntity<>(invoiceService.getInvoiceById(SerialNo), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ApiResponse> getInvoiceByStoreId(Integer storeId) {
        return new ResponseEntity<>(invoiceService.getInvoiceByStoreId(storeId),HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ApiResponse> updateInvoice(Integer SerialNo, Invoice invoice) {
        return invoiceService.updateInvoice(SerialNo,invoice);
    }

    @Override
    public ResponseEntity<ApiResponse> deleteInvoiceById(Integer SerialNo) {
        return invoiceService.deleteInvoiceByid(SerialNo);
    }


}
