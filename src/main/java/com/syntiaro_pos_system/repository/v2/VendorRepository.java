package com.syntiaro_pos_system.repository.v2;

import com.syntiaro_pos_system.entity.v1.Vendor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface VendorRepository extends JpaRepository<Vendor,Long> {

    Page<Vendor> findByStoreId(Integer storeId , Pageable pageable );

    @Query("SELECT MAX(vendor.vendorId) FROM Vendor vendor WHERE vendor.storeId = :storeId")
    Integer findMaxVendorIdByStoreId(Integer storeId);
}
