package com.syntiaro_pos_system.controller.v2;

import com.syntiaro_pos_system.entity.v1.UserSidebar;
import com.syntiaro_pos_system.entity.v2.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.method.P;
import org.springframework.web.bind.annotation.*;

import java.time.format.ResolverStyle;

@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(path = "/v2/sys/usersidebar")
public interface UserSidebarController {


    @PostMapping("/")
    public ResponseEntity<ApiResponse> saveUserSideBar(@RequestBody UserSidebar userSidebar);

    @GetMapping("/store/{storeId}")
    public ResponseEntity<ApiResponse> getUserSidebarByStoreId(@PathVariable String storeId);

    @GetMapping("/id/{userId}")
    public ResponseEntity<ApiResponse> getUserSidebarById(@PathVariable Integer userId);

    @PatchMapping("/id/{userId}")
    public ResponseEntity<ApiResponse> updateUsersidebar(@PathVariable Integer userId,@RequestBody UserSidebar userSidebar);
}
