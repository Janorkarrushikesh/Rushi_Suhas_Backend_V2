package com.syntiaro_pos_system.service.v2;

import com.syntiaro_pos_system.entity.v1.Bill;
import com.syntiaro_pos_system.entity.v2.ApiResponse;
import com.syntiaro_pos_system.request.v1.BillRequest;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

public interface Billservice {
    public ResponseEntity<ApiResponse> saveBillAndOrder(BillRequest billRequest);

    ResponseEntity<ApiResponse> getBillBySerialNo(Integer serialNo);

    ResponseEntity<ApiResponse> getBillByStoreId(Integer storeId);

    ResponseEntity<ApiResponse> updateBillBySerialNo(Integer serialNo , Bill bill);

    ResponseEntity<ApiResponse> fetchBillByStoreId(Integer storeId , Integer page , Integer size , LocalDate startDate , LocalDate endDate);

    ResponseEntity<ApiResponse> quickBill(BillRequest bill);

    ResponseEntity<ApiResponse> billStatusReport(Integer storeId, String orderStatus);

    ResponseEntity<ApiResponse> calculateTotalByPaymnetMode(Integer storeId);

    ResponseEntity<ApiResponse> balanceReportByPaymentMode(Integer storeId);
}
