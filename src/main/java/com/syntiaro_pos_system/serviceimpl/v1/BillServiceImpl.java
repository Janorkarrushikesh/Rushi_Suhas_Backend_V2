package com.syntiaro_pos_system.serviceimpl.v1;



import com.syntiaro_pos_system.entity.v1.*;
import com.syntiaro_pos_system.repository.v1.BalanceRepository;
import com.syntiaro_pos_system.repository.v1.BillRepo;
import com.syntiaro_pos_system.service.v1.BillService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Service
public class  BillServiceImpl implements BillService {
    @Autowired
    BillRepo billRepo;

    @Autowired
    BalanceRepository balanceRepository;

    //THIS METHOD IS USE FOR GET ALL LIST OF BILL
    @Override
    public List<Bill> getBill() {
        return billRepo.findAll();
    }

    //THIS METHOD IS USE FOR ADD BILL
    @Override
    public int addBill(Bill bill) {
        billRepo.save(bill);
        return bill.getBillId();
    }

    //THIS METHOD IS USE FOR UPDATE BILL
    @Override
    public Bill updateBill(Bill bill) {
        billRepo.save(bill);
        return bill;
    }


    //THIS METHOD IS USE FOR DELETE BILL
    @Override
    public void deletebill(int parseInt) {
        Bill entity = billRepo.getOne(parseInt);
        billRepo.delete(entity);
    }


    // THIS METHOD IS USE FOR FETCH BILL BY STOREID
    // bug_10 due to change Query show diffrent data
    @Override
    public List<Bill> getBillsByStoreId(Integer storeId) {
        return billRepo.findBySid(storeId);
    }


    @Override
    public Bill findBillById(Integer id) {
        return billRepo.findById(id).orElse(null);
    }

    @Override
    public Bill saveBill(Bill existingBill) {
        return billRepo.save(existingBill);
    }



    // THIS METHOD IS USE FOR FETCH BILL BY ID
    @Override
    public Optional<Bill> getbillbyid(Integer id) {
        return billRepo.findById(id);
    }


    @Override
    public Bill updateBill(Integer id, Bill bill) {
        Optional<Bill> existingBill = billRepo.findById((int) Integer.parseInt(String.valueOf((id))));


        if (existingBill.isPresent()) {
            Bill updatebill = existingBill.get();

            if (bill.getContact() != null) {
                updatebill.setContact(bill.getContact());
            }

            if (bill.getUpdatedBy() != null) {
                updatebill.setUpdatedBy(bill.getUpdatedBy());
            }
            if (bill.getCreatedBy() != null) {
                updatebill.setCreatedBy(bill.getCreatedBy());
            }
            if (bill.getPaymentMode() != null) {
                updatebill.setPaymentMode(bill.getPaymentMode());
            }
            if (bill.getTransactionId() != null) {
                updatebill.setTransactionId(bill.getTransactionId());
            }
            if (bill.getGst() != null) {
                updatebill.setGst(bill.getGst());
            }
            if (bill.getTotal() != null) {
                updatebill.setTotal(bill.getTotal());
            }
            if (bill.getStoreId() != null) {
                updatebill.setStoreId(bill.getStoreId());
            }
            if (bill.getOrder() != null) {
                updatebill.setOrder(bill.getOrder());
            }
            if (bill.getDiscount() != null) {
                updatebill.setDiscount(bill.getDiscount());
            }
            if(bill.getBillDate() != null){
                updatebill.setBillDate(LocalDate.now());
            }

            billRepo.save(updatebill);
            return updatebill;
        } else {
            return null;
        }
    }


    //THIS METHOD IS USE FOR CALCULATE TOTAL CASH BY STOREID
    public Float calculateTotalCashAmountByStoreIdAndDay(Integer storeId, LocalDate day) {
        // Convert LocalDate to Date
        Date utilDate = java.sql.Date.valueOf(day); // Convert LocalDate to java.util.Date
        Float totalCashAmount = billRepo.calculateTotalCashAmountByStoreIdAndDay(storeId, utilDate);
        return totalCashAmount != null ? totalCashAmount : 0.0f;
    }


    //THIS METHOD IS USE FOR CALCULATE TOTAL UPI BY STOREID
    public Float calculateTotalUpiAmountByStoreIdAndDay(Integer storeId, LocalDate day) {
        Date utilDate = java.sql.Date.valueOf(day); // Convert LocalDate to java.util.Date
        Float totalUpiAmount = billRepo.calculateTotalUpiAmountByStoreIdAndDay(storeId, utilDate);
        return totalUpiAmount != null ? totalUpiAmount : 0.0f;
    }


