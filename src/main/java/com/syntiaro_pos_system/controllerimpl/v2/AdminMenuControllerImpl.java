package com.syntiaro_pos_system.controllerimpl.v2;

import com.syntiaro_pos_system.controller.v2.AdminMenuController;
import com.syntiaro_pos_system.entity.v2.AdminMenu;
import com.syntiaro_pos_system.entity.v2.ApiResponse;
import com.syntiaro_pos_system.service.v2.AdminMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AdminMenuControllerImpl implements AdminMenuController {

    @Autowired
    AdminMenuService adminMenuService;

    @Override
    public ResponseEntity<ApiResponse> saveAdminMenu(List<AdminMenu> adminMenu) {
        return adminMenuService.SaveAdminemenu(adminMenu);
    }

    @Override
    public ResponseEntity<ApiResponse> getAdminMenuByStatus(Long storeid) {
        return adminMenuService.getByStoreIdAndStatus(storeid);
    }

    @Override
    public ResponseEntity<ApiResponse> updateAdminMenu(Long adminMenuId, AdminMenu adminMenu) {
        return adminMenuService.updateAdminMenu(adminMenuId, adminMenu);
    }


    @Override
    public ResponseEntity<ApiResponse> getAdminMenu(Long storeid) {
        return adminMenuService.getByStoreId(storeid);
    }

    @Override
    public ResponseEntity<ApiResponse> getByTitleAndStoreId(String title, Long storeId) {
        return adminMenuService.getByTitleAndStoreId(title, storeId);
    }


}