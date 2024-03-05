package com.syntiaro_pos_system.repository.v1;


import com.syntiaro_pos_system.entity.v1.Bill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository

public interface BillRepo extends JpaRepository<Bill, Integer> {

    LocalDate date = LocalDate.now();

    Optional<Bill> findById(Integer billid);

    // THIS METHOD IS USE FOR FETCH BILL BY STOREID
    @Query("SELECT b FROM Bill b WHERE b.storeId = :storeId")
    List<Bill> findBySid(Integer storeId);

    // this method added for filter last record 3 day  ---- by rushikesh
    @Query("SELECT b FROM Bill b JOIN b.order o WHERE b.storeId = :storeId AND (o.orderStatus = 'Running' OR o.orderStatus = 'Prepared') AND b.billDate >= :DaysAgo")
    List<Bill> findBillsByStoreAndStatusAndDate(@Param("storeId") Integer storeId, @Param("DaysAgo") LocalDate DaysAgo);

    // bug_04 this code made for filter kot   ---- by rushikesh
    @Query("SELECT b FROM Bill b JOIN b.order o WHERE b.storeId = :storeId AND o.orderStatus = 'Prepared' AND b.billDate >= :DaysAgo")
    List<Bill> findBillsByStoreAndStatusAndDatekot(@Param("storeId") Integer storeId, @Param("DaysAgo") LocalDate DaysAgo);

    //THIS METHOD IS USE FOR CALCULATE TOTAL CASH BY STOREID
    @Query("SELECT COALESCE(SUM(b.total), 0) FROM Bill b WHERE b.storeId = :storeId AND DATE(b.billDate) = :day AND b.paymentMode = 'cash'")
    Float calculateTotalCashAmountByStoreIdAndDay(@Param("storeId") Integer storeId, @Param("day") Date day);

    //THIS METHOD IS USE FOR CALCULATE TOTAL UPI BY STOREID
    @Query("SELECT COALESCE(SUM(b.total), 0) FROM Bill b WHERE b.storeId = :storeId AND DATE(b.billDate) = :day AND b.paymentMode = 'upi'")
    Float calculateTotalUpiAmountByStoreIdAndDay(@Param("storeId") Integer storeId, @Param("day") Date day);

    //THIS METHOD IS USE FOR CALCULATE TOTAL CARD BY STOREID
    @Query("SELECT COALESCE(SUM(b.total), 0) FROM Bill b WHERE b.storeId = :storeId AND DATE(b.billDate) = :day AND b.paymentMode = 'card'")
    Float calculateTotalCardAmountByStoreIdAndDay(@Param("storeId") Integer storeId, @Param("day") Date day);

    //THIS METHOD IS USE FOR CALCULATE OPENING BALANCE + CASH = TOTAL CLOSING BALANCE
//    @Query("SELECT b FROM Bill b WHERE b.storeId = :storeId AND b.billDate = :billdate")
//    List<Bill> findByBilldateAndStoreId(@Param("billdate") LocalDate billdate, @Param("store_id") Integer store_id);

    //THIS METHOD IS USE FOR CALCULATE OPENING BALANCE + CASH = TOTAL CLOSING BALANCE IT SHOW WHOLE LIST OF BALANCE
    @Query("SELECT b FROM Bill b WHERE b.billDate = :billdate")
    List<Bill> findByBilldate(LocalDate billdate);

    @Query("SELECT MAX(b.id)  FROM Bill b WHERE b.storeId = :store_id AND b.billDate = :billdate")
    Integer findLastBillNumberForStore(@Param("store_id") Integer store_id, LocalDate billdate);

    //------------------ THIS CODE FOR GETCASH PAYMENT DATE WISE-----------  TRUPTI  ------
    @Query("SELECT COALESCE(SUM(b.total), 0) FROM Bill b WHERE b.storeId = :storeId AND b.paymentMode = 'cash' GROUP BY DATE(b.billDate)")
    List<Float> getAllCashBillsByStoreId(@Param("storeId") Integer storeId);

