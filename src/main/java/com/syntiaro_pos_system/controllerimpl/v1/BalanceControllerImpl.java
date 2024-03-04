package com.syntiaro_pos_system.controllerimpl.v1;


import com.syntiaro_pos_system.controller.v1.BalanceController;
import com.syntiaro_pos_system.entity.v1.Balance;
import com.syntiaro_pos_system.entity.v1.TransactionRecord;
import com.syntiaro_pos_system.repository.v1.BalanceRepository;
import com.syntiaro_pos_system.repository.v1.TransactionRecordRepository;
import com.syntiaro_pos_system.request.v1.BalanceRequest;
import com.syntiaro_pos_system.response.BalanceWithPaymentSummaryResponse;
import com.syntiaro_pos_system.serviceimpl.v1.BalanceService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@RestController
public class BalanceControllerImpl implements BalanceController{

    @Autowired
    BalanceService balanceService;

    @Autowired
    BalanceRepository balanceRepository;


    @Autowired
    TransactionRecordRepository transactionRecordRepository;


    @Override
    public ResponseEntity<String> startNewDay(@RequestBody BalanceRequest balanceRequest) {
        if (balanceRequest == null) {
            return ResponseEntity.badRequest().body("Invalid request");
        }
        LocalDate today = LocalDate.now();
        Balance existingBalance = balanceRepository.findByStoreIdAndDate(balanceRequest.getStoreId(), today);
        Balance newBalance = new Balance();

        if (existingBalance == null) {
            Double addMoreAmount = Double.valueOf(balanceRequest.getAddMoreAmounts());
            if (addMoreAmount == null) {
                return ResponseEntity.badRequest().body("Invalid addMoreAmount value");
            }
            newBalance.setAddMoreAmount(addMoreAmount);
            newBalance.setRemainingBalance(addMoreAmount);
            newBalance.setStoreId(balanceRequest.getStoreId());
            newBalance.setTodays_Opening_Balance(addMoreAmount);
            newBalance.setCreatedBy(balanceRequest.getCreatedBy());
            newBalance.setUpdatedBy(balanceRequest.getUpdatedBy());
            newBalance.setCreatedDate(String.valueOf(LocalDateTime.now()));
            newBalance.setUpdatedDate(String.valueOf(LocalDateTime.now()));
            balanceRepository.save(newBalance);
            return ResponseEntity.ok("Balance added successfully for the new day!!! Today's Opening Balance: ");

        } else if (existingBalance.getAddMoreAmount() != null) {

            if(existingBalance.getFinalClosingBalance() != null){
                return ResponseEntity.badRequest().body(" After Final closing you dont have access");
            }

            Double addMoreAmount = balanceRequest.getAddMoreAmounts();
            if (addMoreAmount == null) {
                return ResponseEntity.badRequest().body("Invalid addMoreAmount value");
            }
            newBalance.setId(existingBalance.getId());
            newBalance.setStoreId(balanceRequest.getStoreId());
            newBalance.setAddMoreAmount(addMoreAmount + existingBalance.getAddMoreAmount());
            newBalance.setRemainingBalance(addMoreAmount  + existingBalance.getRemainingBalance());
            newBalance.setTodays_Opening_Balance(existingBalance.getTodays_Opening_Balance());
            newBalance.setUpdatedDate(String.valueOf(LocalDateTime.now()));
            newBalance.setCreatedDate(existingBalance.getCreatedDate());
            newBalance.setCreatedBy(existingBalance.getCreatedBy());
            newBalance.setUpdatedBy(balanceRequest.getUpdatedBy());
            balanceRepository.save(newBalance);

            TransactionRecord endOfDayTransaction = new TransactionRecord();
            endOfDayTransaction.setDate(LocalDate.now()); // Set the date to the current date
            endOfDayTransaction.setCashier(balanceRequest.getCreatedBy());
            endOfDayTransaction.setStoreId(balanceRequest.getStoreId());
            endOfDayTransaction.setStatus(("Credited"));
            endOfDayTransaction.setExpense(balanceRequest.getCreatedBy());
            endOfDayTransaction.setAmount(balanceRequest.getAddMoreAmounts());

            transactionRecordRepository.save(endOfDayTransaction);
            return ResponseEntity.ok("Balance is added for this store ID for today.");

        }

        else {

            if(existingBalance.getFinalClosingBalance() != null){
                return ResponseEntity.badRequest().body(" After Final closing you dont have access");
            }

            Double addMoreAmount = balanceRequest.getAddMoreAmounts();
            if (addMoreAmount == null) {
                return ResponseEntity.badRequest().body("Invalid addMoreAmount value");
            }
            newBalance.setId(existingBalance.getId());
            newBalance.setStoreId(balanceRequest.getStoreId());
            newBalance.setAddMoreAmount(addMoreAmount );
            newBalance.setRemainingBalance(addMoreAmount  + existingBalance.getRemainingBalance());
            newBalance.setTodays_Opening_Balance(existingBalance.getTodays_Opening_Balance());
            newBalance.setUpdatedDate(String.valueOf(LocalDateTime.now()));
            newBalance.setCreatedDate(existingBalance.getCreatedDate());
            newBalance.setCreatedBy(existingBalance.getCreatedBy());
            newBalance.setUpdatedBy(balanceRequest.getUpdatedBy());
            balanceRepository.save(newBalance);


            TransactionRecord endOfDayTransaction = new TransactionRecord();
            endOfDayTransaction.setDate(LocalDate.now()); // Set the date to the current date
            endOfDayTransaction.setCashier(balanceRequest.getCreatedBy());
            endOfDayTransaction.setStoreId(balanceRequest.getStoreId());
            endOfDayTransaction.setStatus(("Credited"));
            endOfDayTransaction.setExpense(balanceRequest.getCreatedBy());
            endOfDayTransaction.setAmount(balanceRequest.getAddMoreAmounts());
            transactionRecordRepository.save(endOfDayTransaction);
            return ResponseEntity.ok("Balance is added for this store ID for today.");

        }
    }



