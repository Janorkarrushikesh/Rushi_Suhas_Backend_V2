package com.syntiaro_pos_system.repository.v2;


import com.syntiaro_pos_system.entity.v1.Food;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FoodRepository extends JpaRepository<Food, Integer> {

    Page<Food> findByStoreId(String storeId, Pageable pageable);

    boolean existsByFoodNameAndStoreId(String foodName, String storeId);


    @Query("SELECT f FROM Food f WHERE f.storeId = :storeId")
    List<Food> findByStoreIds(String storeId);

    List<Food> findByStoreIdAndCategory(String storeId, String addon);
}
