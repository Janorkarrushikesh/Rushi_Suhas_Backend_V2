package com.syntiaro_pos_system.service.v2;

import com.syntiaro_pos_system.entity.v2.ApiResponse;
import com.syntiaro_pos_system.entity.v2.DashBoard;
import org.springframework.http.ResponseEntity;


public interface DashBoardService {
    ResponseEntity<ApiResponse> saveDashBoard(DashBoard dashBoard);

    ResponseEntity<ApiResponse> getByStoreId(Long storeId);

    ResponseEntity<ApiResponse> getByStoreIdAndStatus(Long storeId);

    ResponseEntity<ApiResponse> updateBoardByid(Long id, DashBoard dashboard);

    ResponseEntity<ApiResponse> deleteById(Long id);
}
