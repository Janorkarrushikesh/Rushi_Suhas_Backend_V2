package com.syntiaro_pos_system.repository.v2;

import com.syntiaro_pos_system.entity.v2.DashBoard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DashBoardRepository extends JpaRepository<DashBoard, Long> {

    // Optional<DashBoard> findByStoreId(Long storeId);
    List<DashBoard> findByStoreId(Long storeId);
}
