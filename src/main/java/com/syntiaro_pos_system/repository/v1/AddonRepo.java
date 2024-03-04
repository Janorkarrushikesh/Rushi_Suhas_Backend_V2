package com.syntiaro_pos_system.repository.v1;



import com.syntiaro_pos_system.entity.v1.Addon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddonRepo extends JpaRepository<Addon,Integer> {

    List<Addon> findByStoreId(String storeId);

}
