package com.syntiaro_pos_system.service.v2;

import com.syntiaro_pos_system.entity.v1.UserSidebar;
import com.syntiaro_pos_system.entity.v2.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface UserSideBarService {

    ResponseEntity<ApiResponse> saveUserSideBar(UserSidebar userSidebar);
    public ResponseEntity<ApiResponse> getUserSidebarByStoreId(String storeId);

    public ResponseEntity<ApiResponse> getUserSidebarById(Integer userId);


    ResponseEntity<ApiResponse> updateUsersidebar(Integer userId, UserSidebar userSidebar);
}
