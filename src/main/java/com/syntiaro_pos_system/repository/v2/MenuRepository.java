package com.syntiaro_pos_system.repository.v2;

import com.syntiaro_pos_system.entity.v1.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Integer> {
}
