package com.syntiaro_pos_system.service.v2;

import com.syntiaro_pos_system.entity.v1.SuperAdmin;
import com.syntiaro_pos_system.entity.v2.ApiResponse;
import com.syntiaro_pos_system.request.v1.SuperAdminLoginRequest;
import com.syntiaro_pos_system.request.v1.SuperAdminSignupRequest;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface SuperAdminService {
    ResponseEntity<ApiResponse> authenticateSuperAdmin(SuperAdminLoginRequest superAdminLoginRequest);

    ResponseEntity<ApiResponse> registerSuperAdmin(SuperAdminSignupRequest superAdminSignupRequest);

    ResponseEntity<ApiResponse> forgotPassword(SuperAdminSignupRequest superAdminSignupRequest);

    ResponseEntity<ApiResponse> resetPassword(Map<String, String> resetRequest);

    ResponseEntity<ApiResponse> updateSuperAdmin(Long superid, SuperAdmin updateSuperAdmin);

    ResponseEntity<ApiResponse> logoutSuperAdmin(String sessionToken);
}
