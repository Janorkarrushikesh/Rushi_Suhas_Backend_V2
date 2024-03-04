package com.syntiaro_pos_system.repository.v2;

import com.syntiaro_pos_system.entity.v1.Inventory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Integer> {

    List<Inventory> findByStoreId(String storeId);
    Page<Inventory> findByStoreId(String storeId , Pageable pageable);
}
