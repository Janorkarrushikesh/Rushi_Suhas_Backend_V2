package com.syntiaro_pos_system.repository.v1;


import com.syntiaro_pos_system.entity.v1.SuperAdmin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SuperAdminRepository extends JpaRepository<SuperAdmin, Long> {
    static Optional<SuperAdmin> findByUsernameAndPassword(String username, String password) {
        return null;
    }

    Optional<SuperAdmin> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    Optional<SuperAdmin> findByEmail(String email);


}
