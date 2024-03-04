package com.syntiaro_pos_system.controller.v1;

import com.syntiaro_pos_system.entity.v1.OtpEntity;
import com.syntiaro_pos_system.request.v1.OtpVerificationRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/v1/sys/otp")
@CrossOrigin(origins = "*", maxAge = 3600)
public interface OtpController {

    @PostMapping(path = "/store/send")
    public ResponseEntity<String> sendOtp(@RequestBody String email) ;

    // THIS METHOD IS USE FOR OTP VERIFICATION .....USE.....
    @PostMapping("/store/verify")
    public ResponseEntity<String> verifyOtp(@RequestBody OtpEntity request) ;

    // THIS METHOD IS USE FOR FIND DETAILS BY ID
    @GetMapping(path = "/mail/store/{id}")
    public Optional<OtpEntity> getbillbyid(@PathVariable Long id) ;
    // THIS METHOD IS USE FOR SEND VALID EMAIL FORM....USE....
    @PostMapping(path = "/store/sends")
    public ResponseEntity<String> sendOtp(@RequestBody OtpVerificationRequest request) ;

    // THIS METHOD IS USE FOR FIND THE LAST EMAIL DETAILS
    @GetMapping("/get/store/{email}")
    public ResponseEntity<OtpEntity> getLatestOtpByEmail(@PathVariable String email) ;
}
