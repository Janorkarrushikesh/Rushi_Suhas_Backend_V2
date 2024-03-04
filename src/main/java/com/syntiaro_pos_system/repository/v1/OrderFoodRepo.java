package com.syntiaro_pos_system.repository.v1;



import com.syntiaro_pos_system.entity.v1.OrderFood;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@EnableJpaRepositories
@Repository
public interface OrderFoodRepo extends JpaRepository<OrderFood, Integer> {

    // THIS METHOD IS USE FOR FETCH ORDERFOOD BY STOREID
    @Query("SELECT o FROM OrderFood o WHERE o.storeId = :storeId")
    List<OrderFood> findByStoreId(String storeId);

    // ----------------------RUSHIKESH ADDED THIS NEW CODE--------------------------
    @Query("SELECT o.foodName, SUM(o.quantity) AS total_quantity " +
            "FROM OrderFood o " +
            "WHERE o.storeId = :storeId " +
            "GROUP BY o.foodName " +
            "ORDER BY total_quantity DESC")
    List<Object[]> getTotalQuantityByFoodName(@Param("storeId") String storeId);

    //-------this code added by Rushikesh for id genrate by store_id-------------------
    @Query("SELECT MAX(b.foodId) FROM OrderFood b WHERE b.storeId = :store_id")
    Integer findLastNumberForStore(@Param("store_id") String store_id);

}