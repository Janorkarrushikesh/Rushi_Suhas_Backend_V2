package com.syntiaro_pos_system.repository.v1;

import com.syntiaro_pos_system.entity.v1.FloorTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FloorTableRepo extends JpaRepository<FloorTable,Long> {

    @Query("SELECT MAX(b.id)  FROM FloorTable b WHERE b.storeid = :storeid" )
    Integer findLastFloorNumberForStore(@Param("storeid") String storeid );

    boolean existsBystoreidAndFloorname(long storeid, String floorname);

    //-------this code added by Rushikesh for id genrate by store_id-------------------
    @Query("SELECT MAX(b.id) FROM FloorTable b WHERE b.storeid = :storeid")
    Integer findLastNumberForStore(@Param("storeid") String storeid);

    @Query("SELECT f FROM FloorTable f WHERE f.storeid = :storeid")
   List<FloorTable> findbyStoreid(@Param("storeid") Long storeid);



}
