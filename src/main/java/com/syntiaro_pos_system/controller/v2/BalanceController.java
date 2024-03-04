package com.syntiaro_pos_system.controller.v2;

import com.syntiaro_pos_system.entity.v2.ApiResponse;
import com.syntiaro_pos_system.entity.v1.Balance;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(path = "/v2/sys/balance/")
public interface BalanceController {

    @PostMapping("/open") // THIS METHOD WE ADD DAY BALANCE
    public ResponseEntity<ApiResponse> openBalance(@RequestBody Balance balance);

    @PostMapping("/close") // AFTER THIS WE DO NOT ANYTHING
    public ResponseEntity<ApiResponse> finalClosingBalance(@RequestBody Balance balance);

    @GetMapping("/yesterday_closing_balance/{storeId}") // GET YESTERDAY COLOSING BALANCE
    public ResponseEntity<ApiResponse> YesterdayClosingBalance(@PathVariable Integer storeId);

    @GetMapping("/remaining_balance/{storeId}") // GET REMAINING BALANCE
    public ResponseEntity<ApiResponse> RemainingBalance(@PathVariable Integer storeId) ;

    @GetMapping("/store/{storeId}") // GET BALANCE LIST BY CASH,CARD.UPI
    public ResponseEntity<ApiResponse> balanceList(@PathVariable Integer storeId);

}
