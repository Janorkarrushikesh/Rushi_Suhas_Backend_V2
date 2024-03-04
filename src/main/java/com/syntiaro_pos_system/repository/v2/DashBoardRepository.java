package com.syntiaro_pos_system.repository.v2;

import com.syntiaro_pos_system.entity.v2.DashBoard;
import org.hibernate.sql.Select;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DashBoardRepository extends JpaRepository<DashBoard, Long> {

   // Optional<DashBoard> findByStoreId(Long storeId);
    List<DashBoard> findByStoreId(Long storeId);
}
