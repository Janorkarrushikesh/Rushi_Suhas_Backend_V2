package com.syntiaro_pos_system.controller.v2;

import com.syntiaro_pos_system.entity.v2.AdminMenu;
import com.syntiaro_pos_system.entity.v2.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(path = "/v2/sys/adminmenu")
public interface AdminMenuController {

    @PostMapping(path = "/")
    ResponseEntity<ApiResponse> saveAdminMenu(@RequestBody List<AdminMenu> adminMenu);

    @GetMapping(path = "/storeidandstatus/{storeid}")  // IF STATUS IS TRUE THEN ONLY SHOW DATA
    ResponseEntity<ApiResponse> getAdminMenuByStatus(@PathVariable Long StoreId);

    @PatchMapping(path = "/{adminMenuId}") // THIS METHOD USE FOR CHANGE THE DATA
    ResponseEntity<ApiResponse> updateAdminMenu(@PathVariable Long adminMenuId, @RequestBody AdminMenu adminMenu);

    @GetMapping(path = "/store/{storeid}")  // IN THIS METHOD FIND ONLY THAT PERTICULAR STORE
    // @Cacheable(value = "storeid")
    ResponseEntity<ApiResponse> getAdminMenu(@PathVariable Long storeid);

    @GetMapping(path = "/getbytitleandstoreid")  // IN THIS METHOD GET DATA BY TITLE
    ResponseEntity<ApiResponse> getByTitleAndStoreId(@RequestParam String title, @RequestParam Long storeId);

}
