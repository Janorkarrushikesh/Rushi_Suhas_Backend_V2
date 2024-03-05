package com.syntiaro_pos_system.utils;

import com.syntiaro_pos_system.repository.v2.StoreRepositry;
import com.syntiaro_pos_system.repository.v2.SuperAdminRepositoryV2;
import com.syntiaro_pos_system.repository.v2.TechRepositoryV2;
import com.syntiaro_pos_system.repository.v2.UserRepositoryV2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmailUsernameValidation {

    @Autowired
    StoreRepositry storeRepository;

    @Autowired
    UserRepositoryV2 userRepository;
    @Autowired
    TechRepositoryV2 techRepository;
    @Autowired
    SuperAdminRepositoryV2 superAdminRepository;

    public boolean isDuplicateUsername(String username) {
        return storeRepository.existsByUsername(username) ||
                userRepository.existsByUsername(username) ||
                techRepository.existsByUsername(username) ||
                superAdminRepository.existsByUsername(username);
    }

    public boolean isDuplicateEmail(String email) {
        return techRepository.existsByEmail(email) ||
                userRepository.existsByEmail(email) ||
                storeRepository.existsByEmail(email) ||
                superAdminRepository.existsByEmail(email);
    }
}
