package com.syntiaro_pos_system.repository.v1;

import com.syntiaro_pos_system.entity.v1.SubMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubMenuRepo extends JpaRepository<SubMenu, Long> {
    SubMenu findById(int id);

}
