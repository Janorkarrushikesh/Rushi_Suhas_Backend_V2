package com.syntiaro_pos_system.controller.v2;

import com.syntiaro_pos_system.entity.v1.Bill;
import com.syntiaro_pos_system.entity.v2.ApiResponse;
import com.syntiaro_pos_system.request.v1.BillRequest;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;


@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(path = "/v2/sys/bill/")
public interface BillController {


    @PostMapping("/")
    public ResponseEntity<ApiResponse> saveBillAndOrder(@RequestBody BillRequest billRequest);

    @GetMapping("/{SerialNo}")
    public ResponseEntity<ApiResponse> getBillBySerialNo(@PathVariable Integer SerialNo);

    @GetMapping("/store/{storeId}")
    public ResponseEntity<ApiResponse> getBillByStoreId(@PathVariable Integer storeId);

    @PatchMapping("/id/{SerialNo}")
    // @CachePut(value = "Bills" , key = "#bill.SerialNo" )
    public ResponseEntity<ApiResponse> updateBillBySerialNo(@PathVariable Integer SerialNo, @RequestBody Bill bill);

    @GetMapping("/storeid/{storeId}")
    //  @Cacheable(value = "Bills" )
    public ResponseEntity<ApiResponse> getStoreID(@PathVariable Integer storeId ,
                                                  @RequestParam(required = false) Integer page ,
                                                  @RequestParam(required = false) Integer size,
                                                  @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                                                  @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate);

    @PostMapping("/quick")
    public ResponseEntity<ApiResponse> quickBill(@RequestBody BillRequest bill);

    @GetMapping("/statusreport")
    public ResponseEntity<ApiResponse> billStatusReport(@RequestParam Integer storeId, @RequestParam List<String> orderStatus );

    @GetMapping("/total/{storeId}")
    public ResponseEntity<ApiResponse> calculateTotalByPaymnetMode(@PathVariable Integer storeId) ;

    @GetMapping("/balancereport/{storeId}")
    public ResponseEntity<ApiResponse> balanceReportByPaymentMode(@PathVariable Integer storeId);

    @GetMapping("/clear_cache")
    // @CacheEvict(value = "Bills", allEntries = true )
    public default String clearCache() {
        return "Cache has been cleared";
    }

}