    @Override
    public ResponseEntity<String> addToClosingBalance(@RequestBody Float additionalAmount) {
        balanceService.addToClosingBalance(additionalAmount);
        return ResponseEntity.ok("Amount added to closing balance successfully!!!");
    }
    @Override
    public ResponseEntity<String> subtractFromClosingBalance(@RequestBody Float closingBalance) {
        balanceService.subtractFromClosingBalance(closingBalance);
        return ResponseEntity.ok("Amount subtracted from closing balance successfully!!!");
    }
    @Override
    public ResponseEntity<Float> getTotalClosingBalance() {
        LocalDate presentDay = LocalDate.now();
        Float closingBalance = balanceService.getClosingBalanceForDay(presentDay);
        return ResponseEntity.ok(closingBalance);
    }
    @Override
    public ResponseEntity<List<BalanceWithPaymentSummaryResponse>> getAllBalancesWithPaymentSummaries() {
        List<BalanceWithPaymentSummaryResponse> balancesWithSummaries = balanceService
                .getAllBalancesWithPaymentSummaries();
        return ResponseEntity.ok(balancesWithSummaries);
    }
    @Override
    public ResponseEntity<Double> getCurrentDateClosingBalanceByStoreId(@PathVariable Integer store_id) {
        LocalDate currentDate = LocalDate.now();
        Double closingBalance = balanceService.getClosingBalanceForDateAndStoreId(currentDate, store_id);

        if (closingBalance == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(closingBalance);
    }
    @Override
    public ResponseEntity<List<Balance>> getBalanceByStoreId(@PathVariable Integer storeId) {
        List<Balance> balances = balanceService.getBalanceByStoreId(storeId);
        if (balances.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(balances);
    }

    @Override
    public ResponseEntity<String> addBalance(@RequestBody BalanceRequest balanceRequest) {
        try {
            // Find the existing Balance entity based on some criteria (e.g., store_id, date)
            Balance balance = balanceRepository.findByStoreIdAndDate(balanceRequest.getStoreId(), LocalDate.now());

            // System.out.println(balance);
            if (balance == null) {
                // If no existing entry is found, create a new one
                balance = new Balance();
                // balance.setDate(LocalDate.now());
                balance.setStoreId(balanceRequest.getStoreId());
                // Set the initial balance to 0 if it doesn't exist
                balance.setFinalAmount(0.0);
            }

            // Check if a final closing balance is available
            if (balance.getFinalClosingBalance() != null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Error: Final closing balance is already available.");
            }

            double finalAmount = balanceRequest.getFinalAmount();
            double currentBalance = balance.getRemainingBalance();
            if (finalAmount > currentBalance) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Error: Enter amount is greater than remaining amount.");
            }
            balance.setFinalAmount(finalAmount);
            balance.setFinalHandedOverTo(balanceRequest.getFinalHandedOverTo());
            balance.setFinalClosingBalance(currentBalance - finalAmount);
            balance.setStoreId(balanceRequest.getStoreId());
            balance.setDate(balanceRequest.getDate());
            balance.setCreatedDate(balance.getCreatedDate());
            balance.setCreatedBy(balance.getCreatedBy());
            balance.setUpdatedBy(balanceRequest.getUpdatedBy());

            // double remainingBalance = currentBalance - finalAmount;
            // balance.setRemaining_Balance((float) remainingBalance);
            balanceRepository.save(balance);

            return ResponseEntity.ok("Balance added successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error adding balance: " + e.getMessage());
        }
    }

    @Override
    public Number getYesterdayClosingBalanceByStoreId(@PathVariable Integer storeId) {
        LocalDate yesterdayDate = LocalDate.now().minusDays(1);
        List<Balance> yesterdayClosingBalance = balanceRepository.findYesterdayClosingBalanceByStoreId(storeId, yesterdayDate);
        if (yesterdayClosingBalance.get(0).getFinalClosingBalance() != null) {
            return yesterdayClosingBalance.get(0).getFinalClosingBalance();
        } else {
            return yesterdayClosingBalance.get(0).getRemainingBalance();
        }
    }

    //rushikesh code
    @Override
    public Double getRemainingBalance(@PathVariable Integer storeId) {
        LocalDate presentDay = LocalDate.now();
        Balance remainingBalance = balanceRepository.findByStoreIdAndDates(storeId, presentDay);
        balanceService.updateRemainingBalancesForAllStores();
        return remainingBalance.getRemainingBalance() ;
    }


    @Override
    public ResponseEntity<?> generatePDF(
            @RequestParam(required = false) Integer store_id,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") String startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") String endDate) throws DocumentException {
        List<Balance> balancelist;
        Date startDates = null;
        Date endDates = null;
        if (store_id != null) {
            // Fetch payments for a specific store ID
            balancelist = balanceRepository.findByStoreIdAndDateRange(store_id, startDate, endDate);
        } else if (startDate != null && endDate != null) {
            try {
                // Parse date strings into java.util.Date
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                startDates = dateFormat.parse(String.valueOf(startDate));
                endDates = dateFormat.parse(String.valueOf(endDate));
            } catch (ParseException ex) {
                // Handle the parsing error here, e.g., return an error response
                return ResponseEntity.badRequest().body("Invalid date format");
            }

            // Filter the stores based on the date range
            balancelist = balanceRepository.findByStoreIdAndDateRange(store_id, startDate, endDate);
        } else {
            // If no date range is specified, retrieve all stores
            balancelist = balanceRepository.findAll();
        }

        if (balancelist.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(document, byteArrayOutputStream);

        document.open();

        Paragraph title = new Paragraph("CASH REGISTER DETAILS", new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD));
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        Paragraph spacing = new Paragraph(" ");
        spacing.setSpacingAfter(10f);
        document.add(spacing);

        PdfPTable table = new PdfPTable(7);
        table.setWidthPercentage(100);

        PdfPCell cell = new PdfPCell(new Phrase("Date", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Opening Balance", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Cashier", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Remaining Balance", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Handed Over to", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Handed Amount", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Closing Balance", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        table.addCell(cell);

        for (Balance balance : balancelist) {
            table.addCell(String.valueOf(balance.getDate()));
            table.addCell(String.valueOf(balance.getTodays_Opening_Balance()));

//            List<Orders> orders = bill.getOrder();
//            if (!orders.isEmpty()) {
//                table.addCell(orders.get(0).getTblno());
//                table.addCell(orders.get(0).getOrdertype());
//            } else {
//                table.addCell("");
//                table.addCell("");
//            }

            table.addCell(balance.getCreatedBy());
            table.addCell(String.valueOf(balance.getRemainingBalance()));
            table.addCell(String.valueOf(balance.getFinalHandedOverTo()));
            table.addCell(String.valueOf(balance.getFinalAmount()));
            table.addCell(String.valueOf(balance.getFinalClosingBalance()));

        }

        document.add(table);

        document.close();

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=BILL_DETAILS.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(byteArrayInputStream));
    }




}