package com.syntiaro_pos_system.controllerimpl.v2;

import com.syntiaro_pos_system.controller.v2.BalanceController;
import com.syntiaro_pos_system.entity.v1.Balance;
import com.syntiaro_pos_system.entity.v2.ApiResponse;
import com.syntiaro_pos_system.service.v2.BalanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BalanceControllerImplV2 implements BalanceController {

    @Autowired
    BalanceService balanceService;

    @Override
    public ResponseEntity<ApiResponse> openBalance(Balance balance) {
        return balanceService.openBalance(balance);
    }

    @Override
    public ResponseEntity<ApiResponse> finalClosingBalance(Balance balance) {
        return balanceService.finalClosingBalance(balance);
    }

    @Override
    public ResponseEntity<ApiResponse> YesterdayClosingBalance(Integer storeId) {
        return balanceService.YesterdayClosingBalance(storeId);
    }

    @Override
    public ResponseEntity<ApiResponse> RemainingBalance(Integer storeId) {
        return balanceService.RemainingBalance(storeId);
    }

    @Override
    public ResponseEntity<ApiResponse> balanceList(Integer storeId) {
        return balanceService.balanceList(storeId);
    }
}
