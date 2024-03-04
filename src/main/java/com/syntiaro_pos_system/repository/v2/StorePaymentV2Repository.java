package com.syntiaro_pos_system.repository.v2;

import com.syntiaro_pos_system.entity.v1.StorePayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StorePaymentV2Repository extends JpaRepository<StorePayment, Long> {

    boolean existsByAccountNoAndStoreId(String accountNo, Long storeId);
    boolean existsByUpiIdAndStoreId(String upiId, Long storeId);

    StorePayment findByStoreIdAndAccountNo(Long storeId, String updatedAccountNo);

    StorePayment findByStoreIdAndUpiId(Long storeId, String updatedUpiId);

    boolean existsByAccountNo(String accountNo);

    boolean existsByUpiId(String upiId);
    @Query("SELECT MAX(sp.paymentId) FROM StorePayment sp WHERE sp.storeId = :storeId")
    Long findMaxPaymentIdByStoreId(@Param("storeId") Long storeId);

    Optional<StorePayment> findByStoreId(Long storeId);
}
