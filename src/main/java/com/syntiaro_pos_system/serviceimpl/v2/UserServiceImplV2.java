package com.syntiaro_pos_system.serviceimpl.v2;

import com.syntiaro_pos_system.entity.v1.ERole;
import com.syntiaro_pos_system.entity.v1.User;
import com.syntiaro_pos_system.entity.v1.UserRole;
import com.syntiaro_pos_system.entity.v2.ApiResponse;
import com.syntiaro_pos_system.repository.v2.*;
import com.syntiaro_pos_system.request.v1.LoginRequest;
import com.syntiaro_pos_system.request.v1.SignupRequest;
import com.syntiaro_pos_system.response.JwtUserResponse;
import com.syntiaro_pos_system.security.jwt.JwtUtils;
import com.syntiaro_pos_system.security.services.EmailSenderService;
import com.syntiaro_pos_system.security.services.UserDetailsImpl;
import com.syntiaro_pos_system.service.v2.UserService;
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

import javax.persistence.EntityNotFoundException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class UserServiceImplV2 implements UserService {

    private static final int MAX_SESSIONS_PER_USER = 500;
    private final Map<String, Set<String>> userSessions = new ConcurrentHashMap<>();
    @Autowired
    UserRepositoryV2 userRepository;
    @Autowired
    StoreRepositry storeRepositry;
    @Autowired
    TechRepositoryV2 techRepository;
    @Autowired
    SuperAdminRepositoryV2 superAdminRepository;
    @Autowired
    AuthenticationManager authenticationManagers;
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    EmailSenderService emailSenderService;
    @Autowired
    JavaMailSender javaMailSender;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    UserRoleRepositoryV2 userRoleRepository;
    @Autowired
    EmailUsernameValidation validation;

    private final Map<String, String> emailToOtpMap = new HashMap<>();

    @Override
    public ResponseEntity<ApiResponse> authenticateUser(LoginRequest loginRequest) {
        try {

            String username = loginRequest.getUsername();
            if (hasExceededSessionLimit(username)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(null, false, "Too many active sessions for this user.", 401));
            }
            Authentication authentication = authenticationManagers.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);

            // // Add the new session token to the active sessions map
            addUserSession(username, jwt);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority()).collect(Collectors.toList());

            return ResponseEntity.ok().body(new ApiResponse(new JwtUserResponse(jwt, userDetails.getId(), userDetails.getUsername(), userDetails.getRegistno(), // added this code
                    userDetails.getEmail(), roles, userDetails.getStoreid(), userDetails.getGstno(), userDetails.getCurrency()), true, 200));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(null, false, "...", 500));
        }


    }

    @Override
    public ResponseEntity<ApiResponse> registerUser(SignupRequest signUpRequest) {

        try {
            if (validation.isDuplicateUsername(signUpRequest.getUsername()) || validation.isDuplicateEmail(signUpRequest.getEmail())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(null, false, "Error: Username or email is already taken!", 400));
            }

            if (userRepository.existsByContact(signUpRequest.getContact())) {

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(null, false, "Error: Contact is already in use!", 400));
            }

            if (signUpRequest.getPassword().equals(signUpRequest.getComfirmpassword())) {
                // Create new user's account
                User user = new User(signUpRequest.getUsername(), signUpRequest.getEmail(), encoder.encode(signUpRequest.getPassword()), signUpRequest.getCrtby(), signUpRequest.getStoreid(), signUpRequest.getRegisterDate(), signUpRequest.getRegistno(), // added this line
                        signUpRequest.getGstno(), signUpRequest.getUpdby(), signUpRequest.getCrtDate(), signUpRequest.getUpdateDate(), signUpRequest.getAddress(), signUpRequest.getComfirmpassword(), signUpRequest.getContact(), signUpRequest.getId(), signUpRequest.getCurrency());

                Long lastBillNumber = userRepository.findLastNumberForStore(signUpRequest.getStoreid()); // ------ THIS CODE EDIT
                user.setId(lastBillNumber != null ? lastBillNumber + 1 : 1); // ----- THIS CODE EDIT BY RUSHIKESH -----

                Set<UserRole> userRoles = new HashSet<>();
                UserRole userRole = userRoleRepository.findByName(ERole.ROLE_USER).orElseThrow(() -> new RuntimeException("Error: UserRole is not found."));
                userRoles.add(userRole);
                user.setRoles(userRoles);
                user.setComfirmPassword(signUpRequest.getComfirmpassword());

                emailSenderService.sendRegistrationSuccessfulEmailuser(user.getEmail(), user.getUsername(), signUpRequest.getPassword());

                return ResponseEntity.ok().body(new ApiResponse(userRepository.save(user), true, "User registered successfully!", 200));
            } else {

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(null, false, "PASSWORD DOES NOT MATCH!!!!!!", 400));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(null, false, "...", 500));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> resetPassword(Map<String, String> resetRequest) {
        try {
            String email = resetRequest.get("emailID");
            String otp = resetRequest.get("oneTimePassword");
            String newPassword = resetRequest.get("password");
            String confirmPassword = resetRequest.get("confirmPassword");

            // Check if the provided email exists in the emailToOtpMap and the OTP matches
            if (!emailToOtpMap.containsKey(email) || !emailToOtpMap.get(email).equals(otp)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(null, false, "Invalid OTP or Email", 400));
            }
            // Check if the new password and confirmation match
            if (!Objects.equals(newPassword, confirmPassword)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(null, false, "New password and confirmation do not match", 400));
            }

            // Check if the newPassword is not null and not empty
            if (StringUtils.isEmpty(newPassword)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(null, false, "New password cannot be empty", 400));
            }
            // Update the password in the database
            Optional<User> optionalUser = userRepository.findByEmail(email);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                user.setPassword(encoder.encode(newPassword));
                user.setComfirmPassword(newPassword);
                userRepository.save(user);

                emailSenderService.sendPasswordChangedEmail(email, newPassword);

            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(null, false, "Failed to update password", 500));
            }

            // Remove the email entry from the emailToOtpMap after successful password
            emailToOtpMap.remove(email);

            return ResponseEntity.ok().body(new ApiResponse(null, true, "Password updated successfully", 200));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(null, false, "...", 500));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> userByStoreId(Integer storeId) {
        try {
            List<User> userList = userRepository.findByStoreId(storeId);
            List<Map<String, Object>> userData = new ArrayList<>();
            for (User user : userList) {
                Map<String, Object> userMap = new LinkedHashMap<>();
                userMap.put("id", user.getId());
                userMap.put("username", user.getUsername());
                userMap.put("email", user.getEmail());
                userMap.put("address", user.getAddress());
                userMap.put("contact", user.getContact());
                userData.add(userMap);
            }
            return ResponseEntity.ok().body(new ApiResponse(userData, true, 200));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(null, false, 400));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> forgotPassword(SignupRequest signupRequest) {
        try {
            String email = signupRequest.getEmail();
            // Check if the email is associated with any existing store account
            Optional<User> optionalUser = userRepository.findByEmail(email);
            if (optionalUser.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(null, false, "Store account not found for the given email", 404));
            }
            // Generate OTP
            String otp = OTPUtil.generateOTP(6);
            // Save the OTP in the emailToOtpMap to verify later
            emailToOtpMap.put(email, otp);

            // Send OTP via email
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("OTP Verification for Password Reset");
            message.setText("Your OTP for password reset is: " + otp + "(Valid for 5 Mins.)" + "\n" + "Your Reset User Password:-" + "https://prod.ubsbill.com/resetuserpassword");
            javaMailSender.send(message);
            return ResponseEntity.ok().body(new ApiResponse(null, true, "OTP sent successfully to the provided email address", 200));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(null, false, "...", 500));
        }

    }

    // Helper methods to manage user sessions
    private boolean hasExceededSessionLimit(String username) {
        return userSessions.getOrDefault(username, Collections.emptySet()).size() >= MAX_SESSIONS_PER_USER;
    }

    private void addUserSession(String username, String sessionToken) {
        userSessions.computeIfAbsent(username, k -> new HashSet<>()).add(sessionToken);
    }

    @Override
    public ResponseEntity<ApiResponse> updateUser(Long id, SignupRequest signUpRequest) {
        try {
            User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User Not Found For Given Id"));

            if ((!signUpRequest.getUsername().equals(user.getUsername()) && userRepository.existsByUsername(signUpRequest.getUsername())) || storeRepositry.existsByUsername(signUpRequest.getUsername()) || techRepository.existsByUsername(signUpRequest.getUsername()) || superAdminRepository.existsByUsername(signUpRequest.getUsername())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(null, false, "Error: Username is already taken", 400));
            }
            if ((!signUpRequest.getEmail().equals(user.getEmail())) && userRepository.existsByEmail(signUpRequest.getEmail()) || storeRepositry.existsByEmail(signUpRequest.getEmail()) || techRepository.existsByEmail(signUpRequest.getEmail()) || superAdminRepository.existsByEmail(signUpRequest.getEmail())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(null, false, "Error email Is already taken ", 400));
            }

            user.setUsername(signUpRequest.getUsername() != null ? signUpRequest.getUsername() : user.getUsername());
            user.setEmail(signUpRequest.getEmail() != null ? signUpRequest.getEmail() : user.getEmail());
            user.setAddress(signUpRequest.getAddress() != null ? signUpRequest.getAddress() : user.getAddress());
            user.setContact(signUpRequest.getContact() != null ? signUpRequest.getContact() : user.getContact());
            user.setPassword(signUpRequest.getPassword() != null ? signUpRequest.getPassword() : user.getPassword());
            if (signUpRequest.getPassword() != null) {
                user.setPassword(encoder.encode(signUpRequest.getPassword()));
            }

            Set<UserRole> userRoles = new HashSet<>();
            UserRole userRole = userRoleRepository.findByName(ERole.ROLE_USER).orElseThrow(() -> new RuntimeException("Error: UserRole is not found."));
            userRoles.add(userRole);

            user.setRoles(userRoles);
            return ResponseEntity.ok().body(new ApiResponse(userRepository.save(user), true, "User Updated Successfully", 200));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(null, false, "...", 500));
        }
    }


    @Override
    public ResponseEntity<ApiResponse> logoutUser(String sessionToken) {
        try {
            String username = getUsernameFromSessionToken(sessionToken);
            if (username == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(null, false, "Invalid session token.", 401));
            }
            removeUserSession(username, sessionToken);
            return ResponseEntity.ok().body(new ApiResponse(null, true, "Logged out successfully.", 200));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(null, false, "...", 500));
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

