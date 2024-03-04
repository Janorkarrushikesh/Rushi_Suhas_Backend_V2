package com.syntiaro_pos_system.security.services;


import com.syntiaro_pos_system.entity.v1.SuperAdmin;
import com.syntiaro_pos_system.repository.v1.SuperAdminRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class SuperAdminServiceIMPl implements SuperAdminService {

    @Autowired
    SuperAdminRepository storeRepo;


    @Override
    public List<SuperAdmin> getStore() {
        return storeRepo.findAll();
    }


}