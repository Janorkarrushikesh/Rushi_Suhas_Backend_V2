package com.syntiaro_pos_system.controller.v2;

import com.syntiaro_pos_system.entity.v2.ApiResponse;
import com.syntiaro_pos_system.entity.v2.DashBoard;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(path = "/v2/sys/dashboard")
public interface DashBoardController {


    @PostMapping(path = "/")
    ResponseEntity<ApiResponse> saveDashboard(@RequestBody DashBoard dashBoard);

    @GetMapping(path = "/store/{storeId}")
    ResponseEntity<ApiResponse> getByStoreId(@PathVariable Long storeId);

    @GetMapping(path = "/idandstatus/{storeId}")
    ResponseEntity<ApiResponse> getByStoreIdAndStatus(@PathVariable Long storeId);


    @PatchMapping(path = "/id/{id}")
    ResponseEntity<ApiResponse> updateBoardByid(@PathVariable Long id, @RequestBody DashBoard dashBoard);

    @DeleteMapping(path = "/{id}")
    ResponseEntity<ApiResponse> deleteById(@PathVariable Long id);


}
