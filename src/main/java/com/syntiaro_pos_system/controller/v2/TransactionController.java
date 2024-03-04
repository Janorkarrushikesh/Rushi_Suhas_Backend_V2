package com.syntiaro_pos_system.controller.v2;

import com.syntiaro_pos_system.entity.v1.TransactionRecord;
import com.syntiaro_pos_system.entity.v2.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/v2/sys/transaction/")
public interface TransactionController {

    @PostMapping("/")
    public ResponseEntity<ApiResponse> debiteTransaction(@RequestBody TransactionRecord transactionRecord);

    @GetMapping("/store/{storeId}")
    public ResponseEntity<ApiResponse> transactionByStore(@PathVariable Integer storeId);

}
