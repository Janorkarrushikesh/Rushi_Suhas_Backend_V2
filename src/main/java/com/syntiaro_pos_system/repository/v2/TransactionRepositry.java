package com.syntiaro_pos_system.repository.v2;

import com.syntiaro_pos_system.entity.v1.TransactionRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TransactionRepositry extends JpaRepository<TransactionRecord,Long> {
}
