package com.syntiaro_pos_system.service.v1;


import com.syntiaro_pos_system.entity.v1.ApiResponse;
import com.syntiaro_pos_system.entity.v1.Bill;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BillService {


 List<Bill> getBill();
 int addBill(Bill bill);
 Bill updateBill(Bill bill);
 void deletebill(int id);

 // THIS METHOD IS USE FOR FETCH BILL BY ID
 Optional<Bill> getbillbyid(Integer id);


 // THIS METHOD IS USE FOR FETCH BILL BY STOREID
 List<Bill> getBillsByStoreId(Integer storeId);


 Bill findBillById(Integer id);

 Bill saveBill(Bill existingBill);

 Bill updateBill(Integer id, Bill bill);


 //THIS METHOD IS USE FOR CALCULATE TOTAL CASH BY STOREID
 Float calculateTotalCashAmountByStoreIdAndDay(Integer storeId, LocalDate specificDay);


 //THIS METHOD IS USE FOR CALCULATE TOTAL UPI BY STOREID
 Float calculateTotalUpiAmountByStoreIdAndDay(Integer storeId, LocalDate specificDay);


 //THIS METHOD IS USE FOR CALCULATE TOTAL CARD BY STOREID
 Float calculateTotalCardAmountByStoreIdAndDay(Integer storeId, LocalDate specificDay);


 //THIS METHOD IS USE FOR ADD AMOUNT IN CLOSING BALANCE FROM BILL
 public void completeOrderAndPlaceBill(Bill bill, LocalDate calculationDate);

 // billreport pagination code  ---- by rushikesh
 public ApiResponse getBillsReport(
         @PathVariable(name = "storeId") Integer storeId,
         @RequestParam(name = "page", defaultValue = "0") Integer page,
         @RequestParam(name = "size", defaultValue = "20") Integer size) ;


}




