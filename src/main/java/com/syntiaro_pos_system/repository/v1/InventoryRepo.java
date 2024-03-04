package com.syntiaro_pos_system.repository.v1;


import com.syntiaro_pos_system.entity.v1.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@EnableJpaRepositories
@Repository
public interface InventoryRepo extends JpaRepository <Inventory,Integer> {

    // THIS METHOD IS USE FOR FETCH INVENTORY BY STOREID
    List<Inventory> findByStoreId(String storeId);

    Inventory findByName(String name);
    @Query("SELECT i FROM Inventory i WHERE i.storeId = ?1 AND i.name = ?2")
    Inventory findByStoreIdAndName(String storeId, String name);

    //-------this code added by Rushikesh for id genrate by store_id-------------------
    @Query("SELECT MAX(b.id) FROM Inventory b WHERE b.storeId = :store_id")
    Integer findLastNumberForStore(@Param("store_id") String store_id);

    @Query("SELECT v FROM Inventory v WHERE v.storeId = :storeid AND v.createdDate BETWEEN :startDate AND :endDate")
    List<Inventory> findByStoreIdAndDateRange(
            @Param("storeid") String storeid,
            @Param("startDate") String startDate,
            @Param("endDate") String endDate
    );
    @Query("SELECT i FROM Inventory i WHERE i.storeId = :storeid AND i.createdDate BETWEEN :startDate AND :endDate")
    List<Inventory> findByStoreIdAndCreatedDateBetween(@Param("storeid") String storeid,
                                                       @Param("startDate") String startDate,
                                                       @Param("endDate") String endDate);


}






