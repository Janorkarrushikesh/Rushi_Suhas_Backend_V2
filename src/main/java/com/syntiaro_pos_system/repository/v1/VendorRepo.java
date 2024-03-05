package com.syntiaro_pos_system.repository.v1;

import com.syntiaro_pos_system.entity.v1.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
@EnableJpaRepositories
public interface VendorRepo extends JpaRepository<Vendor, Long> {

    //THIS METHOD IS USE FOR FETCH VENDOR BY STOREID
    @Query("SELECT v FROM Vendor v WHERE v.storeId = :storeId")
    List<Vendor> findBystore_id(Integer storeId);

    @Query("SELECT CASE WHEN COUNT(v) > 0 THEN true ELSE false END " +
            "FROM Vendor v WHERE v.storeId = :store_id AND v.vendorName = :vendor_name")
    boolean existsByStoreIdAndVendorName(@Param("store_id") Integer store_id,
                                         @Param("vendor_name") String vendor_name);


    @Query("SELECT MAX(vendor.vendorId) FROM Vendor vendor WHERE vendor.storeId = :storeId")
    Integer findMaxVendorIdByStoreId(@Param("storeId") Integer storeId);


    @Query("SELECT v FROM Vendor v WHERE v.storeId = :store_id AND v.createdDate BETWEEN :startDate AND :endDate")
    List<Vendor> findByStoreIdAndDateRange(
            @Param("store_id") Integer store_id,
            @Param("startDate") String startDate,
            @Param("endDate") String endDate
    );

    @Query("SELECT p FROM Vendor p WHERE p.storeId = :store_id AND p.createdDate BETWEEN :startDate AND :endDate")
    List<Vendor> findByStoreIdAndCreatedDateBetween(@Param("store_id") Integer store_id,
                                                    @Param("startDate") String startDate,
                                                    @Param("endDate") String endDate);


    // Find vendor by store ID and account number
    @Query("SELECT v FROM Vendor v WHERE v.storeId = :store_id AND v.accountNo = :account_no")
    Vendor findByStoreIdAndAccountNo(@Param("store_id") Integer store_id, @Param("account_no") String account_no);

    // Find vendor by store ID and UPI ID
    @Query("SELECT v FROM Vendor v WHERE v.storeId = :store_id AND v.upiId = :upi_id")
    Vendor findByStoreIdAndUpiId(@Param("store_id") Integer store_id, @Param("upi_id") String upi_id);

}