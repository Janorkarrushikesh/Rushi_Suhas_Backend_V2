package com.syntiaro_pos_system.security.services;


import com.syntiaro_pos_system.entity.v1.Tech;
import com.syntiaro_pos_system.repository.v1.TechRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class TechServiceIMPl implements TechService {

    @Autowired
    TechRepository storeRepo;

    @Override
    public List<Tech> getStore() {
        return storeRepo.findAll();
    }

}