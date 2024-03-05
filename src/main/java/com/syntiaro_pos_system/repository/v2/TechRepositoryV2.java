package com.syntiaro_pos_system.repository.v2;

import com.syntiaro_pos_system.entity.v1.Tech;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TechRepositoryV2 extends JpaRepository<Tech, Long> {
    Boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    Optional<Tech> findByUsername(String username);

    Optional<Tech> findByEmail(String email);
}
