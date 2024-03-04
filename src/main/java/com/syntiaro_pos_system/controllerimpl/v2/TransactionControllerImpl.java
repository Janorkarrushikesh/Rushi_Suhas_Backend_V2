package com.syntiaro_pos_system.controllerimpl.v2;

import com.syntiaro_pos_system.controller.v2.TransactionController;
import com.syntiaro_pos_system.entity.v1.TransactionRecord;
import com.syntiaro_pos_system.entity.v2.ApiResponse;
import com.syntiaro_pos_system.service.v2.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class TransactionControllerImpl implements TransactionController {

    @Autowired
    TransactionService transactionService;

    @Override
    public ResponseEntity<ApiResponse> debiteTransaction(TransactionRecord transactionRecord) {
        return transactionService.debiteTransaction(transactionRecord);
    }

    @Override
    public ResponseEntity<ApiResponse> transactionByStore(Integer storeId) {
        return transactionService.transactionByStore(storeId);
    }
}
