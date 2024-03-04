package com.syntiaro_pos_system.repository.v1;

import com.syntiaro_pos_system.entity.v1.StorePayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StorePaymentRepository extends JpaRepository<StorePayment, Long> {

    List<StorePayment> findByStoreId(Long storeId);

    @Query("SELECT MAX(sp.paymentId) FROM StorePayment sp WHERE sp.storeId = :storeId")
    Long findMaxPaymentIdByStoreId(@Param("storeId") Long storeId);


    boolean existsByAccountNoAndStoreId(String accountNo, Long storeId);

    boolean existsByUpiIdAndStoreId(String upiId, Long storeId);



    StorePayment findByStoreIdAndAccountNo(Long storeId, String updatedAccountNo);

    StorePayment findByStoreIdAndUpiId(Long storeId, String updatedUpiId);

    boolean existsByAccountNo(String accountNo);

    boolean existsByUpiId(String upiId);

}