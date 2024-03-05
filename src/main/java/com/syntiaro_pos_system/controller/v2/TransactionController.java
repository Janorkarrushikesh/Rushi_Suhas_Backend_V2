package com.syntiaro_pos_system.controller.v2;

import com.syntiaro_pos_system.entity.v1.TransactionRecord;
import com.syntiaro_pos_system.entity.v2.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/v2/sys/transaction/")
public interface TransactionController {

    @PostMapping("/")
    ResponseEntity<ApiResponse> debiteTransaction(@RequestBody TransactionRecord transactionRecord);

    @GetMapping("/store/{storeId}")
    ResponseEntity<ApiResponse> transactionByStore(@PathVariable Integer storeId);

}
