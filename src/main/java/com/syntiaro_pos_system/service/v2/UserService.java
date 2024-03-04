package com.syntiaro_pos_system.service.v2;

import com.syntiaro_pos_system.entity.v2.ApiResponse;
import com.syntiaro_pos_system.request.v1.LoginRequest;
import com.syntiaro_pos_system.request.v1.SignupRequest;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface UserService {
    ResponseEntity<ApiResponse> authenticateUser(LoginRequest loginRequest);

    ResponseEntity<ApiResponse> registerUser(SignupRequest signUpRequest);

    ResponseEntity<ApiResponse> forgotPassword(SignupRequest signupRequest);

    ResponseEntity<ApiResponse> resetPassword(Map<String, String> resetRequest);

    ResponseEntity<ApiResponse> userByStoreId(Integer storeId);

    ResponseEntity<ApiResponse> logoutUser(String sessionToken);

    ResponseEntity<ApiResponse> updateUser(Long id, SignupRequest signUpRequest);
}
