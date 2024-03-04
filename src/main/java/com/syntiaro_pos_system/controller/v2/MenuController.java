package com.syntiaro_pos_system.controller.v2;


import com.syntiaro_pos_system.entity.v2.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(path = "/v2/sys/menu")
public interface MenuController {

    @GetMapping("/Id/{menuId}")
    public ResponseEntity<ApiResponse> getMenuById(@PathVariable int menuId);

//    @GetMapping("/getByStoreId/{storeId}")
//    public ResponseEntity<ApiResponse> getMenuByStoreId(@PathVariable )

}

