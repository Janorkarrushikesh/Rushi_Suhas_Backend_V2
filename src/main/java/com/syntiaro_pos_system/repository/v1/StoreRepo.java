package com.syntiaro_pos_system.repository.v1;


import com.syntiaro_pos_system.entity.v1.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;


@EnableJpaRepositories
@Repository
public interface StoreRepo extends JpaRepository<Store, Integer> {
}