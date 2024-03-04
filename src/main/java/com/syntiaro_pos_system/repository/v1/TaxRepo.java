package com.syntiaro_pos_system.repository.v1;


import com.syntiaro_pos_system.entity.v1.Tax;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
@EnableJpaRepositories
public interface TaxRepo extends JpaRepository<Tax, Long> {

    //-------this code added by Rushikesh for id genrate by store_id-------------------
    @Query("SELECT MAX(b.taxId) FROM Tax b WHERE b.storeidfk = :store_id")
    Long findLastNumberForStore(@Param("store_id") String store_id);

}