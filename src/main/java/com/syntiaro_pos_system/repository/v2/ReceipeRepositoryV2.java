package com.syntiaro_pos_system.repository.v2;

import com.syntiaro_pos_system.entity.v1.Receipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReceipeRepositoryV2 extends JpaRepository<Receipe,Long> {

    @Query("SELECT r FROM Receipe r WHERE r.storeId = :storeId")
    List<Receipe>findByStoreid(String storeId);
}
