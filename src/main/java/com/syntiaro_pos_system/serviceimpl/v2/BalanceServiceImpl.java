package com.syntiaro_pos_system.serviceimpl.v2;

import com.syntiaro_pos_system.entity.v1.Store;
import com.syntiaro_pos_system.entity.v1.TransactionRecord;
import com.syntiaro_pos_system.entity.v2.ApiResponse;
import com.syntiaro_pos_system.entity.v1.Balance;
import com.syntiaro_pos_system.repository.v2.BalanceRepositry;
import com.syntiaro_pos_system.repository.v2.TransactionRepositry;
import com.syntiaro_pos_system.service.v2.BalanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BalanceServiceImpl implements BalanceService {

    @Autowired
    BalanceRepositry balanceRepository;

    @Autowired
    TransactionRepositry transactionRepositry;

    @Autowired
    BillServiceImplV2 billServiceImplV2;

    @Autowired
    TransactionServiceImpl transactionServiceimpl;

    @Override
    public ResponseEntity<ApiResponse> openBalance(Balance balance) {
        try{

        if (balance == null) {
            return ResponseEntity.badRequest().body(new ApiResponse(null, false, "Invalid request", 400));
        }
        LocalDate today = LocalDate.now();
        Balance existingBalance = balanceRepository.findByStoreIdAndDate(balance.getStoreId(), today);
        Balance newBalance = new Balance();
        if (existingBalance == null) {
            Double addMoreAmount = Double.valueOf(balance.getAddMoreAmount());
            if (addMoreAmount == null) {
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                        .body(new ApiResponse(null, false, "Invalid addMoreAmount value", 406));
            }
            newBalance.setAddMoreAmount(addMoreAmount);
            newBalance.setRemainingBalance(addMoreAmount);
            newBalance.setStoreId(balance.getStoreId());
            newBalance.setTodays_Opening_Balance(addMoreAmount);
            newBalance.setCreatedBy(balance.getCreatedBy());
            newBalance.setUpdatedBy(balance.getUpdatedBy());
            newBalance.setCreatedDate(String.valueOf(LocalDateTime.now()));
            newBalance.setUpdatedDate(String.valueOf(LocalDateTime.now()));
            balanceRepository.save(newBalance);

            return ResponseEntity.ok(new ApiResponse(newBalance, true, 200));
        } else if (existingBalance.getAddMoreAmount() != null) {

            if (existingBalance.getFinalClosingBalance() != null) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse(null, false, " After Final closing you dont have access", 400));
            }
            Double addMoreAmount = balance.getAddMoreAmount();
            if (addMoreAmount == null) {
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                        .body(new ApiResponse(null, false, "Invalid addMoreAmount value", 406));
            }
            newBalance.setId(existingBalance.getId());
            newBalance.setStoreId(balance.getStoreId());
            newBalance.setAddMoreAmount(addMoreAmount + existingBalance.getAddMoreAmount());
            newBalance.setRemainingBalance(addMoreAmount + existingBalance.getRemainingBalance());
            newBalance.setTodays_Opening_Balance(existingBalance.getTodays_Opening_Balance());
            newBalance.setUpdatedDate(String.valueOf(LocalDateTime.now()));
            newBalance.setCreatedDate(existingBalance.getCreatedDate());
            newBalance.setCreatedBy(existingBalance.getCreatedBy());
            newBalance.setUpdatedBy(balance.getUpdatedBy());
            balanceRepository.save(newBalance);

            TransactionRecord endOfDayTransaction = new TransactionRecord();
            endOfDayTransaction.setDate(LocalDate.now()); // Set the date to the current date
            endOfDayTransaction.setCashier(balance.getCreatedBy());
            endOfDayTransaction.setStoreId(balance.getStoreId());
            endOfDayTransaction.setStatus(("Credited"));
            endOfDayTransaction.setExpense(balance.getCreatedBy());
            endOfDayTransaction.setAmount(balance.getAddMoreAmount());
            transactionRepositry.save(endOfDayTransaction);

            return ResponseEntity.ok(new ApiResponse(endOfDayTransaction, true, 200));
        } else {

            if (existingBalance.getFinalClosingBalance() != null) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse(null, false, " After Final closing you dont have access", 400));
            }

            Double addMoreAmount = balance.getAddMoreAmount();
            if (addMoreAmount == null) {
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                        .body(new ApiResponse(null, false, "Invalid addMoreAmount value", 406));
            }
            newBalance.setId(existingBalance.getId());
            newBalance.setStoreId(balance.getStoreId());
            newBalance.setAddMoreAmount(addMoreAmount);
            newBalance.setRemainingBalance(addMoreAmount + existingBalance.getRemainingBalance());
            newBalance.setTodays_Opening_Balance(existingBalance.getTodays_Opening_Balance());
            newBalance.setUpdatedDate(String.valueOf(LocalDateTime.now()));
            newBalance.setCreatedDate(existingBalance.getCreatedDate());
            newBalance.setCreatedBy(existingBalance.getCreatedBy());
            newBalance.setUpdatedBy(balance.getUpdatedBy());
            balanceRepository.save(newBalance);

            TransactionRecord endOfDayTransaction = new TransactionRecord();
            endOfDayTransaction.setDate(LocalDate.now()); // Set the date to the current date
            endOfDayTransaction.setCashier(balance.getCreatedBy());
            endOfDayTransaction.setStoreId(balance.getStoreId());
            endOfDayTransaction.setStatus(("Credited"));
            endOfDayTransaction.setExpense(balance.getCreatedBy());
            endOfDayTransaction.setAmount(balance.getAddMoreAmount());

            transactionRepositry.save(endOfDayTransaction);
            return ResponseEntity.ok(new ApiResponse(endOfDayTransaction, true, 200));
        }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(null, false, "...", 500));

        }
    }


    // plesae check this method
    @Override
    public ResponseEntity<ApiResponse> finalClosingBalance(Balance balance1) {
        try {
            Balance balance = balanceRepository.findByStoreIdAndDate(balance1.getStoreId(), LocalDate.now());
            if (balance == null) {
                balance = new Balance();
                balance.setStoreId(balance1.getStoreId());
                balance.setFinalAmount(0.0);
            }
            if (balance.getFinalClosingBalance() != null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse(null, false, "Error: Final closing balance is already available.", 400));
            }
            double finalAmount = balance.getFinalAmount();
            double currentBalance = balance.getRemainingBalance();
            if (finalAmount > currentBalance) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse(null, false, "Error: Enter amount is greater than remaining amount.", 400));
            }
            balance.setFinalAmount(finalAmount);
            balance.setFinalHandedOverTo(balance.getFinalHandedOverTo());
            balance.setFinalClosingBalance(currentBalance - finalAmount);
            balance.setStoreId(balance.getStoreId());
            balance.setDate(balance.getDate());
            balance.setCreatedDate(balance.getCreatedDate());
            balance.setCreatedBy(balance.getCreatedBy());
            balance.setUpdatedBy(balance.getUpdatedBy());
            Balance data = balanceRepository.save(balance);
            return ResponseEntity.ok(new ApiResponse(data, true, 200));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(null, false, "..." , 500));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> YesterdayClosingBalance(Integer storeId) {
        try {
            LocalDate yesterdayDate = LocalDate.now().minusDays(1);
            Balance balance = balanceRepository.findByStoreIdAndDate(storeId, yesterdayDate);
            if (balance != null) {
                return ResponseEntity.ok(new ApiResponse(balance.getRemainingBalance(), true, 200));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse(null, false, "Yesterday Closing Balance Not Found", 404));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(null, false,"...", 500));

        }
    }

    @Override
    public ResponseEntity<ApiResponse> RemainingBalance(Integer storeId) {
        try {
            Balance balance = balanceRepository.findByStoreIdAndDate(storeId, LocalDate.now());
            if (balance != null) {
                return ResponseEntity.ok(new ApiResponse(balance.getRemainingBalance(), true, 200));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse(null, false, " Balance Not Found", 404));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(null, false,"...", 500));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> balanceList(Integer storeId) {
        try{

            List<Balance> balanceList = balanceRepository.findByStoreId(storeId);
            System.out.println(balanceList);
            List<Map<String,Object>> balanceData = new ArrayList<>();

            for (Balance balance : balanceList) {
                Map<String,Object> balanceMap = new LinkedHashMap<>();
                balanceMap.put("date",balance.getDate());
                balanceMap.put("openingBalance",String.format("%.2f",balance.getTodays_Opening_Balance()));
                balanceMap.put("sales",billServiceImplV2.TotalwithDate(balance.getStoreId(),balance.getDate()));
                balanceMap.put("remainingBalance",String.format("%.2f",balance.getRemainingBalance()));
                balanceMap.put("handedOver",balance.getFinalHandedOverTo());
                balanceMap.put("handedAmonut",balance.getFinalAmount());
                balanceMap.put("closingBalance",balance.getFinalClosingBalance());
                balanceMap.put("transction",transactionServiceimpl.transactionByStoreAndDate(balance.getStoreId(),balance.getDate()));
                balanceData.add(balanceMap);

            }


            return ResponseEntity.ok().body(new ApiResponse(balanceData,true,200));

        }
        catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(null,false,"...",500));
        }
    }


