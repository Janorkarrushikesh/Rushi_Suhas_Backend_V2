package com.syntiaro_pos_system.service.v2;

import com.syntiaro_pos_system.entity.v1.Tech;
import com.syntiaro_pos_system.entity.v2.ApiResponse;
import com.syntiaro_pos_system.request.v1.TechLoginRequest;
import com.syntiaro_pos_system.request.v1.TechSignupRequest;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface TechService {
    ResponseEntity<ApiResponse> authenticateTech(TechLoginRequest techLoginRequest);

    ResponseEntity<ApiResponse> registerTech(TechSignupRequest techsignupRequest);

    ResponseEntity<ApiResponse> forgotPassword(TechSignupRequest techSignupRequest);

    ResponseEntity<ApiResponse> resetPasseord(Map<String, String> resetRequest);

    ResponseEntity<ApiResponse> updateTech(Long technicianId, Tech tech);
    ResponseEntity<ApiResponse> logoutTech(String sessionToken);
}
