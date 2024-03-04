package com.syntiaro_pos_system.repository.v2;

import com.syntiaro_pos_system.entity.v1.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {

    Page<Payment> findByStoreId(Integer storeId , Pageable pageable);
    List<Payment> findByStoreId(Integer storeId);

    @Query("SELECT MAX(b.paymentId) FROM Payment b WHERE b.storeId = :storeId")
    Integer findLastNumberForStore(Integer storeId);
@Query("select p from Payment p where p.storeId = :storeId and p.paymentDate Between :startDate and :endDate Order by p.SerialNo desc")
    List<Payment> findBetweenDate(Integer storeId, String startDate, String endDate);
}
