package com.syntiaro_pos_system.repository.v2;

import com.syntiaro_pos_system.entity.v1.ERole;
import com.syntiaro_pos_system.entity.v1.TechRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TechRoleRepositoryV2 extends JpaRepository<TechRole, Long> {
    Optional<TechRole> findByName(ERole name);
}
