package com.syntiaro_pos_system.service.v2;

import com.syntiaro_pos_system.entity.v1.Invoice;
import com.syntiaro_pos_system.entity.v2.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface InvoiceService {

    public ApiResponse getInvoiceById(Integer SerialNo);

    ApiResponse getInvoiceByStoreId(Integer storeId);

    ResponseEntity<ApiResponse> saveInvoice(Invoice invoice);

    public ResponseEntity<ApiResponse> updateInvoice(Integer SerialNo, Invoice invoice);

    ResponseEntity<ApiResponse> deleteInvoiceByid(Integer serialNo);
}
