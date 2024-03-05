package com.syntiaro_pos_system.service.v2;

import com.syntiaro_pos_system.entity.v2.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface MenuService {
    ResponseEntity<ApiResponse> getMenuById(int menuId);
}
