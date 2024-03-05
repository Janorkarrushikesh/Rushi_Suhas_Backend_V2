package com.syntiaro_pos_system.serviceimpl.v2;

import com.syntiaro_pos_system.entity.v1.ERole;
import com.syntiaro_pos_system.entity.v1.SuperAdmin;
import com.syntiaro_pos_system.entity.v1.SuperAdminRole;
import com.syntiaro_pos_system.entity.v2.ApiResponse;
import com.syntiaro_pos_system.repository.v2.*;
import com.syntiaro_pos_system.request.v1.SuperAdminLoginRequest;
import com.syntiaro_pos_system.request.v1.SuperAdminSignupRequest;
import com.syntiaro_pos_system.response.SuperAdminJwtResponse;
import com.syntiaro_pos_system.security.jwt.SuperAdminJwtUtils;
import com.syntiaro_pos_system.security.services.EmailSenderService;
import com.syntiaro_pos_system.security.services.SuperAdminDetailsImpl;
import com.syntiaro_pos_system.service.v2.SuperAdminService;
import com.syntiaro_pos_system.utils.EmailUsernameValidation;
import com.syntiaro_pos_system.utils.OTPUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class SuperAdminServiceImplV2 implements SuperAdminService {
    private static final int MAX_SESSIONS_PER_USER = 500;
    private final Map<String, Set<String>> userSessions = new ConcurrentHashMap<>();
    private final Map<String, String> emailToOtpMap = new HashMap<>();
    @Autowired
    UserRepositoryV2 userRepository;
    @Autowired
    StoreRepositry storeRepositry;
    @Autowired
    TechRepositoryV2 techRepository;
    @Autowired
    SuperAdminRepositoryV2 superAdminRepository;
    @Autowired
    SuperAdminRoleRepositoryV2 superAdminRoleRepository;
    @Autowired
    EmailSenderService emailSenderService;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    SuperAdminJwtUtils superAdminJwtUtils;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    EmailUsernameValidation validation;
    @Autowired
    JavaMailSender javaMailSender;

    @Override
    public ResponseEntity<ApiResponse> authenticateSuperAdmin(SuperAdminLoginRequest superAdminLoginRequest) {
        try {

            String username = superAdminLoginRequest.getUsername();
            if (hasExceededSessionLimit(username)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(null, false, "Too many active sessions for this user.", 401));
            }

            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(superAdminLoginRequest.getUsername(), superAdminLoginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = superAdminJwtUtils.generateJwtToken(authentication);

            // // Add the new session token to the active sessions map
            addUserSession(username, jwt); // ADDED BY RUSHIKEH
            SuperAdminDetailsImpl userDetails = (SuperAdminDetailsImpl) authentication.getPrincipal();
            List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority()).collect(Collectors.toList());
            // Assuming you have a logo URL field in the userDetails
            Optional<SuperAdmin> techOptional = superAdminRepository.findByUsername(superAdminLoginRequest.getUsername());

            String logoUrl = null; // Default value if logo URL is not present
            String storeName = null; // Default value if store name is not present

            if (techOptional.isPresent()) {
                SuperAdmin superAdmin = techOptional.get();
            }
            return ResponseEntity.ok().body(new ApiResponse(new SuperAdminJwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(), userDetails.getGstno(), roles), true, 200));
//            return null;

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(null, false, "...", 500));
        }
    }

    private boolean hasExceededSessionLimit(String username) {
        return userSessions.getOrDefault(username, Collections.emptySet()).size() >= MAX_SESSIONS_PER_USER;
    }

    private void addUserSession(String username, String sessionToken) {
        userSessions.computeIfAbsent(username, k -> new HashSet<>()).add(sessionToken);
    }

