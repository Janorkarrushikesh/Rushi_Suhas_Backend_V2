package com.syntiaro_pos_system.repository.v2;


import com.syntiaro_pos_system.entity.v1.UserSidebar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserSideBarRepositry extends JpaRepository<UserSidebar,Integer> {

   List<UserSidebar> findByStoreId(String storeId);

    boolean existsByUsername(String username);
}
