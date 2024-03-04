package com.syntiaro_pos_system.controller.v1;

import com.syntiaro_pos_system.entity.v1.Balance;
import com.syntiaro_pos_system.request.v1.BalanceRequest;
import com.syntiaro_pos_system.response.BalanceWithPaymentSummaryResponse;
import com.itextpdf.text.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/v1/sys/balance")
public interface BalanceController {


    @PatchMapping("/start-new-day")
    public ResponseEntity<String> startNewDay(@RequestBody BalanceRequest balanceRequest);

    @PostMapping("/add-to-closing-balance")
    public ResponseEntity<String> addToClosingBalance(@RequestBody Float additionalAmount);

    @PostMapping("/subtract-from-closing-balance")
    public ResponseEntity<String> subtractFromClosingBalance(@RequestBody Float closingBalance);

    @GetMapping("/total-closing-balance")
    public ResponseEntity<Float> getTotalClosingBalance();

    @GetMapping("/with-payment-summaries")
    public ResponseEntity<List<BalanceWithPaymentSummaryResponse>> getAllBalancesWithPaymentSummaries();

    @GetMapping("/current-closing-balance/{store_id}")
    public ResponseEntity<Double> getCurrentDateClosingBalanceByStoreId(@PathVariable Integer store_id);


    @GetMapping("/balance/{storeId}")
    public ResponseEntity<List<Balance>> getBalanceByStoreId(@PathVariable Integer storeId);

    @PostMapping("/addBalance")
    public ResponseEntity<String> addBalance(@RequestBody BalanceRequest balanceRequest) ;

    @GetMapping("/yesterday-closing-balance/{storeId}")
    public Number getYesterdayClosingBalanceByStoreId(@PathVariable Integer storeId);

    //rushikesh code
    @GetMapping("/remaining_balance/{storeId}")
    public Double getRemainingBalance(@PathVariable Integer storeId);


    @PostMapping("/generate-pdf-balance/")
    public ResponseEntity<?> generatePDF( @RequestParam(required = false) Integer store_id,
                                          @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") String startDate,
                                          @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") String endDate) throws DocumentException ;


}
