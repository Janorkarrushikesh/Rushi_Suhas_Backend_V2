package com.syntiaro_pos_system.repository.v2;

import com.syntiaro_pos_system.entity.v1.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;

public interface OrderRepository extends JpaRepository<Orders, Integer> {
    @Query("SELECT MAX(b.BillId)  FROM Bill b WHERE b.storeId = :storeId AND b.billDate = :billdate")
    Integer findLastOrderNumberForStore(Integer storeId, LocalDate billdate);
}
