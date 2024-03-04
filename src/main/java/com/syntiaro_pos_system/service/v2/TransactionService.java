package com.syntiaro_pos_system.service.v2;

import com.syntiaro_pos_system.entity.v1.TransactionRecord;
import com.syntiaro_pos_system.entity.v2.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface TransactionService {
    ResponseEntity<ApiResponse> debiteTransaction(TransactionRecord transactionRecord);

    ResponseEntity<ApiResponse> transactionByStore(Integer storeId);
}
