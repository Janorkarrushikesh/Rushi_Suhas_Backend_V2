package com.syntiaro_pos_system.repository.v1;


import com.syntiaro_pos_system.entity.v1.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@EnableJpaRepositories
@Repository
public interface FoodRepo extends JpaRepository<Food, Integer> {

    // THIS METHOD IS USE FOR FETCH FOOD BY STOREID
    @Query("SELECT f FROM Food f WHERE f.storeId = :store_id")
    List<Food> findByStore_id(String store_id);

    //THIS METHOD IS USE FOR NOT INSERT SAME FOOD NAME
    @Query("SELECT COUNT(f) > 0 FROM Food f WHERE f.foodName = :food_name AND f.storeId = :store_id")
    boolean existsByFoodNameAndStoreId(@Param("food_name") String foodName, @Param("store_id") String storeId);


    @Query("SELECT f FROM Food f WHERE f.storeId = :store_id")
    List<Food> findByStoreId(@Param("store_id") String store_id);


    //-------this code added by Rushikesh for id genrate by store_id-------------------
    @Query("SELECT MAX(b.foodId) FROM Food b WHERE b.storeId = :store_id")
    Integer findLastNumberForStore(@Param("store_id") String store_id);

    @Query("SELECT f FROM Food f WHERE f.storeId = :store_id AND f.category = :category")
    List<Food> findByStoreIdAndCategory(@Param("store_id") String store_id, @Param("category") String category);


}