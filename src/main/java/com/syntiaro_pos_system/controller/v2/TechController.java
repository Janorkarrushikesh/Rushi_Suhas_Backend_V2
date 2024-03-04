package com.syntiaro_pos_system.controller.v2;

import com.syntiaro_pos_system.entity.v1.Tech;
import com.syntiaro_pos_system.entity.v2.ApiResponse;
import com.syntiaro_pos_system.request.v1.TechLoginRequest;
import com.syntiaro_pos_system.request.v1.TechSignupRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;



@RequestMapping("/v2/api/auth/tech")
public interface TechController {

    @PostMapping("/signin")
    public ResponseEntity<ApiResponse> authenticateTech(@Valid @RequestBody TechLoginRequest techLoginRequest) ;

    @PostMapping("/techSignup")
    public ResponseEntity<ApiResponse> registerTech(@Valid @RequestBody TechSignupRequest techsignupRequest) ;

    @PostMapping("/forgotPassword")
    public ResponseEntity<ApiResponse> forgotPassword(@RequestBody TechSignupRequest techSignupRequest) ;

    @PostMapping("/resetPassword")
    public ResponseEntity<ApiResponse> resetPassword(@RequestBody Map<String, String> resetRequest) ;

    @PatchMapping("/{technicianId}")
    public ResponseEntity<ApiResponse> updateTech(@PathVariable Long technicianId , @RequestBody Tech tech);

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse> logoutTech(@RequestParam String sessionToken);

}