    //----------------- trail code -------------------------------------
    @Query("SELECT DATE(b.billDate) AS day, MAX(b.id) AS max_id " + "FROM Bill b " + "WHERE b.storeId = :store_id " + "GROUP BY day " + "ORDER BY day")
    List<Object[]> findMaxIdByDay(@Param("store_id") Integer storeId);


    @Query("SELECT b FROM Bill b WHERE b.storeId = :store_id ORDER BY b.SerialNo DESC")
    List<Bill> findLastBillByStoreId(@Param("store_id") Integer storeId, PageRequest pageable);


    @Query("SELECT b FROM Bill b WHERE b.storeId = :store_id AND b.billDate BETWEEN :start_date AND :end_date")
    List<Bill> findByStoreIdAndBillDateBetween(@Param("store_id") Integer storeId, @Param("start_date") LocalDate startDate, @Param("end_date") LocalDate endDate);


    @Query("SELECT p FROM Bill p WHERE p.storeId = :store_id AND p.billDate BETWEEN :startDate AND :endDate")
    List<Bill> findByStoreIdAndDateRange(@Param("store_id") Integer store_id, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);


    @Query("SELECT b.billDate, COALESCE(SUM(b.total), 0) " + "FROM Bill b " + "WHERE b.storeId = :store_id AND b.billDate BETWEEN :startDatess AND :endDatess AND b.paymentMode = 'cash' " + "GROUP BY b.billDate")
    List<Object[]> calculateTotalCashAmountByStoreIdAndDates(@Param("store_id") Integer store_id, @Param("startDatess") LocalDate startDatess, @Param("endDatess") LocalDate endDatess);

    @Query("SELECT b.billDate, COALESCE(SUM(b.total), 0) " + "FROM Bill b " + "WHERE b.storeId = :store_id AND b.billDate BETWEEN :startDatess AND :endDatess AND b.paymentMode = 'upi' " + "GROUP BY b.billDate")
    List<Object[]> calculateTotalcardAmountByStoreIdAndDates(@Param("store_id") Integer store_id, @Param("startDatess") LocalDate startDatess, @Param("endDatess") LocalDate endDatess);

    @Query("SELECT b.billDate, COALESCE(SUM(b.total), 0) " + "FROM Bill b " + "WHERE b.storeId = :store_id AND b.billDate BETWEEN :startDatess AND :endDatess AND b.paymentMode = 'card' " + "GROUP BY b.billDate")
    List<Object[]> calculateTotalupiAmountByStoreIdAndDates(@Param("store_id") Integer store_id, @Param("startDatess") LocalDate startDatess, @Param("endDatess") LocalDate endDatess);


    @Query("SELECT b.billDate, COALESCE(SUM(b.total), 0) " + "FROM Bill b " + "WHERE b.storeId = :store_id AND b.paymentMode = 'cash' " + "GROUP BY b.billDate")
    List<Object[]> calculateTotalCashAmountByStoreId(@Param("store_id") Integer store_id);

    @Query("SELECT b.billDate, COALESCE(SUM(b.total), 0) " + "FROM Bill b " + "WHERE b.storeId = :store_id  AND b.paymentMode = 'upi' " + "GROUP BY b.billDate")
    List<Object[]> calculateTotalcardAmountByStoreId(@Param("store_id") Integer store_id);

    @Query("SELECT b.billDate, COALESCE(SUM(b.total), 0) " + "FROM Bill b " + "WHERE b.storeId = :store_id AND b.paymentMode = 'card' " + "GROUP BY b.billDate")
    List<Object[]> calculateTotalupiAmountByStoreId(@Param("store_id") Integer store_id);

    // billreport pagination code
    @Query("SELECT b FROM Bill b WHERE b.storeId = :storeId ORDER BY b.SerialNo DESC")
    Page<Bill> findBillsByStoreId(@Param("storeId") Integer storeId, Pageable pageable);


}






