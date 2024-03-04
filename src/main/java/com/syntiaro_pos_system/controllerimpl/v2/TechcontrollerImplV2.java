package com.syntiaro_pos_system.controllerimpl.v2;

import com.syntiaro_pos_system.controller.v2.TechController;
import com.syntiaro_pos_system.entity.v1.Tech;
import com.syntiaro_pos_system.entity.v2.ApiResponse;
import com.syntiaro_pos_system.request.v1.TechLoginRequest;
import com.syntiaro_pos_system.request.v1.TechSignupRequest;
import com.syntiaro_pos_system.service.v2.TechService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class TechcontrollerImplV2 implements TechController {

    @Autowired
    TechService techService;

    @Override
    public ResponseEntity<ApiResponse> authenticateTech(TechLoginRequest techLoginRequest) {
        return techService.authenticateTech(techLoginRequest);
    }

    @Override
    public ResponseEntity<ApiResponse> registerTech(TechSignupRequest techsignupRequest) {
        return techService.registerTech(techsignupRequest);
    }

    @Override
    public ResponseEntity<ApiResponse> forgotPassword(TechSignupRequest techSignupRequest) {
        return techService.forgotPassword(techSignupRequest);
    }

    @Override
    public ResponseEntity<ApiResponse> resetPassword(Map<String, String> resetRequest) {
        return techService.resetPasseord(resetRequest);
    }


    @Override
    public ResponseEntity<ApiResponse> updateTech(Long technicianId, Tech tech) {
        return techService.updateTech(technicianId,tech);
    }

    @Override
    public ResponseEntity<ApiResponse> logoutTech(String sessionToken) {
        return techService.logoutTech(sessionToken);
    }

}