// ************************************Sign Up Super Admin *********************

    @Override
    public ResponseEntity<ApiResponse> registerSuperAdmin(SuperAdminSignupRequest superAdminSignupRequest) {
        try {
            if (validation.isDuplicateUsername(superAdminSignupRequest.getUsername()) || validation.isDuplicateEmail(superAdminSignupRequest.getEmail())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(null, false, "Error: Username or email is already taken!", 400));
            }
            String hashedPassword = encoder.encode(superAdminSignupRequest.getPassword());
            if (superAdminSignupRequest.getPassword().equals(superAdminSignupRequest.getComfirmpassword())) {
                // Create new user's account
                SuperAdmin user = new SuperAdmin(superAdminSignupRequest.getUsername(), superAdminSignupRequest.getSaddress(), superAdminSignupRequest.getEmail(), superAdminSignupRequest.getContact(), superAdminSignupRequest.getDate(), superAdminSignupRequest.getCountry(), superAdminSignupRequest.getState(), encoder.encode(superAdminSignupRequest.getPassword()), superAdminSignupRequest.getComfirmpassword());

                Set<SuperAdminRole> superAdminRoles = new HashSet<>();
                SuperAdminRole superADSuperAdminRole = superAdminRoleRepository.findByName(ERole.ROLE_SUPER_ADMIN).orElseThrow(() -> new RuntimeException("Error: SuperAdminRole is not found."));
                superAdminRoles.add(superADSuperAdminRole);


                user.setSuperAdminRoles(superAdminRoles);
                user.setComfirmPassword(superAdminSignupRequest.getComfirmpassword());
                SuperAdmin superAdmin = superAdminRepository.save(user);

                emailSenderService.sendRegistrationSuccessfulEmailadmin(user.getEmail(), user.getUsername(), superAdminSignupRequest.getPassword());

                return ResponseEntity.ok().body(new ApiResponse(superAdmin, true, "SuperAdmin registered successfully!", 200));
            } else {

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(null, false, "PASSWORD DOES NOT MATCH!!!!!!", 400));

            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(null, false, "...", 500));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> forgotPassword(SuperAdminSignupRequest superAdminSignupRequest) {
        try {
            String email = superAdminSignupRequest.getEmail();
            Optional<SuperAdmin> existingSuperAdmin = superAdminRepository.findByEmail(email);
            if (existingSuperAdmin.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(null, false, "SuperAdmin account not found for the given email", 400));
            }
            String otp = OTPUtil.generateOTP(6);
            //put in emailToOtpMap
            emailToOtpMap.put(email, otp);
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("OTP Verification for Password Reset");
            message.setText("Your OTP for password reset is: " + otp + "(Valid for 5 Mins.)" + "\n" + "Your Reset Super Admin Password:-" + "https://prod.ubsbill.com/superresetpassword");
            javaMailSender.send(message);
            return ResponseEntity.ok().body(new ApiResponse(null, true, "OTP sent successfully to the provided email address", 200));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(null, false, "..", 500));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> resetPassword(Map<String, String> resetRequest) {

        try {
            String email = resetRequest.get("emailID");
            String otp = resetRequest.get("oneTimePassword");
            String password = resetRequest.get("password");
            String confirmPassword = resetRequest.get("confirmPassword");

            // Check if the provided email exists in the emailToOtpMap and the OTP matches
            if (!emailToOtpMap.containsKey(email) || !emailToOtpMap.get(email).equals(otp)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(null, false, "Invalid OTP or Email", 400));
            }
            // Check if the new password and confirmation match
            if (!password.equals(confirmPassword)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(null, false, "New password and confirmation do not match", 400));
            }
            // Check if the newPassword is not null and not empty
            if (StringUtils.isEmpty(password)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(null, false, "New password cannot be empty", 400));
            }

            //update Password
            Optional<SuperAdmin> existingSuperAdmin = superAdminRepository.findByEmail(email);
            if (existingSuperAdmin.isPresent()) {
                SuperAdmin superAdmin = existingSuperAdmin.get();
                superAdmin.setPassword(encoder.encode(password));
                superAdmin.setComfirmPassword(password);
                superAdminRepository.save(superAdmin);

                emailSenderService.sendPasswordChangedEmail(email, password);

            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(null, false, "Failed to update password", 400));
            }
            emailToOtpMap.remove(email);
            return ResponseEntity.ok().body(new ApiResponse(null, true, "Password updated successfully", 200));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(null, false, "..", 500));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> updateSuperAdmin(Long superid, SuperAdmin updateSuperAdmin) {
        try {
            Optional<SuperAdmin> existingSuperAdmin = superAdminRepository.findById(superid);
            if (existingSuperAdmin.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(null, false, "Given Id Not Found FOr SuperAdmin !", 404));
            }
            SuperAdmin superAdmin = existingSuperAdmin.get();
            if ((!superAdmin.getUsername().equals(updateSuperAdmin.getUsername()) && superAdminRepository.existsByUsername(updateSuperAdmin.getUsername()))) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(null, false, "Error : Username is already taken ", 400));
            }
            if ((!superAdmin.getEmail().equals(updateSuperAdmin.getEmail()) && superAdminRepository.existsByEmail(updateSuperAdmin.getEmail()))) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(null, false, "Error : Email is already taken ", 400));
            }
            superAdmin.setUsername(updateSuperAdmin.getUsername() != null ? updateSuperAdmin.getUsername() : superAdmin.getUsername());
            superAdmin.setEmail(updateSuperAdmin.getEmail() != null ? updateSuperAdmin.getEmail() : superAdmin.getEmail());
            superAdmin.setAddress(updateSuperAdmin.getAddress() != null ? updateSuperAdmin.getAddress() : superAdmin.getAddress());
            superAdmin.setContact(updateSuperAdmin.getContact() != null ? updateSuperAdmin.getContact() : superAdmin.getContact());
            superAdmin.setGstNo(updateSuperAdmin.getGstNo() != null ? updateSuperAdmin.getGstNo() : superAdmin.getGstNo());
            superAdmin.setCountry(updateSuperAdmin.getCountry() != null ? updateSuperAdmin.getCountry() : superAdmin.getCountry());
            superAdmin.setState(updateSuperAdmin.getState() != null ? updateSuperAdmin.getState() : superAdmin.getState());
            return ResponseEntity.ok().body(new ApiResponse(superAdminRepository.save(superAdmin), true, "Super Admin Updated SuccessFully !", 200));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(null, false, "..", 500));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> logoutSuperAdmin(String sessionToken) {
        try {
            String username = getUsernameFromSessionToken(sessionToken);
            if (username == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(null, false, "Invalid session token.", 401));
            }
            removeUserSession(username, sessionToken);
            return ResponseEntity.ok().body(new ApiResponse(null, true, "Logged out successfully.", 200));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(null, false, "..", 500));
        }
    }

    private void removeUserSession(String username, String sessionToken) {
        userSessions.getOrDefault(username, Collections.emptySet()).remove(sessionToken);
    }

    private String getUsernameFromSessionToken(String sessionToken) {
        for (Map.Entry<String, Set<String>> entry : userSessions.entrySet()) {
            if (entry.getValue().contains(sessionToken)) {
                return entry.getKey();
            }
        }
        return null;
    }
}
