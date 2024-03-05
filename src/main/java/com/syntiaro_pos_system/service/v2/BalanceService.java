package com.syntiaro_pos_system.service.v2;

import com.syntiaro_pos_system.entity.v1.Balance;
import com.syntiaro_pos_system.entity.v2.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface BalanceService {

    ResponseEntity<ApiResponse> openBalance(Balance balance);

    ResponseEntity<ApiResponse> finalClosingBalance(Balance balance);

    ResponseEntity<ApiResponse> YesterdayClosingBalance(Integer storeId);

    ResponseEntity<ApiResponse> RemainingBalance(Integer storeId);

    ResponseEntity<ApiResponse> balanceList(Integer storeId);
}
