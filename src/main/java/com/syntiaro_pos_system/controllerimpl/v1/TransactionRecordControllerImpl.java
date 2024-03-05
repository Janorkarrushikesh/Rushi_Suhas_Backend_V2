package com.syntiaro_pos_system.controllerimpl.v1;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.syntiaro_pos_system.controller.v1.TransactionRecordController;
import com.syntiaro_pos_system.entity.v1.Balance;
import com.syntiaro_pos_system.entity.v1.TransactionRecord;
import com.syntiaro_pos_system.repository.v1.BalanceRepository;
import com.syntiaro_pos_system.repository.v1.TransactionRecordRepository;
import com.syntiaro_pos_system.request.v1.TransactionRequest;
import com.syntiaro_pos_system.serviceimpl.v1.TransactionRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class TransactionRecordControllerImpl implements TransactionRecordController {

    @Autowired
    TransactionRecordRepository transactionRecordRepository;

    @Autowired
    TransactionRecordService recordService;

    @Autowired
    BalanceRepository balanceRepository;

    @Override
    public ResponseEntity<String> endOfDayClose(@RequestBody TransactionRequest request) {
        try {
            // Fetch a list of balances based on the store ID and the current date
            List<Balance> balances = balanceRepository.findAllByStoreIdAndDate(request.getStore_id(), LocalDate.now());

            if (balances.get(0).getFinalClosingBalance() != null) {
                return ResponseEntity.badRequest().body("After Final Closing You Not Able To Do Process ");
            }
            if (balances.isEmpty()) {
                return ResponseEntity.badRequest().body("Balance not found for store ID: " + request.getStore_id());
            }

            for (Balance balance : balances) {
                // Calculate the new closing balance by subtracting the given amount
                Double currentClosingBalance = balance.getRemainingBalance();
                Float givenAmount = request.getAmount();

                if (currentClosingBalance < givenAmount) {
                    return ResponseEntity.badRequest().body("Not sufficient balance for end-of-day close.");
                }

                Double newClosingBalance = currentClosingBalance - givenAmount;

                // Update the closing balance in the balance table
                balance.setRemainingBalance(newClosingBalance);
                balanceRepository.save(balance);
            }

            // Create a new TransactionRecord object to represent the end-of-day close entry
            TransactionRecord endOfDayTransaction = new TransactionRecord();
            endOfDayTransaction.setDate(LocalDate.now()); // Set the date to the current date
            endOfDayTransaction.setCashier(request.getCashier());
            endOfDayTransaction.setExpense(request.getExpense());
            endOfDayTransaction.setStoreId(request.getStore_id());
            endOfDayTransaction.setStatus(("Debited"));
            endOfDayTransaction.setAmount(Double.valueOf(request.getAmount()));

            // Save the end-of-day transaction entry to the database
            transactionRecordRepository.save(endOfDayTransaction);


            return ResponseEntity.ok("End of day closing successful!!!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error occurred during end-of-day close: " + e.getMessage());
        }
    }


    /*------------------------CHANGES MADE BY TRUPTI--------------------------*/
    // THIS METHOD IS USE FOR GET ALL DATA OF TRANSACTION
    @Override
    public ResponseEntity<List<TransactionRecord>> getAllTransactions() {
        try {
            List<TransactionRecord> transactions = transactionRecordRepository.findAll();
            return ResponseEntity.ok(transactions);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }

    }

    /*------------------------CHANGES MADE BY TRUPTI--------------------------*/
    // THIS METHOD IS USE FOR GET DATA OF TRANSACTION BY STOREID
    @Override
    public ResponseEntity<List<TransactionRecord>> getTransactionByStoreId(@PathVariable Integer storeId) {
        List<TransactionRecord> transactionRecords = recordService.getTransactioneByStoreId(storeId);
        if (transactionRecords.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(transactionRecords);
    }


    @Override
    public ResponseEntity<?> generatePDF(
            @RequestParam(required = false) Integer store_id,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) throws DocumentException {

        List<TransactionRecord> transactionlist;
        List<Balance> balancelist;
        Date startDates = null;
        Date endDates = null;

        if (store_id != null) {
            // Fetch payments for a specific store ID
            transactionlist = transactionRecordRepository.findByStoreIdAndDateRange(store_id, startDate, endDate);
            // Fetch payments for a specific store ID
            balancelist = balanceRepository.findByStoreIdAndDateRange(store_id, startDate, endDate);

        } else if (startDate != null && endDate != null) {
            try {
                // Parse date strings into java.util.Date
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                startDates = dateFormat.parse(startDate);
                endDates = dateFormat.parse(endDate);
            } catch (ParseException ex) {
                // Handle the parsing error here, e.g., return an error response
                return ResponseEntity.badRequest().body("Invalid date format");
            }

            // Filter the stores based on the date range
            transactionlist = transactionRecordRepository.findByStoreIdAndDateRange(store_id, startDate, endDate);
            balancelist = balanceRepository.findByStoreIdAndDateRange(store_id, startDate, endDate);
        } else {
            // If no date range is specified, retrieve all stores
            transactionlist = transactionRecordRepository.findAll();
            balancelist = balanceRepository.findAll();
        }

        if (transactionlist.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        if (balancelist.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(document, byteArrayOutputStream);

        document.open();

        Paragraph title = new Paragraph("CASH REGISTER DETAILS DETAILS", new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD));
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        Paragraph spacing = new Paragraph(" ");
        spacing.setSpacingAfter(10f);
        document.add(spacing);

        PdfPTable table = new PdfPTable(10);
        table.setWidthPercentage(100);

        PdfPCell cell = new PdfPCell(new Phrase("Sr No", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Date", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Opening Balance", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Expense", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Amount", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Status", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Remaining Balance", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Handed Over to", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Handed Amount", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Closing Balance", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        table.addCell(cell);

        List<Object> combinedList = new ArrayList<>();
        combinedList.addAll(transactionlist);
        combinedList.addAll(balancelist);
        int serialNumber = 1;

        for (Object item : combinedList) {
            if (item instanceof Balance balance) {

                // Find matching TransactionRecords for the current Balance based on date
                List<TransactionRecord> matchingTransactions = transactionlist.stream()
                        .filter(transaction -> balance.getCreatedDate().equals(transaction.getCreatedDate()))
                        .collect(Collectors.toList());

                // Add data to the table for the matched dates
                table.addCell(String.valueOf(serialNumber++));
                table.addCell(String.valueOf(balance.getDate()));
                table.addCell(String.format("%.2f", balance.getTodays_Opening_Balance()));

                // If there are matching TransactionRecords, concatenate their data into a single cell
                if (!matchingTransactions.isEmpty()) {
                    StringBuilder expensesInfo = new StringBuilder();
                    StringBuilder amountsInfo = new StringBuilder();
                    StringBuilder statusesInfo = new StringBuilder();

                    for (TransactionRecord matchingTransaction : matchingTransactions) {
                        // Adjust the order of data based on your requirement
                        expensesInfo.append(matchingTransaction.getExpense()).append("\n");
                        amountsInfo.append(matchingTransaction.getAmount()).append("\n");
                        statusesInfo.append(matchingTransaction.getStatus()).append("\n");
                    }
                    // Add expenses, amounts, and statuses into separate cells
                    table.addCell(expensesInfo.toString());
                    table.addCell(amountsInfo.toString());
                    table.addCell(statusesInfo.toString());
                } else {
                    // If no matching TransactionRecord, add empty cells
                    table.addCell("");
                    table.addCell("");
                    table.addCell("");
                }

                // Add remaining data
                table.addCell(String.format("%.2f", balance.getRemainingBalance()));
                table.addCell(String.valueOf(balance.getFinalHandedOverTo()));
                table.addCell(String.valueOf(balance.getFinalAmount()));
                table.addCell(String.format("%.2f", balance.getFinalClosingBalance()));

            } else if (item instanceof TransactionRecord transaction) {
                // This block is for TransactionRecord only, as you're handling Balance separately

                // Check if there is a matching Balance for the current TransactionRecord based on date
                Balance matchingBalance = balancelist.stream()
                        .filter(balance -> transaction.getCreatedDate().equals(balance.getCreatedDate()))
                        .findFirst()
                        .orElse(null);
            }
        }

        document.add(table);

        document.close();

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=TRANSACTION DETAILS.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(byteArrayInputStream));
    }


    @Override
    public ResponseEntity<?> generatePDFbyStoreid(
            @PathVariable Integer store_id) throws DocumentException {

        List<TransactionRecord> transactionlist;
        List<Balance> balancelist;
        Date startDates = null;
        Date endDates = null;

        if (store_id != null) {
            // Fetch payments for a specific store ID
            transactionlist = transactionRecordRepository.findByStoreid(store_id);
            // Fetch payments for a specific store ID
            balancelist = balanceRepository.findByStoreid(store_id);

        } else {
            // If no date range is specified, retrieve all stores
            transactionlist = transactionRecordRepository.findAll();
            balancelist = balanceRepository.findAll();
        }

        if (transactionlist.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        if (balancelist.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(document, byteArrayOutputStream);

        document.open();

        Paragraph title = new Paragraph("CASH REGISTER DETAILS DETAILS", new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD));
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        Paragraph spacing = new Paragraph(" ");
        spacing.setSpacingAfter(10f);
        document.add(spacing);

        PdfPTable table = new PdfPTable(9);
        table.setWidthPercentage(100);

        PdfPCell cell = new PdfPCell(new Phrase("Date", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Opening Balance", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Expense", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Amount", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Status", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Remaining Balance", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Handed Over to", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Handed Amount", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Closing Balance", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        table.addCell(cell);

        List<Object> combinedList = new ArrayList<>();
        combinedList.addAll(transactionlist);
        combinedList.addAll(balancelist);
        for (Object item : combinedList) {
            if (item instanceof Balance balance) {

                // Find matching TransactionRecords for the current Balance based on date
                List<TransactionRecord> matchingTransactions = transactionlist.stream()
                        .filter(transaction -> balance.getCreatedDate().equals(transaction.getCreatedDate()))
                        .collect(Collectors.toList());

                // Add data to the table for the matched dates
                table.addCell(String.valueOf(balance.getDate()));
                table.addCell(String.format("%.2f", balance.getTodays_Opening_Balance()));


                // If there are matching TransactionRecords, concatenate their data into a single cell
                if (!matchingTransactions.isEmpty()) {
                    StringBuilder expensesInfo = new StringBuilder();
                    StringBuilder amountsInfo = new StringBuilder();
                    StringBuilder statusesInfo = new StringBuilder();

                    for (TransactionRecord matchingTransaction : matchingTransactions) {
                        // Adjust the order of data based on your requirement
                        expensesInfo.append(matchingTransaction.getExpense()).append("\n");
                        amountsInfo.append(matchingTransaction.getAmount()).append("\n");
                        statusesInfo.append(matchingTransaction.getStatus()).append("\n");
                    }
                    // Add expenses, amounts, and statuses into separate cells
                    table.addCell(expensesInfo.toString());
                    table.addCell(amountsInfo.toString());
                    table.addCell(statusesInfo.toString());
                } else {
                    // If no matching TransactionRecord, add empty cells
                    table.addCell("");
                    table.addCell("");
                    table.addCell("");
                }

                // Add remaining data
                table.addCell(String.format("%.2f", balance.getRemainingBalance()));
                table.addCell(String.valueOf(balance.getFinalHandedOverTo()));
                table.addCell(String.valueOf(balance.getFinalAmount()));
                table.addCell(String.format("%.2f", balance.getFinalClosingBalance()));

            } else if (item instanceof TransactionRecord transaction) {
                // This block is for TransactionRecord only, as you're handling Balance separately

                // Check if there is a matching Balance for the current TransactionRecord based on date
                Balance matchingBalance = balancelist.stream()
                        .filter(balance -> transaction.getCreatedDate().equals(balance.getCreatedDate()))
                        .findFirst()
                        .orElse(null);
            }
        }

        document.add(table);

        document.close();

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=TRANSACTION DETAILS.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(byteArrayInputStream));
    }


}