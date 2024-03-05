package com.syntiaro_pos_system.security.services;


import com.syntiaro_pos_system.entity.v1.Store;
import com.syntiaro_pos_system.repository.v1.StoreRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class StoreServiceIMPl implements StoreService {

    @Autowired
    StoreRepository storeRepo;


    @Override
    public List<Store> getStore() {
        return storeRepo.findAll();
    }


}