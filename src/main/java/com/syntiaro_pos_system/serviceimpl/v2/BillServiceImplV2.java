package com.syntiaro_pos_system.serviceimpl.v2;


import com.syntiaro_pos_system.entity.v1.Balance;
import com.syntiaro_pos_system.entity.v1.Bill;
import com.syntiaro_pos_system.entity.v1.OrderFood;
import com.syntiaro_pos_system.entity.v1.Orders;
import com.syntiaro_pos_system.entity.v2.ApiResponse;
import com.syntiaro_pos_system.exception.v2.EntityNotFoundExceptionById;
import com.syntiaro_pos_system.repository.v2.BalanceRepositry;
import com.syntiaro_pos_system.repository.v2.BillRepositry;
import com.syntiaro_pos_system.repository.v2.OrderRepository;
import com.syntiaro_pos_system.request.v1.BillRequest;
import com.syntiaro_pos_system.service.v2.Billservice;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;




@Service
public class BillServiceImplV2 implements Billservice {

    private static final Logger logger = Logger.getLogger(BillServiceImplV2.class);
    @Autowired
    BillRepositry billRepositry;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    BalanceRepositry balanceRepositry;

    @Override
    public ResponseEntity<ApiResponse> saveBillAndOrder(BillRequest billRequest) {
        try {
            LocalDate billdate = LocalDate.now();
            Integer lastBillId = billRepositry.findLastBillNumberForStore(billRequest.getBill().getStoreId(), billdate);
            billRequest.getBill().setBillId(lastBillId != null ? lastBillId + 1 : 1);
            Integer lastOrderId = orderRepository.findLastOrderNumberForStore(billRequest.getBill().getStoreId(), billdate);
            billRequest.getBill().getOrder().get(0).setOrderId(lastOrderId != null ? lastOrderId + 1 : 1);
            Bill savedBill =  billRepositry.save(billRequest.getBill());
            return ResponseEntity.ok().body(new ApiResponse(savedBill, true, 200));
        } catch (Exception e) {
            logger.debug(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(null, false, "...", 500));
        }

    }


    @Override
    public ResponseEntity<ApiResponse> getBillBySerialNo(Integer serialNo) {
        try {
            Optional<Bill> existingBill = billRepositry.findById(serialNo);
            if(existingBill.isPresent()){
                return ResponseEntity.ok().body(new ApiResponse(existingBill, true, 200));
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(null, false,"Id not found ",404));



        } catch (Exception e) {
            logger.debug(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(null, false, "...",500));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> getBillByStoreId(Integer storeId) {
        try {
            List<Bill> billList = billRepositry.findByStoreId(storeId);
            if (!billList.isEmpty()){
                return ResponseEntity.ok().body(new ApiResponse(billList, true, 200));
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(null, false,"Store id not found ", 404));

        } catch (Exception e) {
            logger.debug(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(null, false, "...",500));
        }

    }


    @Override
    public ResponseEntity<ApiResponse> updateBillBySerialNo(Integer serialNo, Bill bill) {

        try {
        Bill billList = billRepositry.findById(serialNo)
                .orElseThrow(() -> new EntityNotFoundExceptionById("Invalid Id was provided"));
            billList.setBillDate(bill.getBillDate());
            billList.setBillStatus(bill.getBillStatus());
            billList.setContact(bill.getContact());
            billList.setDiscount(bill.getDiscount());
            billList.setPaymentMode(bill.getPaymentMode());
            billList.setOrder(bill.getOrder());
            return ResponseEntity.ok().body(new ApiResponse(  billRepositry.save(billList), true, 200));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(null, false, "...",500));
        }

    }

    @Override // Filter Response
    public ResponseEntity<ApiResponse> fetchBillByStoreId(Integer storeId , Integer page , Integer size , LocalDate startDate , LocalDate endDate) {

        try {
            List<Bill> existingBill = billRepositry.findByStoreId(storeId);
            if (!existingBill.isEmpty()) {
                if (startDate != null && endDate != null) {
                    return ResponseEntity.ok().body(new ApiResponse(getBillByStartDateAndEndDate(storeId, startDate, endDate), true, 200));
                } else if (size != null) {
                    return ResponseEntity.ok().body(new ApiResponse(getBillByPageAndSize(storeId, page, size), true, 200));
                } else {
                    return ResponseEntity.ok().body(new ApiResponse(existingBill, true, 200));
                }
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(null, false, "Store Id Not Found ",404));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(null, false, "...",500));
        }


    }

