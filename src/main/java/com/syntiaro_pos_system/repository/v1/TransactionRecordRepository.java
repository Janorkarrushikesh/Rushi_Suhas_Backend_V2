package com.syntiaro_pos_system.repository.v1;

import com.syntiaro_pos_system.entity.v1.TransactionRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TransactionRecordRepository extends JpaRepository<TransactionRecord, Long> {
    @Query("SELECT t FROM TransactionRecord t WHERE t.storeId = :storeId")
    List<TransactionRecord> findByStoreid(Integer storeId);


    @Query("SELECT p FROM TransactionRecord p WHERE p.storeId = :store_id AND p.createdDate BETWEEN :startDate AND :endDate")
    List<TransactionRecord> findByStoreIdAndDateRange(
            @Param("store_id") Integer store_id,
            @Param("startDate") String startDate,
            @Param("endDate") String endDate
    );
}