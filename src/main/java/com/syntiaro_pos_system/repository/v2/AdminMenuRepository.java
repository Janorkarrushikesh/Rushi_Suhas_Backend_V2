package com.syntiaro_pos_system.repository.v2;

import com.syntiaro_pos_system.entity.v2.AdminMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AdminMenuRepository extends JpaRepository<AdminMenu, Long> {
    List<AdminMenu> findByStoreId(Long storeid);

    @Query("SELECT a FROM AdminMenu a WHERE a.status = :aTrue")
    List<AdminMenu> findByStatus(Boolean aTrue);


    AdminMenu findByTitleAndStoreId(String title, Long storeId);
}