    // By page and Size
    public  List<Map<String, Object>> getBillByPageAndSize(Integer storeId , Integer page , Integer size ){
        Pageable pageable = PageRequest.of(page, size);
        Page<Bill> billList = billRepositry.findBillByStoreId(storeId, pageable);

        List<Map<String, Object>> billData = new ArrayList<>();
        if (billList != null) {
            Map<String, Object> billMap = null;
            for (Bill bill : billList) {
                billMap = new LinkedHashMap<>();
                billMap.put("serialNumber", bill.getSerialNo());
                billMap.put("id", bill.getBillId());
                billMap.put("billDate", bill.getBillDate());
                billMap.put("billStatus", bill.getBillStatus());
                billMap.put("discount", bill.getDiscount());
                billMap.put("total", bill.getTotal());
                billMap.put("payment", bill.getPaymentMode());
                billMap.put("storeId", bill.getStoreId());

                List<Map<String, Object>> orderData = new ArrayList<>();
                for (Orders orders : bill.getOrder()) {
                    Map<String, Object> orderMap = new LinkedHashMap<>();
                    orderMap.put("id", orders.getOrderId());
                    orderMap.put("orderDate", orders.getOrderDate());
                    orderMap.put("tableNumber", orders.getTableNo());
                    orderMap.put("orderStatus", orders.getOrderStatus());
                    orderMap.put("orderType", orders.getOrderType());

                    List<Map<String, Object>> orderFoodData = new ArrayList<>();
                    for (OrderFood orderFood : orders.getOrderFoods()) {
                        Map<String, Object> orderFoodMap = new LinkedHashMap<>();
                        orderFoodMap.put("foodName", orderFood.getFoodId());
                        orderFoodMap.put("category", orderFood.getCategory());
                        orderFoodMap.put("quantity", orderFood.getQuantity());
                        orderFoodMap.put("price", orderFood.getPrice());
                        orderFoodData.add(orderFoodMap);
                    }
                    orderMap.put("orderFood", orderFoodData);
                    orderData.add(orderMap);
                }
                billMap.put("order", orderData);
                billData.add(billMap);
            }
        }
        return billData;

    }
    //////////////////////////////////////////////
 public List<Map<String, Object>> getBillByStartDateAndEndDate(Integer storeId , LocalDate startDate , LocalDate endDate){
     List<Bill> billList = billRepositry.findBillByStoreIds(storeId, startDate, endDate);
     List<Map<String, Object>> billData = new ArrayList<>();
     if (billList != null) {
         Map<String, Object> billMap = null;
         for (Bill bill : billList) {
             billMap = new LinkedHashMap<>();
             billMap.put("serialNumber", bill.getSerialNo());
             billMap.put("id", bill.getBillId());
             billMap.put("billDate", bill.getBillDate());
             billMap.put("billStatus", bill.getBillStatus());
             billMap.put("discount", bill.getDiscount());
             billMap.put("total", bill.getTotal());
             billMap.put("payment", bill.getPaymentMode());
             billMap.put("storeId", bill.getStoreId());
             List<Map<String, Object>> orderData = new ArrayList<>();
             for (Orders orders : bill.getOrder()) {
                 Map<String, Object> orderMap = new LinkedHashMap<>();
                 orderMap.put("id", orders.getOrderId());
                 orderMap.put("orderDate", orders.getOrderDate());
                 orderMap.put("tableNumber", orders.getTableNo());
                 orderMap.put("orderStatus", orders.getOrderStatus());
                 orderMap.put("orderType", orders.getOrderType());
                 List<Map<String, Object>> orderFoodData = new ArrayList<>();
                 for (OrderFood orderFood : orders.getOrderFoods()) {
                     Map<String, Object> orderFoodMap = new LinkedHashMap<>();
                     orderFoodMap.put("foodName", orderFood.getFoodId());
                     orderFoodMap.put("category", orderFood.getCategory());
                     orderFoodMap.put("quantity", orderFood.getQuantity());
                     orderFoodMap.put("price", orderFood.getPrice());
                     orderFoodData.add(orderFoodMap);
                 }
                 orderMap.put("orderFood", orderFoodData);
                 orderData.add(orderMap);
             }
             billMap.put("order", orderData);
             billData.add(billMap);
         }
     }
     return billData;
 }


//////////////////////////////////////////////////////////////////////////////////////////////////////


