package com.syntiaro_pos_system.controllerimpl.v2;

import com.syntiaro_pos_system.controller.v2.DashBoardController;
import com.syntiaro_pos_system.entity.v2.ApiResponse;
import com.syntiaro_pos_system.entity.v2.DashBoard;
import com.syntiaro_pos_system.service.v2.DashBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class DashBoardControllerImpl implements DashBoardController {

    @Autowired
    DashBoardService dashBoardService;

    @Override
    public ResponseEntity<ApiResponse> saveDashboard(DashBoard dashBoard) {
        return dashBoardService.saveDashBoard(dashBoard);
    }

    @Override
    public ResponseEntity<ApiResponse> getByStoreId(Long storeId) {
        return dashBoardService.getByStoreId(storeId);
    }

    @Override
    public ResponseEntity<ApiResponse> getByStoreIdAndStatus(Long storeId) {
        return dashBoardService.getByStoreIdAndStatus(storeId);
    }

    @Override
    public ResponseEntity<ApiResponse> updateBoardByid(Long id, DashBoard dashBoard) {
        return dashBoardService.updateBoardByid(id, dashBoard);
    }

    @Override
    public ResponseEntity<ApiResponse> deleteById(Long id) {
        return dashBoardService.deleteById(id);
    }


}
