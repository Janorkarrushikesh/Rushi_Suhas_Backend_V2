package com.syntiaro_pos_system.controller.v1;

import com.itextpdf.text.DocumentException;
import com.syntiaro_pos_system.entity.v1.TransactionRecord;
import com.syntiaro_pos_system.request.v1.TransactionRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/v1/sys/transaction")
public interface TransactionRecordController {

    @PostMapping("/end-of-day-close")
    ResponseEntity<String> endOfDayClose(@RequestBody TransactionRequest request);

    @GetMapping("/all-transactions")
    ResponseEntity<List<TransactionRecord>> getAllTransactions();

    @GetMapping("/transaction/{storeId}")
    ResponseEntity<List<TransactionRecord>> getTransactionByStoreId(@PathVariable Integer storeId);

    @PostMapping("/generate-pdf-transaction/")
    ResponseEntity<?> generatePDF(
            @RequestParam(required = false) Integer store_id,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) throws DocumentException;

    @PostMapping("/generate-pdf-transaction/{store_id}")
    ResponseEntity<?> generatePDFbyStoreid(
            @PathVariable Integer store_id) throws DocumentException;

}
