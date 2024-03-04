package com.syntiaro_pos_system.controllerimpl.v2;

import com.syntiaro_pos_system.controller.v2.UserController;
import com.syntiaro_pos_system.entity.v2.ApiResponse;
import com.syntiaro_pos_system.request.v1.LoginRequest;
import com.syntiaro_pos_system.request.v1.SignupRequest;
import com.syntiaro_pos_system.service.v2.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class UserControllerImplV2 implements UserController {

    @Autowired
    UserService userService;
    @Override
    public ResponseEntity<ApiResponse> authenticateUser(LoginRequest loginRequest) {
        return userService.authenticateUser(loginRequest);
    }

    @Override
    public ResponseEntity<ApiResponse> registerUser(SignupRequest signUpRequest) {
        return userService.registerUser(signUpRequest);
    }

    @Override
    public ResponseEntity<ApiResponse> forgotPassword(SignupRequest signupRequest) {
        return userService.forgotPassword(signupRequest);
    }

    @Override
    public ResponseEntity<ApiResponse> resetPassword(Map<String, String> resetRequest) {
        return userService.resetPassword(resetRequest);
    }

    @Override
    public ResponseEntity<ApiResponse> userByStoreId(Integer storeId) {
        return userService.userByStoreId(storeId);
    }


    @Override
    public ResponseEntity<ApiResponse> logoutUser(String sessionToken) {
        return userService.logoutUser(sessionToken);
    }
    @Override
    public ResponseEntity<ApiResponse> updateUser(Long id , SignupRequest signUpRequest) {
        return userService.updateUser(id,signUpRequest);
    }

}
