package com.syntiaro_pos_system.service.v2;

import com.syntiaro_pos_system.entity.v2.AdminMenu;
import com.syntiaro_pos_system.entity.v2.ApiResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AdminMenuService {

    ResponseEntity<ApiResponse> SaveAdminemenu(List<AdminMenu> adminMenu);

    ResponseEntity<ApiResponse> getByStoreIdAndStatus(Long storeid);
    public ResponseEntity<ApiResponse> getStoreByStoreIdAndStatus(Long storeId);

    ResponseEntity<ApiResponse> updateAdminMenu(Long adminMenuId , AdminMenu adminMenu);

    ResponseEntity<ApiResponse> getByStoreId(Long storeId);

    ResponseEntity<ApiResponse> getByTitleAndStoreId(String title, Long storeId);
}
