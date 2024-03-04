package com.syntiaro_pos_system.repository.v2;

import com.syntiaro_pos_system.entity.v1.ERole;
import com.syntiaro_pos_system.entity.v1.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRoleRepositoryV2 extends JpaRepository<UserRole,Long> {
    Optional<UserRole> findByName(ERole name);
}
