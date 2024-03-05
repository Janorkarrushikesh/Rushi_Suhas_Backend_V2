package com.syntiaro_pos_system.controllerimpl.v2;

import com.syntiaro_pos_system.controller.v2.BillController;
import com.syntiaro_pos_system.entity.v1.Bill;
import com.syntiaro_pos_system.entity.v2.ApiResponse;
import com.syntiaro_pos_system.request.v1.BillRequest;
import com.syntiaro_pos_system.service.v2.Billservice;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
public class BillControllerImpl implements BillController {

    public static final Logger logger = Logger.getLogger(BillControllerImpl.class);
    @Autowired
    Billservice billservice;

    @Override
    public ResponseEntity<ApiResponse> saveBillAndOrder(BillRequest billRequest) {
        logger.debug("loading ......");
        return billservice.saveBillAndOrder(billRequest);
    }

    @Override
    public ResponseEntity<ApiResponse> getBillBySerialNo(Integer SerialNo) {
        // logger.debug("loading ......");
        return billservice.getBillBySerialNo(SerialNo);
    }

    @Override
    public ResponseEntity<ApiResponse> getBillByStoreId(Integer storeId) {
        return billservice.getBillByStoreId(storeId);
    }

    @Override
    public ResponseEntity<ApiResponse> updateBillBySerialNo(Integer SerialNo, Bill bill) {
        return billservice.updateBillBySerialNo(SerialNo, bill);
    }

    @Override
    public ResponseEntity<ApiResponse> getStoreID(Integer storeId, Integer page, Integer size, LocalDate startDate, LocalDate endDate) {
        logger.debug("loading ......");
        return billservice.fetchBillByStoreId(storeId, page, size, startDate, endDate);
    }

    @Override
    public ResponseEntity<ApiResponse> quickBill(BillRequest bill) {
        return billservice.quickBill(bill);
    }

    @Override

    public ResponseEntity<ApiResponse> billStatusReport(Integer storeId, List<String> orderStatus) {
        return billservice.billStatusReport(storeId, orderStatus);
    }

    @Override
    public ResponseEntity<ApiResponse> calculateTotalByPaymnetMode(Integer storeId) {
        return billservice.calculateTotalByPaymnetMode(storeId);
    }

    @Override
    public ResponseEntity<ApiResponse> balanceReportByPaymentMode(Integer storeId) {
        return billservice.balanceReportByPaymentMode(storeId);
    }

    @Override
    public ResponseEntity<ApiResponse> updateOrderStatus(Integer SerialNo, String orderStatus) {
        return billservice.updateOrderStatus(SerialNo,orderStatus);
    }

    @Override
    public ResponseEntity<ApiResponse> printBill(Integer SerialNo) {
        return billservice.printBill(SerialNo);
    }
}
