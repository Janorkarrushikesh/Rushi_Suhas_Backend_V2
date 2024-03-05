package com.syntiaro_pos_system.controller.v2;

import com.syntiaro_pos_system.entity.v1.UserSidebar;
import com.syntiaro_pos_system.entity.v2.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(path = "/v2/sys/usersidebar")
public interface UserSidebarController {


    @PostMapping("/")
    ResponseEntity<ApiResponse> saveUserSideBar(@RequestBody UserSidebar userSidebar);

    @GetMapping("/store/{storeId}")
    ResponseEntity<ApiResponse> getUserSidebarByStoreId(@PathVariable String storeId);

    @GetMapping("/id/{userId}")
    ResponseEntity<ApiResponse> getUserSidebarById(@PathVariable Integer userId);

    @PatchMapping("/id/{userId}")
    ResponseEntity<ApiResponse> updateUsersidebar(@PathVariable Integer userId, @RequestBody UserSidebar userSidebar);

    @GetMapping("/status/{storeId}")
    ResponseEntity<ApiResponse> getByStatusAndStoreId(@PathVariable String storeId);

}
