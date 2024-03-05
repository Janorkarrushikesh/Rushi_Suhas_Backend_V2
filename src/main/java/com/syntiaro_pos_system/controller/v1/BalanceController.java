package com.syntiaro_pos_system.controller.v1;

import com.itextpdf.text.DocumentException;
import com.syntiaro_pos_system.entity.v1.Balance;
import com.syntiaro_pos_system.request.v1.BalanceRequest;
import com.syntiaro_pos_system.response.BalanceWithPaymentSummaryResponse;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/v1/sys/balance")
public interface BalanceController {


    @PatchMapping("/start-new-day")
    ResponseEntity<String> startNewDay(@RequestBody BalanceRequest balanceRequest);

    @PostMapping("/add-to-closing-balance")
    ResponseEntity<String> addToClosingBalance(@RequestBody Float additionalAmount);

    @PostMapping("/subtract-from-closing-balance")
    ResponseEntity<String> subtractFromClosingBalance(@RequestBody Float closingBalance);

    @GetMapping("/total-closing-balance")
    ResponseEntity<Float> getTotalClosingBalance();

    @GetMapping("/with-payment-summaries")
    ResponseEntity<List<BalanceWithPaymentSummaryResponse>> getAllBalancesWithPaymentSummaries();

    @GetMapping("/current-closing-balance/{store_id}")
    ResponseEntity<Double> getCurrentDateClosingBalanceByStoreId(@PathVariable Integer store_id);


    @GetMapping("/balance/{storeId}")
    ResponseEntity<List<Balance>> getBalanceByStoreId(@PathVariable Integer storeId);

    @PostMapping("/addBalance")
    ResponseEntity<String> addBalance(@RequestBody BalanceRequest balanceRequest);

    @GetMapping("/yesterday-closing-balance/{storeId}")
    Number getYesterdayClosingBalanceByStoreId(@PathVariable Integer storeId);

    //rushikesh code
    @GetMapping("/remaining_balance/{storeId}")
    Double getRemainingBalance(@PathVariable Integer storeId);


    @PostMapping("/generate-pdf-balance/")
    ResponseEntity<?> generatePDF(@RequestParam(required = false) Integer store_id,
                                  @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") String startDate,
                                  @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") String endDate) throws DocumentException;


}
