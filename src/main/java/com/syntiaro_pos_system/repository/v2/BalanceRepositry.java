package com.syntiaro_pos_system.repository.v2;

import com.syntiaro_pos_system.entity.v1.Balance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface BalanceRepositry extends JpaRepository<Balance, Long> {
    Balance findByStoreIdAndDate(Integer storeId, LocalDate now);

    List<Balance> findByStoreId(Integer storeId);

    List<Balance> findAllByStoreIdAndDate(Integer storeId, LocalDate now);
}
