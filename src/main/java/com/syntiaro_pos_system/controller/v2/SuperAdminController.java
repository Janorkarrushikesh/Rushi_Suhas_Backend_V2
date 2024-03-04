package com.syntiaro_pos_system.controller.v2;

import com.syntiaro_pos_system.entity.v1.SuperAdmin;
import com.syntiaro_pos_system.entity.v2.ApiResponse;
import com.syntiaro_pos_system.request.v1.SuperAdminLoginRequest;
import com.syntiaro_pos_system.request.v1.SuperAdminSignupRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RequestMapping("/v2/api/auth/superadmin")
public interface SuperAdminController {
    @PostMapping("/signin")
    public ResponseEntity<ApiResponse> authenticateSuperAdmin(@Valid @RequestBody SuperAdminLoginRequest superAdminLoginRequest);

    @PostMapping("/superSignup")
    public ResponseEntity<ApiResponse> registerSuperAdmin(@Valid @RequestBody SuperAdminSignupRequest superAdminSignupRequest);

    @PostMapping("/forgotPassword")
    public ResponseEntity<ApiResponse> forgotPassword(@RequestBody SuperAdminSignupRequest superAdminSignupRequest);

    @PostMapping("/resetPassword")
    public ResponseEntity<ApiResponse> resetPassword(@RequestBody Map<String, String> resetRequest);

    @PatchMapping("/{superid}")
    public ResponseEntity<ApiResponse> updateSuperAdmin(@PathVariable Long superid, @RequestBody SuperAdmin updateSuperAdmin);

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse> logoutSuperAdmin(@RequestParam String sessionToken);
}


