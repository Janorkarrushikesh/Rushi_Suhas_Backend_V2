package com.syntiaro_pos_system.controllerimpl.v1;


import com.syntiaro_pos_system.controller.v1.UserOtpController;
import com.syntiaro_pos_system.entity.v1.UserOtpEntity;
import com.syntiaro_pos_system.repository.v1.UserOtpRepository;
import com.syntiaro_pos_system.request.v1.UserOtpVerificationRequest;
import com.syntiaro_pos_system.security.services.OtpService;
import com.syntiaro_pos_system.utils.OTPUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
public class UserOtpControllerImpl implements UserOtpController {
    @Autowired
    private OtpService otpService;

    private final UserOtpRepository userOtpRepository;

    public UserOtpControllerImpl(OtpService otpService, UserOtpRepository userOtpRepository) {
        this.otpService = otpService;
        this.userOtpRepository = userOtpRepository;
    }

    //THIS METHOD IS USE FOR SEND THE VALIDE EMAIL FORM....USE....
    @Override
    public ResponseEntity<String> sendOtp(@RequestBody UserOtpVerificationRequest request) {

        String email = request.getEmail();
        String otp = OTPUtil.generateOTP(6);
        LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(5);
        UserOtpEntity userOtpEntity = new UserOtpEntity();
        userOtpEntity.setEmail(String.valueOf(email));
        userOtpEntity.setOtp(otp);
        userOtpEntity.setExpirationTime(expirationTime);
        userOtpRepository.save(userOtpEntity);
        otpService.sendOtp(String.valueOf(email), otp);
        return ResponseEntity.ok("OTP sent successfully");
    }

    //THIS METHOD IS USE FOR OTP VERIFICATION....USE....
    @Override
    public ResponseEntity<String> verifyOtp(@RequestBody UserOtpEntity request) {
        String email = request.getEmail();
        String otp = request.getOtp();
        Pageable pageable = PageRequest.of(0, 1, Sort.by("expirationTime").descending());
        List<UserOtpEntity> otpEntities = userOtpRepository.findLatestByEmail(email, pageable);
        if (otpEntities.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid OTP" + email + "---" + otp);
        }
        UserOtpEntity userOtpEntity = otpEntities.get(0);
        LocalDateTime currentDateTime = LocalDateTime.now();
        if (currentDateTime.isAfter(userOtpEntity.getExpirationTime())) {
            return ResponseEntity.badRequest().body("OTP has expired");
        }
        if (otp.equals(userOtpEntity.getOtp())) {
            return ResponseEntity.ok("OTP verified successfully");
        } else {
            return ResponseEntity.badRequest().body("Invalid OTP" + userOtpEntity.getOtp());
        }
    }

    //THIS METHOD IS USE FOR FIND THE DETAILS BY ID
    @Override
    public Optional<UserOtpEntity> getbillbyid(@PathVariable Long id) {
        return userOtpRepository.findById(id);
    }

    //THIS METHOD IS USE FOR FIND THE LAST EMAIL DETAILS
    @Override
    public ResponseEntity<UserOtpEntity> getLatestOtpByEmail(@PathVariable String email) {
        Pageable pageable = PageRequest.of(0, 1, Sort.by("expirationTime").descending());
        List<UserOtpEntity> otpEntities = userOtpRepository.findLatestByEmail(email, pageable);
        if (otpEntities.isEmpty()) {
            System.out.println("No UserOtpEntity found for email: " + email);
            return ResponseEntity.notFound().build();
        }
        UserOtpEntity latestUserOtpEntity = otpEntities.get(0);
        System.out.println("Latest UserOtpEntity for email: " + email);
        System.out.println(latestUserOtpEntity.toString());
        return ResponseEntity.ok(latestUserOtpEntity);
    }





}



