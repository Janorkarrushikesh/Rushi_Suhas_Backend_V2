package com.syntiaro_pos_system.repository.v1;

import com.syntiaro_pos_system.entity.v1.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@EnableJpaRepositories
public interface OrderRepo extends JpaRepository<Orders, Integer> {

    //THIS METHOD IS USE FOR FETCH ORDER BY STOREID
    List<Orders> findBystoreId(String storeId);

    //-------this code added by Rushikesh for id genrate by store_id-------------------
    @Query("SELECT MAX(b.orderId) FROM Orders b WHERE b.storeId = :store_id")
    Integer findLastNumberForStore(@Param("store_id") String store_id);

//    @Query("SELECT MAX(b.oid) FROM Orders b WHERE b.sid = :store_id AND b.orddate= :orddate")
//    Integer findLastNumberForStore(@Param("store_id") String store_id ,  String orddate);


    @Query("SELECT MAX(b.id)  FROM Bill b WHERE b.storeId = :store_id AND b.billDate = :billdate")
    Integer findLastOrderNumberForStore(@Param("store_id") Integer store_id, LocalDate billdate);


    @Query("SELECT b FROM Orders b WHERE b.SerialNo = :Serial_no")
    Optional<Orders> findbySerialno(@Param("Serial_no") Integer Serial_no);


}
