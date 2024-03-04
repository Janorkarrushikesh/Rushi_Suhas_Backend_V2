package com.syntiaro_pos_system.controllerimpl.v2;

import com.syntiaro_pos_system.controller.v2.SuperAdminController;
import com.syntiaro_pos_system.entity.v1.SuperAdmin;
import com.syntiaro_pos_system.entity.v2.ApiResponse;
import com.syntiaro_pos_system.request.v1.SuperAdminLoginRequest;
import com.syntiaro_pos_system.request.v1.SuperAdminSignupRequest;
import com.syntiaro_pos_system.service.v2.SuperAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class SuperAdminControllerImplV2 implements SuperAdminController {
    @Autowired
    SuperAdminService superAdminService;

    @Override
    public ResponseEntity<ApiResponse> authenticateSuperAdmin(SuperAdminLoginRequest superAdminLoginRequest) {
        return superAdminService.authenticateSuperAdmin(superAdminLoginRequest);
    }

    @Override
    public ResponseEntity<ApiResponse> registerSuperAdmin(SuperAdminSignupRequest superAdminSignupRequest) {
        return superAdminService.registerSuperAdmin(superAdminSignupRequest);
    }

    @Override
    public ResponseEntity<ApiResponse> forgotPassword(SuperAdminSignupRequest superAdminSignupRequest) {
        return superAdminService.forgotPassword(superAdminSignupRequest);
    }

    @Override
    public ResponseEntity<ApiResponse> resetPassword(Map<String, String> resetRequest) {
        return superAdminService.resetPassword(resetRequest);
    }

    @Override
    public ResponseEntity<ApiResponse> updateSuperAdmin(Long superid, SuperAdmin updateSuperAdmin) {
        return superAdminService.updateSuperAdmin(superid,updateSuperAdmin);
    }
    @Override
    public ResponseEntity<ApiResponse> logoutSuperAdmin(String sessionToken) {
        return superAdminService.logoutSuperAdmin(sessionToken);
    }
}
