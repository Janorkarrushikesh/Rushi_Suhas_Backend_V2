package com.syntiaro_pos_system.repository.v2;

import com.syntiaro_pos_system.entity.v1.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepositry extends JpaRepository<Payment, Integer> {

    List<Payment> findByStoreId(Integer storeId);
}
