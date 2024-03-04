package com.syntiaro_pos_system.repository.v1;


import com.syntiaro_pos_system.entity.v1.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuRepo extends JpaRepository<Menu, Long> {
    Menu findByMenuId(int menuId);

}
