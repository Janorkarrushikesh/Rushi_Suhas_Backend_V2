package com.syntiaro_pos_system.controllerimpl.v2;

import com.syntiaro_pos_system.controller.v2.MenuController;
import com.syntiaro_pos_system.entity.v2.ApiResponse;
import com.syntiaro_pos_system.service.v2.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MenuControllerV2Impl implements MenuController {

    @Autowired
    MenuService menuService;

    @Override
    public ResponseEntity<ApiResponse> getMenuById(int menuId) {

        return menuService.getMenuById(menuId);
    }
}
