package com.syntiaro_pos_system.repository.v2;

import com.syntiaro_pos_system.entity.v1.SuperAdmin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SuperAdminRepositoryV2 extends JpaRepository<SuperAdmin, Long> {
    Boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    Optional<SuperAdmin> findByUsername(String username);

    Optional<SuperAdmin> findByEmail(String email);
}
