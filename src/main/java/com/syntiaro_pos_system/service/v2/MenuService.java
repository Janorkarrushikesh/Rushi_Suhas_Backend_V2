package com.syntiaro_pos_system.service.v2;

import com.syntiaro_pos_system.entity.v2.ApiResponse;

public interface MenuService {
    ApiResponse getMenuById(int menuId);
}
