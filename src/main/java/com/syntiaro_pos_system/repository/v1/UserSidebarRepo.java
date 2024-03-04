package com.syntiaro_pos_system.repository.v1;

import com.syntiaro_pos_system.entity.v1.UserSidebar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserSidebarRepo extends JpaRepository<UserSidebar, Integer> {

    UserSidebar findByUserId(int parseInt);

    boolean existsByUsername(String username);

    UserSidebar findByUsername(String username);

    @Query("SELECT u FROM UserSidebar u WHERE u.storeId = :storeId")
    List<UserSidebar> getUsersByStoreId(String storeId);
}
