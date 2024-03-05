package com.syntiaro_pos_system.controllerimpl.v1;

import com.syntiaro_pos_system.entity.v1.Store;
import com.syntiaro_pos_system.repository.v1.StoreRepository;
import com.syntiaro_pos_system.repository.v1.StoreRoleRepository;
import com.syntiaro_pos_system.security.jwt.StoreJwtUtils;
import com.syntiaro_pos_system.security.services.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/test/store")
public class StoreTestController {

    @Autowired
    StoreRepository userRepo;

    @Autowired
    StoreRoleRepository storeRoleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    StoreJwtUtils storeJwtUtils;

    @Autowired
    StoreService storeService;

    // THIS METHOD IS USE FOR GET ALL STORE DETAILS
    @GetMapping("/allStore")
    public String allAccess() {
        return "Public Content.";
    }

    // THIS METHOD IS USE FOR GET USER
    @GetMapping("/user")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public String userAccess() {
        return "Store Content.";
    }

    // THIS METHOD IS USE FOR GET MODERATOR
    @GetMapping("/mod")
    @PreAuthorize("hasRole('MODERATOR')")
    public String moderatorAccess() {
        return "Moderator Board.";
    }

    // THIS METHOD IS USE FOR GET ADMIN
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminAccess() {
        return "Admin Board.";
    }

    // THIS METHOD IS USE FOR GET ALL STORE DETAIL
    @GetMapping("/getstore")
    // @PreAuthorize("hasRole('ADMIN')")
    public List<Store> getStore() {
        System.out.println("/getstore");
        return this.storeService.getStore();
    }

}
