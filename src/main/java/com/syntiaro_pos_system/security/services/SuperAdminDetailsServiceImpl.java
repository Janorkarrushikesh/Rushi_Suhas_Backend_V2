package com.syntiaro_pos_system.security.services;


import com.syntiaro_pos_system.entity.v1.SuperAdmin;
import com.syntiaro_pos_system.repository.v1.SuperAdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SuperAdminDetailsServiceImpl implements UserDetailsService {
  @Autowired
  SuperAdminRepository superAdminRepository;

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    SuperAdmin superAdmin = superAdminRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("SuperAdmin Not Found with username: " + username));

    return SuperAdminDetailsImpl.build(superAdmin);
  }

}
