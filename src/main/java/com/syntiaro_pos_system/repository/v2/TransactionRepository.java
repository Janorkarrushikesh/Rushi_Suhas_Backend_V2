package com.syntiaro_pos_system.repository.v2;

import com.syntiaro_pos_system.entity.v1.TransactionRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface TransactionRepository extends JpaRepository<TransactionRecord,Long> {

    List<TransactionRecord> findByStoreId(Integer storeId);

    List<TransactionRecord> findByStoreIdAndDate(Integer storeId, LocalDate utilDate);
}
