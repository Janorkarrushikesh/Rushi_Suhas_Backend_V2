package com.syntiaro_pos_system.repository.v1;


import com.syntiaro_pos_system.entity.v1.ERole;
import com.syntiaro_pos_system.entity.v1.StoreRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StoreRoleRepository extends JpaRepository<StoreRole, Long> {
  Optional<StoreRole> findByName(ERole name);
}