    @Override
    public ResponseEntity<ApiResponse> quickBill(BillRequest bill) {
        try {
            LocalDate billDate = LocalDate.now();

            LocalDate today = LocalDate.now();
            Balance existingBalance = balanceRepositry.findByStoreIdAndDate(bill.getBill().getStoreId(), today);
            if (existingBalance != null && existingBalance.getFinalClosingBalance() != null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse(null, false, "After Final closing you don't have access", 400));
            }

            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
            String formattedDate = dateFormat.format(date);

            Integer lastBillNumber = billRepositry.findLastBillNumberForStore(bill.getBill().getStoreId(), billDate);
            bill.getBill().setBillId(lastBillNumber != null ? lastBillNumber + 1 : 1);

            String orderId = bill.getBill().getOrder().get(0).getStoreId();
            Integer lastOrderNumber = orderRepository.findLastOrderNumberForStore(bill.getBill().getStoreId(), billDate);
            bill.getBill().getOrder().get(0).setOrderId(lastOrderNumber != null ? lastOrderNumber + 1 : 1);


            Bill savedBill = billRepositry.save(bill.getBill());
            completeOrderAndPlaceBill(savedBill, billDate);
            return ResponseEntity.ok().body(new ApiResponse(savedBill, true, 200));
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(null, false, "...", 500));

        }

    }


    @Override
    public ResponseEntity<ApiResponse> billStatusReport(Integer storeId, String orderStatus) {
        try {
            LocalDate DaysAgo = LocalDate.now().minusDays(150);

            List<Bill> OrderData = billRepositry.findBillsByStoreAndStatusAndDatekot(storeId, DaysAgo,orderStatus);
            return ResponseEntity.ok().body(new ApiResponse(OrderData, true, 200));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(null, false, "", 500));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> calculateTotalByPaymnetMode(Integer storeId) {
      try{
          Date utilDate = java.sql.Date.valueOf(LocalDate.now());
          Map<String, Object> transaction = new LinkedHashMap<>();
          Float totalCashAmount = billRepositry.calculateTotalCardAmountByStoreIdAndDay(storeId, utilDate,"cash");
          Float totalUpiAmount = billRepositry.calculateTotalCardAmountByStoreIdAndDay(storeId, utilDate,"upi");
          Float totalCardAmount = billRepositry.calculateTotalCardAmountByStoreIdAndDay(storeId, utilDate,"card");
          transaction.put("cash",totalCashAmount);
          transaction.put("card",totalCardAmount);
          transaction.put("upi",totalUpiAmount);
          return ResponseEntity.ok().body(new ApiResponse(transaction,true,200));

      }
      catch (Exception e){
          return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                  .body(new ApiResponse(null,false,"...",500));
      }
    }

    @Override
    public ResponseEntity<ApiResponse> balanceReportByPaymentMode(Integer storeId) {
        try{
     Map<String, Map<LocalDate, Float>> cashReport = getDailyBalanceReport(storeId);
            return ResponseEntity.ok().body(new ApiResponse(cashReport,true,200)) ;
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(null,false,"..",500));
        }
    }


    public Map<String, Map<LocalDate, Float>> getDailyBalanceReport(@PathVariable(name = "store_id") Integer storeId) {
        Map<String, Map<LocalDate, Float>> dailyBalanceReports = new HashMap<>();
        try {
            List<Bill> allBills = billRepositry.findByStoreId(storeId);
            for (String paymentMode : Arrays.asList("cash", "card", "upi")) {
                Map<LocalDate, Float> dailyBalances = new HashMap<>();
                allBills.stream()
                        .filter(bill -> paymentMode.equalsIgnoreCase(bill.getPaymentMode()))
                        .forEach(bill -> {
                            LocalDate billDate = bill.getBillDate();
                            Float totalAmount = bill.getTotal();
                            dailyBalances.merge(billDate, totalAmount, Float::sum);
                        });
                Map<LocalDate, Float> sortedDailyBalances = dailyBalances.entrySet().stream()
                        .sorted(Map.Entry.<LocalDate, Float>comparingByKey().reversed())
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));
                dailyBalanceReports.put(paymentMode, sortedDailyBalances);
            }
            dailyBalanceReports = dailyBalanceReports.entrySet().stream()
                    .sorted((entry1, entry2) -> {
                        LocalDate maxDate1 = entry1.getValue().keySet().stream().max(LocalDate::compareTo).orElse(LocalDate.MIN);
                        LocalDate maxDate2 = entry2.getValue().keySet().stream().max(LocalDate::compareTo).orElse(LocalDate.MIN);
                        return maxDate2.compareTo(maxDate1);
                    })
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));
            return dailyBalanceReports;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public void completeOrderAndPlaceBill(Bill bill, LocalDate calculationDate) {
        if (bill.getPaymentMode().equalsIgnoreCase("cash")) {
            Double billAmount = Double.valueOf(bill.getTotal());
            if (billAmount == null) {billAmount = 0.0;}
            Balance balance = balanceRepositry.findByStoreIdAndDate(bill.getStoreId(), calculationDate);
            if (balance == null) {
                balance = new Balance();
                balance.setStoreId(bill.getStoreId());
                balance.setDate(calculationDate);
                balance.setTodays_Opening_Balance(0.0);
                balance.setRemainingBalance(billAmount);
            } else {
                Double currentBalance = balance.getRemainingBalance();
                Double updatedBalance = currentBalance + billAmount;
                balance.setRemainingBalance(updatedBalance);
            }
            balanceRepositry.save(balance);
        }
    }

    public Map<String, Object> TotalwithDate(Integer storeId , LocalDate date) {
        try{
            Date utilDate = java.sql.Date.valueOf(date);
            Map<String, Object> transaction = new LinkedHashMap<>();
            Float totalCashAmount = billRepositry.calculateTotalCardAmountByStoreIdAndDay(storeId, utilDate,"cash");
            Float totalUpiAmount = billRepositry.calculateTotalCardAmountByStoreIdAndDay(storeId, utilDate,"upi");
            Float totalCardAmount = billRepositry.calculateTotalCardAmountByStoreIdAndDay(storeId, utilDate,"card");
            transaction.put("cash",totalCashAmount);
            transaction.put("card",totalCardAmount);
            transaction.put("upi",totalUpiAmount);
            return transaction;

        }
        catch (Exception e){
            return null;

        }
    }


}
