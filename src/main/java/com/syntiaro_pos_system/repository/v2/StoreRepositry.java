package com.syntiaro_pos_system.repository.v2;

import com.syntiaro_pos_system.entity.v1.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoreRepositry extends JpaRepository<Store,Long> {

    Optional<Store> findByUsername(String username);
    Optional<Store> findByEmail(String email);
    Boolean existsByUsername(String username);

    boolean existsByEmail(String email);
    Boolean existsByContact(String contact);
    @Query("SELECT s FROM Store s ORDER BY s.Storeid DESC")
    List<Store> findAllByDesc();


}