//    @Scheduled(cron = "0 0 0 * * *")
//    public void updateRemainingBalancesForAllStores() {
//        LocalDateTime currentDate = LocalDateTime.now();
//        List<Store> stores = storeRepository.findAll();
//        List<Long> storeIds = stores.stream()
//                .map(Store::getStoreid)
//                .collect(Collectors.toList());
//        for (Long storeId : storeIds) {
//            // Get the last recorded balance for the specific store
//            //Balance lastBalance = balanceRepository.findTopByStoreIdForYesterday(Math.toIntExact(storeId));
//            LocalDate yesterdayDate = LocalDate.now().minusDays(1);
//            List<Balance> lastBalance = balanceRepository.findYesterdayClosingBalanceByStoreId(Math.toIntExact(storeId), yesterdayDate);
//            Balance currentBalance = balanceRepository.findByStoreIdAndToday(Math.toIntExact(storeId), LocalDate.now());
//            if(currentBalance == null){
//                if (lastBalance != null && !lastBalance.get(0).getDate().equals(currentDate)) {
//                    if(lastBalance.get(0).getFinalClosingBalance() != null){
//                        double todaysOpeningBalance = lastBalance.get(0).getFinalClosingBalance();
//                        double remainingBalance = lastBalance.get(0).getFinalClosingBalance() ;
//                        // Create a new balance entry for the current day
//                        Balance newBalance = new Balance();
//                        newBalance.setDate(LocalDate.from(currentDate));
//                        newBalance.setTodays_Opening_Balance(todaysOpeningBalance);
//                        newBalance.setRemainingBalance( remainingBalance);
//                        newBalance.setStoreId(Math.toIntExact(storeId)); // Set the current store ID
//                        newBalance.setUpdatedDate(String.valueOf(LocalDateTime.now()));
//                        newBalance.setCreatedDate(String.valueOf(LocalDateTime.now()));
//
//                        balanceRepository.save(newBalance);
//                    }
//                    else {
//
//                        Double todaysOpeningBalance = lastBalance.get(0).getRemainingBalance();
//                        Double remainingBalance = lastBalance.get(0).getRemainingBalance();
//
//                        // Create a new balance entry for the current day
//                        Balance newBalance = new Balance();
//                        newBalance.setDate(LocalDate.from(currentDate));
//                        newBalance.setTodays_Opening_Balance(todaysOpeningBalance);
//                        newBalance.setRemainingBalance(remainingBalance);
//                        newBalance.setStoreId(Math.toIntExact(storeId)); // Set the current store ID
//
//                        balanceRepository.save(newBalance);
//                    }
//                }
//            }
//        }
//    }

}
