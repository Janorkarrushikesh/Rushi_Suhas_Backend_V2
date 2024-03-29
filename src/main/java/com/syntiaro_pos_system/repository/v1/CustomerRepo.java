package com.syntiaro_pos_system.repository.v1;

import com.syntiaro_pos_system.entity.v1.CustomerDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@EnableJpaRepositories
public interface CustomerRepo extends JpaRepository<CustomerDetails, Integer> {


    @Query("SELECT MAX(customer.customerId) FROM CustomerDetails customer WHERE customer.storeId = :storeId")
    Integer findMaxVendorIdByStoreId(@Param("storeId") Integer storeId);

    @Query("SELECT c FROM CustomerDetails c WHERE c.storeId = :storeId")
    List<CustomerDetails> findByStore_id(Integer storeId);


    @Query("SELECT v FROM CustomerDetails v WHERE v.storeId = :store_id AND v.dateOfBirth BETWEEN :startDate AND :endDate")
    List<CustomerDetails> findByStoreIdAndDateRange(
            @Param("store_id") Integer store_id,
            @Param("startDate") String startDate,
            @Param("endDate") String endDate
    );

    boolean existsByCustomerNameAndStoreId(String customerName, Integer storeid);

    Boolean existsByEmailAndStoreId(String email, Integer storeid);

    Boolean existsByContactAndStoreId(String contact, Integer storeid);


}
