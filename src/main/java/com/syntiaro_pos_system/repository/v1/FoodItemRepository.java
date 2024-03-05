package com.syntiaro_pos_system.repository.v1;

import com.syntiaro_pos_system.entity.v1.Receipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodItemRepository extends JpaRepository<Receipe, Long> {
    Receipe findByName(String foodName);

    List<Receipe> getFoodItemsByStoreId(String storeid);

    @Query("SELECT CASE WHEN COUNT(f) > 0 THEN true ELSE false END FROM Receipe f WHERE f.storeId = :storeid AND f.name = :name")
    boolean checkFoodNameExists(String storeid, String name);

    @Query("SELECT f FROM Receipe f WHERE f.name = :name AND f.storeId = :storeid")
    Receipe findByNameAndStoreId(String name, String storeid);


    //-------this code added by Rushikesh for id genrate by store_id-------------------
    @Query("SELECT MAX(b.id) FROM Receipe b WHERE b.storeId = :storeid")
    Long findLastNumberForStore(@Param("storeid") String storeid);


}