package com.syntiaro_pos_system.controller.v1;

import com.syntiaro_pos_system.entity.v1.ApiResponse;
import com.syntiaro_pos_system.entity.v1.Bill;
import com.syntiaro_pos_system.request.v1.BillRequest;
import com.itextpdf.text.DocumentException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(path = "/v1/sys/bill")
public interface BillController {
     @PostMapping(path = "/postbillOrder")
    public ResponseEntity<String> placebill(@RequestBody BillRequest request);
    @PutMapping(path = "/updatebill") // For Update Data
    public Bill updateBill(@RequestBody Bill bill);
    @DeleteMapping(path = "/deletebill/{id}") // For Delete Data
    public ResponseEntity<HttpStatus> deletebill(@PathVariable String id);


    @GetMapping(path = "/bill/{id}")
    public Optional<Bill> getbillbyid(@PathVariable Integer id);
    @PostMapping(path = "/postBill") // For Add Data
    public int saveBill(@RequestBody Bill bill);
    @GetMapping(path = "/getbill")
    public List<Bill> getBill();
    @GetMapping(path = "/getBillByID/{id}")
    public ResponseEntity<?> getBillById(@PathVariable Integer id);
    @PatchMapping(path = "/updateBillorder/{id}") // its use for
    public ResponseEntity<String> updateBill( @PathVariable("id") Integer id, @RequestBody Bill bill);
    @PatchMapping(path = "/patchbillOrder")
    public Bill patchbill(@RequestBody BillRequest request);
    @PostMapping("/ExcelBill/{store_id}")
    public ResponseEntity<byte[]> generateExcelByStoreId(@PathVariable Integer store_id);

    @PostMapping("/postid")
    public Bill placebilld(@RequestBody BillRequest request);

    @GetMapping("/getLastBill/{store_id}")
    public ResponseEntity<Bill> getLastBillByStoreIdd(@PathVariable("store_id") Integer storeId);

    @GetMapping("/billorder/{storeId}")
    public List<Bill> getBillsByStoreId(@PathVariable Integer storeId);

    @GetMapping("/pendingOrderReport/{storeId}")
    public List<Bill> pendingOrder(@PathVariable Integer storeId);

    @GetMapping("/KotReport/{storeId}")
    public List<Bill> kotOrder(@PathVariable Integer storeId);

    @GetMapping("/total-cash-amount/{storeId}/today")
    public ResponseEntity<Float> getTotalCashAmountByStoreIdAndToday(@PathVariable Integer storeId);

    @GetMapping("/total-card-amount/{storeId}/today")
    public ResponseEntity<Float> getTotalCardAmountByStoreIdAndToday(@PathVariable Integer storeId);

    @GetMapping("/total-upi-amount/{storeId}/today")
    public ResponseEntity<Float> getTotalUpiAmountByStoreIdAndToday(@PathVariable Integer storeId);

    @GetMapping("/total-payments/{storeId}/today")
    public ResponseEntity<Float> getTotalPaymentsByStoreIdAndToday(@PathVariable Integer storeId);

    @GetMapping("/generate/{billNo}")
    public ResponseEntity<byte[]> generatePdf(@PathVariable("billNo") Integer billNo);

    @GetMapping("/daily-cash-balance-report/{store_id}")
    public ResponseEntity<List<Bill>> getDailyBalanceReport(@PathVariable(name = "store_id") Integer storeId);

    @GetMapping("/daily-card-balance-report/{store_id}")
    public ResponseEntity<List<Bill>> getDailyBalanceReportCard(@PathVariable(name = "store_id") Integer storeId);

    @GetMapping("/daily-upi-balance-report/{store_id}")
    public ResponseEntity<List<Bill>> getDailyBalanceReportUpi(@PathVariable(name = "store_id") Integer storeId);

    @PostMapping("/create-bill")
    public ResponseEntity<String> createBill(@RequestBody BillRequest request);

    @PostMapping("/generate-pdf-bill/")
    public ResponseEntity<?> generatePDF(
            @RequestParam(required = false) Integer store_id,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) throws DocumentException;


    @PostMapping("/generate-excel-bill/")
    public ResponseEntity<byte[]> generateExcel(
            @RequestParam(required = false) Integer store_id,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate);

    @PostMapping("/total-payment-amount-pdf/")
    public ResponseEntity<byte[]> getTotalPaymentGeneratePdf(
            @RequestParam Integer storeId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) throws DocumentException;

    @PostMapping("/generate-pdf-bill/{store_id}")
    public ResponseEntity<?> generatePDFbyStoreid(
            @PathVariable Integer store_id) throws DocumentException;

    @PostMapping("/total-payment-amount-pdf/{storeId}")
    public ResponseEntity<byte[]> getTotalPaymentGeneratePdfbyStoreid( @PathVariable Integer storeId) throws DocumentException;

    @GetMapping("/Bill_Storeid")
    public ResponseEntity<List<Bill>> getBillsByStoreId(
            @RequestParam(name = "storeId") Integer storeId,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size);

    @GetMapping("/store/{storeId}/")
    public ResponseEntity<ApiResponse> getBillsReport(
            @PathVariable(name = "storeId") Integer storeId,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "20") Integer size);

}
