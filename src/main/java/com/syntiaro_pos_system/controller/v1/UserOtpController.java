package com.syntiaro_pos_system.controller.v1;

import com.syntiaro_pos_system.entity.v1.UserOtpEntity;
import com.syntiaro_pos_system.request.v1.UserOtpVerificationRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/v1/sys/otp")
@CrossOrigin(origins = "*", maxAge = 3600)
public interface UserOtpController {

    @PostMapping(path = "/sends")
    public ResponseEntity<String> sendOtp(@RequestBody UserOtpVerificationRequest request) ;

    //THIS METHOD IS USE FOR OTP VERIFICATION....USE....
    @PostMapping("/verify")
    public ResponseEntity<String> verifyOtp(@RequestBody UserOtpEntity request) ;

    //THIS METHOD IS USE FOR FIND THE DETAILS BY ID
    @GetMapping(path = "/mail/{id}")
    public Optional<UserOtpEntity> getbillbyid(@PathVariable Long id) ;

    //THIS METHOD IS USE FOR FIND THE LAST EMAIL DETAILS
    @GetMapping("/get/{email}")
    public ResponseEntity<UserOtpEntity> getLatestOtpByEmail(@PathVariable String email) ;

}
