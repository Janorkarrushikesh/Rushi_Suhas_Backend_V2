package com.syntiaro_pos_system.repository.v1;


import com.syntiaro_pos_system.entity.v1.ERole;
import com.syntiaro_pos_system.entity.v1.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
    static Optional<Store> findByUsernameAndPassword(String username, String password) {
        return null;
    }

    Optional<Store> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    Boolean existsByContact(String contact);

    Optional<Store> findByEmail(String email);


    @Query("SELECT DISTINCT s FROM Store s LEFT JOIN FETCH s.tax")
    List<Store> findAllStoresWithTax();

    List<Store> findByStoreRoles_Name(ERole eRole);


    // THIS METHOD USE RENEWSUBSCRIPTION USING REGISTRATION NUMBER
    @Query("SELECT s FROM Store s WHERE s.registrationNo = :regiNum")
    Optional<Store> findByRegistrationNumber(@Param("regiNum") String registrationNumber);

    @Query("SELECT s FROM Store s WHERE s.createdDate BETWEEN :startDate AND :endDate")
    List<Store> findByCreatedDateBetween(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query("SELECT s.email FROM Store s")
    String findStoreEmail();

    @Query("SELECT s FROM Store s ORDER BY s.Storeid DESC")
    List<Store> findAllByDesc();

}
