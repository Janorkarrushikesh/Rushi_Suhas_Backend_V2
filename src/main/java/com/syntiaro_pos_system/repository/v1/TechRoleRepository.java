package com.syntiaro_pos_system.repository.v1;

import com.syntiaro_pos_system.entity.v1.ERole;
import com.syntiaro_pos_system.entity.v1.TechRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TechRoleRepository extends JpaRepository<TechRole, Long> {
    Optional<TechRole> findByName(ERole name);
}
