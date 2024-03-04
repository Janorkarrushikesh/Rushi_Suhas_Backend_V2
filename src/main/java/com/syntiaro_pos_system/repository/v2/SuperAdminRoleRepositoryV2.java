package com.syntiaro_pos_system.repository.v2;

import com.syntiaro_pos_system.entity.v1.ERole;
import com.syntiaro_pos_system.entity.v1.SuperAdminRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SuperAdminRoleRepositoryV2 extends JpaRepository<SuperAdminRole,Long> {
    Optional<SuperAdminRole> findByName(ERole name);
}
