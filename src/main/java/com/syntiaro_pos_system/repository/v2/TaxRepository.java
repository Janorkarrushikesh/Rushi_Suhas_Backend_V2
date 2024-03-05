package com.syntiaro_pos_system.repository.v2;

import com.syntiaro_pos_system.entity.v1.Tax;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaxRepository extends JpaRepository<Tax, Long> {

    @Query("SELECT MAX(b.taxId) FROM Tax b WHERE b.storeidfk = :store_id")
    Long findLastNumberForStore(@Param("store_id") String store_id);

    @Query("SELECT t FROM Tax t WHERE t.storeidfk = :storeid")
    List<Tax> findByStoreIdFk(String storeid);

}
