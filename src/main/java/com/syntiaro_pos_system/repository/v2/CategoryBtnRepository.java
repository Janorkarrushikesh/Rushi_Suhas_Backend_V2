package com.syntiaro_pos_system.repository.v2;

import com.syntiaro_pos_system.entity.v1.CategoryButton;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryBtnRepository extends JpaRepository<CategoryButton,Long> {

    List<CategoryButton> findByStoreId(String storeId);

    @Query("SELECT b FROM CategoryButton b WHERE b.storeId = :storeId AND b.Name = :butname")
    CategoryButton findButtonByIdAndButname(@Param("storeId") String storeId, @Param("butname") String butname);
}
