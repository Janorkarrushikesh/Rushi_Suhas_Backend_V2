package com.syntiaro_pos_system.controllerimpl.v2;

import com.syntiaro_pos_system.controller.v2.UserSidebarController;
import com.syntiaro_pos_system.entity.v1.UserSidebar;
import com.syntiaro_pos_system.entity.v2.ApiResponse;
import com.syntiaro_pos_system.service.v2.UserSideBarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserSideBarControllerImpl implements UserSidebarController {

    @Autowired
    UserSideBarService userSideBarService;

    @Override
    public ResponseEntity<ApiResponse> saveUserSideBar(UserSidebar userSidebar) {
        return userSideBarService.saveUserSideBar(userSidebar);
    }

    @Override
    public ResponseEntity<ApiResponse> getUserSidebarByStoreId(String storeId) {
        return userSideBarService.getUserSidebarByStoreId(storeId);
    }

    @Override
    public ResponseEntity<ApiResponse> getUserSidebarById(Integer userId) {
        return userSideBarService.getUserSidebarById(userId);
    }

    @Override
    public ResponseEntity<ApiResponse> updateUsersidebar(Integer userId, UserSidebar userSidebar) {
        return userSideBarService.updateUsersidebar(userId, userSidebar);
    }

    @Override
    public ResponseEntity<ApiResponse> getByStatusAndStoreId(String storeId) {
        return userSideBarService.getByStatusAndStoreId(storeId);
    }
}