    //THIS METHOD IS USE FOR CALCULATE TOTAL CARD BY STOREID
    public Float calculateTotalCardAmountByStoreIdAndDay(Integer storeId, LocalDate day) {
        Date utilDate = java.sql.Date.valueOf(day); // Convert LocalDate to java.util.Date
        Float totalCardAmount = billRepo.calculateTotalCardAmountByStoreIdAndDay(storeId, utilDate);
        return totalCardAmount != null ? totalCardAmount : 0.0f;
    }


    @Override
    public void completeOrderAndPlaceBill(Bill bill, LocalDate calculationDate) {
        // Logic to mark order as completed and place the bill

        // Check if the payment method is "cash"
        if (bill.getPaymentMode().equalsIgnoreCase("cash")) {
            // Calculate bill amount
            Double billAmount = Double.valueOf(bill.getTotal());

            if (billAmount == null) {
                billAmount = 0.0; // You can choose a suitable default value
            }

            Balance balance = balanceRepository.findByStoreIdAndDate(bill.getStoreId(), calculationDate);

            if (balance == null) {
                balance = new Balance();
                balance.setStoreId(bill.getStoreId());
                balance.setDate(calculationDate);
                balance.setTodays_Opening_Balance(0.0);
                balance.setRemainingBalance(billAmount);
            } else {
                Double currentBalance = balance.getRemainingBalance();

                // Update closing balance by adding bill amount
                Double updatedBalance = currentBalance + billAmount;
                balance.setRemainingBalance(updatedBalance);
                //new code added

            }

            // Save the balance in the database (create new or update existing)
            balanceRepository.save(balance);

            // Now you can display the updated closing balance
            System.out.println("Updated Closing Balance for Store " + balance.getStoreId() + " on " + calculationDate + ": " + balance.getRemainingBalance());
        } else {
            System.out.println("Payment method is not 'cash', skipping balance update.");
        }
    }

    // billreport pagination code   ---- by rushikesh
    @Override
    public ApiResponse getBillsReport(Integer storeId, Integer page, Integer size) {
        ApiResponse emptyResponse = null;

        Pageable pageable = PageRequest.of(page, size);
        Page<Bill> result = billRepo.findBillsByStoreId(storeId, pageable);

        if (result.isEmpty()) {
            return new ApiResponse(null, false, 200);
        }

        // Create a Map for the "data" field
        Map<String, Object> data = new HashMap<>();
        data.put("totalCount", String.valueOf(result.getTotalElements()));

        List<Map<String, Object>> billDetailsList = new ArrayList<>();
        for (Bill bill : result.getContent()) {
            Map<String, Object> billDetailsMap = new HashMap<>();
            billDetailsMap.put("id", bill.getBillId());
            billDetailsMap.put("billdate", bill.getBillDate());
            billDetailsMap.put("contact", bill.getContact());
            billDetailsMap.put("paymentmode", bill.getPaymentMode());
            billDetailsMap.put("discount", bill.getDiscount());
            billDetailsMap.put("total", bill.getTotal());
            billDetailsMap.put("store_id", bill.getStoreId());
            billDetailsMap.put("serial_no", bill.getSerialNo());

            // Create Order list
            List<Map<String, Object>> orderList = new ArrayList<>();
            for (Orders order : bill.getOrder()) {
                Map<String, Object> orderMap = new HashMap<>();
                orderMap.put("oid", order.getOrderId());
                orderMap.put("orddate", order.getOrderDate());
                orderMap.put("tblno", order.getTableNo());
                orderMap.put("ordstatus", order.getOrderStatus());
                orderMap.put("ordertype", order.getOrderType());

                // Create OrderFood list
                List<Map<String, Object>> orderFoodsList = new ArrayList<>();
                for (OrderFood orderFood : order.getOrderFoods()) {
                    Map<String, Object> orderFoodMap = new HashMap<>();
                    orderFoodMap.put("food_name", orderFood.getFoodName());
                    orderFoodMap.put("quantity", orderFood.getQuantity());
                    orderFoodMap.put("price", orderFood.getPrice());
                    orderFoodsList.add(orderFoodMap);
                }
                orderMap.put("orderFoods", orderFoodsList);

                orderList.add(orderMap);
            }

            billDetailsMap.put("order", orderList);
            billDetailsList.add(billDetailsMap);
        }

        data.put("billDetails", billDetailsList);

        return new ApiResponse(data, true, 200);
    }





}