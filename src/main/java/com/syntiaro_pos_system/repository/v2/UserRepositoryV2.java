package com.syntiaro_pos_system.repository.v2;

import com.syntiaro_pos_system.entity.v1.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepositoryV2 extends JpaRepository <User,Long>{
    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    boolean existsByContact(Long contact);

    @Query("SELECT MAX(b.id) FROM User b WHERE b.storeId = :store_id")
    Long findLastNumberForStore(@Param("store_id") Integer store_id);

    Optional<User> findByEmail(String email);


    List<User> findByStoreId(Integer storeId);
}
