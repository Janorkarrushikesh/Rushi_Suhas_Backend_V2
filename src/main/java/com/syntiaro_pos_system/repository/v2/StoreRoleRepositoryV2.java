package com.syntiaro_pos_system.repository.v2;

import com.syntiaro_pos_system.entity.v1.ERole;
import com.syntiaro_pos_system.entity.v1.StoreRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StoreRoleRepositoryV2 extends  JpaRepository <StoreRole,Long>{
    Optional<StoreRole> findByName(ERole name);
}