package com.syntiaro_pos_system.service.v2;

import com.syntiaro_pos_system.entity.v1.Bill;
import com.syntiaro_pos_system.entity.v2.ApiResponse;
import com.syntiaro_pos_system.request.v1.BillRequest;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

public interface Billservice {
    ResponseEntity<ApiResponse> saveBillAndOrder(BillRequest billRequest);

    ResponseEntity<ApiResponse> getBillBySerialNo(Integer serialNo);

    ResponseEntity<ApiResponse> getBillByStoreId(Integer storeId);

    ResponseEntity<ApiResponse> updateBillBySerialNo(Integer serialNo, Bill bill);

    ResponseEntity<ApiResponse> fetchBillByStoreId(Integer storeId, Integer page, Integer size, LocalDate startDate, LocalDate endDate);

    ResponseEntity<ApiResponse> quickBill(BillRequest bill);

    ResponseEntity<ApiResponse> billStatusReport(Integer storeId, List<String> orderStatus);

    ResponseEntity<ApiResponse> calculateTotalByPaymnetMode(Integer storeId);

    ResponseEntity<ApiResponse> balanceReportByPaymentMode(Integer storeId);

    ResponseEntity<ApiResponse> updateOrderStatus(Integer serialNo, String orderStatus);

    ResponseEntity<ApiResponse> printBill(Integer serialNo);
}
