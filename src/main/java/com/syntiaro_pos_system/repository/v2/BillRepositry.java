package com.syntiaro_pos_system.repository.v2;

import com.syntiaro_pos_system.entity.v1.Bill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Repository
public interface BillRepositry extends JpaRepository<Bill,Integer> {
    List<Bill> findByStoreId(Integer storeId);

    @Query("SELECT b FROM Bill b WHERE b.storeId = :storeId  ORDER BY b.SerialNo   DESC")
    Page<Bill> findBillByStoreId(Integer storeId , Pageable pageable );

    @Query("SELECT b FROM Bill b WHERE b.storeId = :storeId AND b.billDate BETWEEN :start_date AND :end_date ORDER BY b.SerialNo   DESC")
    List<Bill> findBillByStoreIds(Integer storeId  , @Param("start_date") LocalDate startDate, @Param("end_date") LocalDate endDate);

    @Query("SELECT MAX(b.BillId)  FROM Bill b WHERE b.storeId = :storeId AND b.billDate = :billdate")
    Integer findLastBillNumberForStore(Integer storeId, LocalDate billdate);

    @Query("SELECT b FROM Bill b JOIN b.order o WHERE b.storeId = :storeId AND o.orderStatus = :orderStatus AND b.billDate >= :DaysAgo")
    List<Bill> findBillsByStoreAndStatusAndDatekot(@Param("storeId") Integer storeId, @Param("DaysAgo") LocalDate DaysAgo,@Param("orderStatus") String orderStatus);

    @Query("SELECT COALESCE(SUM(b.total), 0) FROM Bill b WHERE b.storeId = :storeId AND DATE(b.billDate) = :day AND b.paymentMode = :mode")
    Float calculateTotalCardAmountByStoreIdAndDay(@Param("storeId") Integer storeId, @Param("day") Date day, @Param("mode") String mode);

    @Query("SELECT SUM(b.total), b.billDate FROM Bill b WHERE b.storeId = :storeId AND b.paymentMode = 'card' GROUP BY b.billDate")
    List<Object[]> total(@Param("storeId") Integer storeId) ;
}
