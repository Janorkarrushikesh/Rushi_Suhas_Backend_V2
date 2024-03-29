package com.syntiaro_pos_system.repository.v1;

import com.syntiaro_pos_system.entity.v1.Tech;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TechRepository extends JpaRepository<Tech, Long> {
    static Optional<Tech> findByUsernameAndPassword(String username, String password) {
        return null;
    }

    Optional<Tech> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    Optional<Tech> findByEmail(String email);


}
