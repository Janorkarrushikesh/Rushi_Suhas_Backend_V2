package com.syntiaro_pos_system.repository.v2;

import com.syntiaro_pos_system.entity.v1.VendorInventory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VendorInventoryRepository extends JpaRepository<VendorInventory,Integer> {
    List<VendorInventory> findInvoiceByStoreId(Integer storeId);

    @Query("SELECT MAX(b.invoiceId) FROM VendorInventory b WHERE b.storeId = :storeId")
    Integer findLastNumberForStore(Integer storeId);

    Page<VendorInventory> findByStoreId(Integer storeId, Pageable pageable);

    @Query("Select i from VendorInventory i where i.storeId = :storeId and i.createDate Between :startDate and :endDate Order by i.SerialNo  ")
    List<VendorInventory> findBetweenDate(Integer storeId, String startDate, String endDate);
}
