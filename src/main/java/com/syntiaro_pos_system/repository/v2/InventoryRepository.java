package com.syntiaro_pos_system.repository.v2;

import com.syntiaro_pos_system.entity.v1.Inventory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Integer> {

    List<Inventory> findByStoreId(String storeId);
    Page<Inventory> findByStoreId(String storeId , Pageable pageable);
    @Query("SELECT MAX(inventory.id) FROM Inventory inventory WHERE inventory.storeId = :storeId")
    Integer findMaxIdByStoreId(String storeId);

    @Query("SELECT i FROM Inventory i WHERE i.storeId = :storeId AND i.createdDate BETWEEN :startDate AND :endDate ORDER BY i.SerialNo   DESC")
    List<Inventory> getInventoryByDate(String storeId, String startDate, String endDate);
}
