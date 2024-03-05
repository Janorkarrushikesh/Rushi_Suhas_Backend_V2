package com.syntiaro_pos_system.serviceimpl.v2;

import com.syntiaro_pos_system.entity.v1.Balance;
import com.syntiaro_pos_system.entity.v1.TransactionRecord;
import com.syntiaro_pos_system.entity.v2.ApiResponse;
import com.syntiaro_pos_system.repository.v2.BalanceRepositry;
import com.syntiaro_pos_system.repository.v2.TransactionRepository;
import com.syntiaro_pos_system.service.v2.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    BalanceRepositry balanceRepository;

    @Autowired
    TransactionRepository transactionRepository;


    @Override
    public ResponseEntity<ApiResponse> debiteTransaction(TransactionRecord transactionRecord) {
        try {
            List<Balance> balances = balanceRepository.findAllByStoreIdAndDate(transactionRecord.getStoreId(), LocalDate.now());

            if (balances.get(0).getFinalClosingBalance() != null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse(null, false, "After Final Closing You Not Able To Do Process ", 400));
            }
            if (balances.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse(null, false, "Balance not found for store ID: " + transactionRecord.getStoreId(), 400));
            }

            for (Balance balance : balances) {
                // Calculate the new closing balance by subtracting the given amount
                Double currentClosingBalance = balance.getRemainingBalance();
                Double givenAmount = transactionRecord.getAmount();

                if (currentClosingBalance < givenAmount) {

                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(new ApiResponse(null, false, "Not sufficient balance for end-of-day close.", 400));
                }

                Double newClosingBalance = currentClosingBalance - givenAmount;
                balance.setRemainingBalance(newClosingBalance);
                balanceRepository.save(balance);
            }
            TransactionRecord endOfDayTransaction = new TransactionRecord();
            endOfDayTransaction.setDate(LocalDate.now());
            endOfDayTransaction.setCashier(transactionRecord.getCashier());
            endOfDayTransaction.setExpense(transactionRecord.getExpense());
            endOfDayTransaction.setStoreId(transactionRecord.getStoreId());
            endOfDayTransaction.setStatus(("Debited"));
            endOfDayTransaction.setAmount(transactionRecord.getAmount());
            TransactionRecord transactionRecordList = transactionRepository.save(endOfDayTransaction);
            return ResponseEntity.ok()
                    .body(new ApiResponse(transactionRecordList, true, 200));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(null, false, "", 500));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> transactionByStore(Integer storeId) {
        try {
            List<TransactionRecord> transactionRecordList = transactionRepository.findByStoreId(storeId);
            List<Map<String, Object>> transactionData = new ArrayList<>();
            for (TransactionRecord transactionRecord : transactionRecordList) {
                Map<String, Object> transactionMap = new LinkedHashMap<>();
                transactionMap.put("id", transactionRecord.getId());
                transactionMap.put("date", transactionRecord.getDate());
                transactionMap.put("amount", transactionRecord.getAmount());
                transactionMap.put("cashier", transactionRecord.getCashier());
                transactionMap.put("expense", transactionRecord.getExpense());
                transactionMap.put("status", transactionRecord.getStatus());
                transactionData.add(transactionMap);
            }
            return ResponseEntity.ok()
                    .body(new ApiResponse(transactionData, true, 200));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(null, false, "", 500));
        }
    }

    public List<Map<String, Object>> transactionByStoreAndDate(Integer storeId, LocalDate date) {
        try {
            LocalDate utilDate = Date.valueOf(date).toLocalDate();
            List<TransactionRecord> transactionRecordList = transactionRepository.findByStoreIdAndDate(storeId, utilDate);
            List<Map<String, Object>> transactionData = new ArrayList<>();
            for (TransactionRecord transactionRecord : transactionRecordList) {
                Map<String, Object> transactionMap = new LinkedHashMap<>();
                transactionMap.put("id", transactionRecord.getId());
                transactionMap.put("date", transactionRecord.getDate());
                transactionMap.put("amount", transactionRecord.getAmount());
                transactionMap.put("cashier", transactionRecord.getCashier());
                transactionMap.put("expense", transactionRecord.getExpense());
                transactionMap.put("status", transactionRecord.getStatus());
                transactionData.add(transactionMap);
            }
            return transactionData;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
