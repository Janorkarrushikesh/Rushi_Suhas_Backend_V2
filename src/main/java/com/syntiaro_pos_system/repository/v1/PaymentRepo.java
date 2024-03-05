package com.syntiaro_pos_system.repository.v1;

import com.syntiaro_pos_system.entity.v1.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;


@Repository
@EnableJpaRepositories
public interface PaymentRepo extends JpaRepository<Payment, Integer> {

    // THIS METHOD IS USE FOR FETCH PAYMENT BY STOREID
    @Query("SELECT p FROM Payment p WHERE p.storeId = :storeId")
    List<Payment> findByStore_id(Integer storeId);

    // THIS METHOD IS USE FOR FETCH PAYMENT BY ID
    @Query("SELECT p FROM Payment p WHERE p.SerialNo = :Serial_no")
    Payment findByPaymentId(Integer Serial_no);

    //-------this code added by Rushikesh for id genrate by store_id-------------------
    @Query("SELECT MAX(b.paymentId) FROM Payment b WHERE b.storeId = :store_id")
    Integer findLastNumberForStore(@Param("store_id") Integer store_id);

    @Query("SELECT p FROM Payment p WHERE p.storeId = :store_id AND p.paymentDate BETWEEN :startDate AND :endDate")
    List<Payment> findByStoreIdAndDateRange(
            @Param("store_id") Integer store_id,
            @Param("startDate") String startDate,
            @Param("endDate") String endDate
    );

    @Query("SELECT p FROM Payment p WHERE p.storeId = :store_id")
    List<Payment> findByStoreId(Integer store_id);


    @Query("SELECT p FROM Payment p WHERE p.createDate BETWEEN :startDate AND :endDate")
    List<Payment> findByCreatedDateBetween(@Param("startDate") Date startDate, @Param("endDate") Date endDate);


    @Query("SELECT p FROM Payment p WHERE p.storeId = :store_id AND p.paymentDate BETWEEN :startDate AND :endDate")
    List<Payment> findByStoreIdAndCreatedDateBetween(@Param("store_id") Integer store_id,
                                                     @Param("startDate") String startDate,
                                                     @Param("endDate") String endDate);

    @Query("SELECT p FROM Payment p WHERE p.storeId = :store_id AND p.accountNo = :account_no")
    Payment findByStoreIdAndAccountNo(@Param("store_id") Integer store_id, @Param("account_no") String account_no);


    @Query("SELECT p FROM Payment p WHERE p.storeId = :store_id AND p.upiId = :upi_id")
    Payment findByStoreIdAndUpiId(@Param("store_id") Integer store_id, @Param("upi_id") String upi_id);


}
