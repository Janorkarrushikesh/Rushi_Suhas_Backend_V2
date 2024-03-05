package com.syntiaro_pos_system.security.services;


import com.syntiaro_pos_system.entity.v1.Tech;
import com.syntiaro_pos_system.repository.v1.TechRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TechDetailsServiceImpl implements UserDetailsService {
    @Autowired
    TechRepository techRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Tech tech = techRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Tech Not Found with username: " + username));

        return TechDetailsImpl.build(tech);
    }

}
