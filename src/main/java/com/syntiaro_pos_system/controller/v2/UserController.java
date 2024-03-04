package com.syntiaro_pos_system.controller.v2;

import com.syntiaro_pos_system.entity.v2.ApiResponse;
import com.syntiaro_pos_system.request.v1.LoginRequest;
import com.syntiaro_pos_system.request.v1.SignupRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RequestMapping("/v2/api/auth/user")
@CrossOrigin(origins = "*", maxAge = 3600)
public interface UserController {

    @PostMapping("/signin")
    public ResponseEntity<ApiResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest);

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse> registerUser(@Valid @RequestBody SignupRequest signUpRequest) ;
    @PostMapping("/forgotPassword")
    public ResponseEntity<ApiResponse> forgotPassword(@RequestBody SignupRequest signupRequest);

    @PostMapping("/resetPassword")
    public ResponseEntity<ApiResponse> resetPassword(@RequestBody Map<String, String> resetRequest) ;

    @GetMapping("store/{storeId}")
    public ResponseEntity<ApiResponse> userByStoreId(@PathVariable Integer storeId);

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse> logoutUser(@RequestParam String sessionToken);

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse> updateUser(@PathVariable Long id , @Valid @RequestBody SignupRequest signUpRequest);
}
