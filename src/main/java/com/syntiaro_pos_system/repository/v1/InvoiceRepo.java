package com.syntiaro_pos_system.repository.v1;


import com.syntiaro_pos_system.entity.v1.VendorInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
@EnableJpaRepositories
public interface InvoiceRepo extends JpaRepository<VendorInventory, Integer> {


    // THIS METHOD IS USE FOR FETCH INVOICE BY STOREID
    @Query("SELECT i FROM VendorInventory i WHERE i.storeId = :storeId")
    List<VendorInventory> findByStore_id(Integer storeId);

    //-------this code added by Rushikesh for id genrate by store_id-------------------
    @Query("SELECT MAX(b.invoiceId) FROM VendorInventory b WHERE b.storeId = :store_id")
    Integer findLastNumberForStore(@Param("store_id") Integer store_id);


    @Query("SELECT i FROM VendorInventory i WHERE i.storeId = :store_id AND i.createDate BETWEEN :startDate AND :endDate")
    List<VendorInventory> findByStoreIdAndDateRange(
            @Param("store_id") Integer store_id,
            @Param("startDate") String startDate,
            @Param("endDate") String endDate
    );
}