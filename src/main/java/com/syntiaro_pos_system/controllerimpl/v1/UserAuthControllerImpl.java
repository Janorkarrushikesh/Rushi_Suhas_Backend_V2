package com.syntiaro_pos_system.controllerimpl.v1;

import com.syntiaro_pos_system.controller.v1.UserAuthController;
import com.syntiaro_pos_system.entity.v1.ERole;
import com.syntiaro_pos_system.entity.v1.User;
import com.syntiaro_pos_system.entity.v1.UserRole;
import com.syntiaro_pos_system.repository.v1.*;
import com.syntiaro_pos_system.request.v1.LoginRequest;
import com.syntiaro_pos_system.request.v1.SignupRequest;
import com.syntiaro_pos_system.response.JwtUserResponse;
import com.syntiaro_pos_system.response.MessageResponse;
import com.syntiaro_pos_system.response.MessageUserResponse;
import com.syntiaro_pos_system.security.jwt.JwtUtils;
import com.syntiaro_pos_system.security.services.EmailSenderService;
import com.syntiaro_pos_system.security.services.UserDetailsImpl;
import com.syntiaro_pos_system.utils.OTPUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@RestController
public class UserAuthControllerImpl implements UserAuthController {
    private static final int MAX_SESSIONS_PER_USER = 500;
    /// {----------------------MADE BY RUSHIKESH-----------------START
    /// HERE--------------------}
    private final Map<String, Set<String>> userSessions = new ConcurrentHashMap<>();
    private final Map<String, String> emailToOtpMap = new HashMap<>();
    @Autowired
    AuthenticationManager authenticationManagers;
    @Autowired
    StoreRepository storeRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    TechRepository techRepository;
    @Autowired
    SuperAdminRepository superAdminRepository;
    @Autowired
    UserRoleRepository userRoleRepository;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    EmailSenderService emailSenderService;

    /// {----------------------MADE BY RUSHIKESH-----------------END
    /// HETE--------------------}
    @Autowired
    JavaMailSender javaMailSender;
    private final String otp = OTPUtil.generateOTP(6);

    // THIS METHOD IS USE FOR USER SIGNIN
    @Override
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        // ADDED BY RUSHIKESH --START--
        String username = loginRequest.getUsername();
        if (hasExceededSessionLimit(username)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Too many active sessions for this user.");

        }

        // --END--

        Authentication authentication = authenticationManagers.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        // // Add the new session token to the active sessions map
        addUserSession(username, jwt); // ADDED BY RUSHIKEH

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtUserResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getRegistno(), // added this code
                userDetails.getEmail(),
                roles,
                userDetails.getStoreid(),
                userDetails.getGstno(),
                userDetails.getCurrency()));
    }

    /// {----------------------MADE THIS CHANGES RUSHIKESH-----------------START
    /// HERE--------------------}

    // Helper methods to manage user sessions
    private boolean hasExceededSessionLimit(String username) {
        return userSessions.getOrDefault(username, Collections.emptySet()).size() >= MAX_SESSIONS_PER_USER;
    }

    private void addUserSession(String username, String sessionToken) {
        userSessions.computeIfAbsent(username, k -> new HashSet<>()).add(sessionToken);
    }

    /// {----------------------MADE THIS CHANGES RUSHIKESH-----------------END
    /// HERE--------------------}

    // THIS METHOD IS USE FOR USER SIGNUP
    @Override
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (storeRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageUserResponse("Error: Username is already taken!"));
        }

        if (techRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (superAdminRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageUserResponse("Error: Email is already in use!"));
        }

        if (superAdminRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        if (storeRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        if (techRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        if (userRepository.existsByContact(signUpRequest.getContact())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Contact is already in use!"));
        }

        if (userRepository.existsByContact(signUpRequest.getContact())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Contact is already in use!"));
        }

        if (signUpRequest.getPassword().equals(signUpRequest.getComfirmpassword())) {
            // Create new user's account
            User user = new User(signUpRequest.getUsername(),
                    signUpRequest.getEmail(),
                    encoder.encode(signUpRequest.getPassword()),
                    signUpRequest.getCrtby(),
                    signUpRequest.getStoreid(),
                    signUpRequest.getRegisterDate(),
                    signUpRequest.getRegistno(), // added this line
                    signUpRequest.getGstno(),
                    signUpRequest.getUpdby(),
                    signUpRequest.getCrtDate(),
                    signUpRequest.getUpdateDate(),
                    signUpRequest.getAddress(),
                    signUpRequest.getComfirmpassword(),
                    signUpRequest.getContact(),
                    signUpRequest.getId(),
                    signUpRequest.getCurrency()

            );

            // ------------------------ THIS CODE EDIT BY RUSHIKESH
            // -----------------------------

            Long lastBillNumber = userRepository.findLastNumberForStore(signUpRequest.getStoreid()); // ------ THIS CODE EDIT
            // BY RUSHIKESH -------
            user.setId(lastBillNumber != null ? lastBillNumber + 1 : 1); // ----- THIS CODE EDIT BY RUSHIKESH -----

            Set<String> strRoles = signUpRequest.getRole();
            Set<UserRole> userRoles = new HashSet<>();

            if (strRoles == null) {
                UserRole userRole = userRoleRepository.findByName(ERole.ROLE_USER)
                        .orElseThrow(() -> new RuntimeException("Error: UserRole is not found."));
                userRoles.add(userRole);
            } else {
                strRoles.forEach(role -> {
                    switch (role) {
                        case "admin":
                            UserRole adminUserRole = userRoleRepository.findByName(ERole.ROLE_ADMIN)
                                    .orElseThrow(() -> new RuntimeException("Error: UserRole is not found."));
                            userRoles.add(adminUserRole);

                            break;
                        case "mod":
                            UserRole modUserRole = userRoleRepository.findByName(ERole.ROLE_MODERATOR)
                                    .orElseThrow(() -> new RuntimeException("Error: UserRole is not found."));
                            userRoles.add(modUserRole);

                            break;
                        default:
                            UserRole userRole = userRoleRepository.findByName(ERole.ROLE_USER)
                                    .orElseThrow(() -> new RuntimeException("Error: UserRole is not found."));
                            userRoles.add(userRole);
                    }
                });
            }

            user.setRoles(userRoles);
            user.setComfirmPassword(signUpRequest.getComfirmpassword());
            userRepository.save(user);

            emailSenderService.sendRegistrationSuccessfulEmailuser(user.getEmail(), user.getUsername(),
                    signUpRequest.getPassword());

            return ResponseEntity.ok(new MessageUserResponse("User registered successfully!"));
        } else {

            return ResponseEntity.ok(new MessageUserResponse("PASSWORD DOES NOT MATCH!!!!!!"));

        }
    }

    // THIS METHOD IS USE FOR UPDATE USER
    @Override
    public ResponseEntity<?> updateUser(@PathVariable("id") Long userId, @RequestBody SignupRequest signUpRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

// condition changed because it is taking own username and email while updating
        if (!signUpRequest.getUsername().equals(user.getUsername()) && userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (!signUpRequest.getEmail().equals(user.getEmail()) && userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }


        if (storeRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }


        if (techRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (superAdminRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }


        if (superAdminRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        if (storeRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        if (techRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        if (signUpRequest.getUsername() != null) {
            user.setUsername(signUpRequest.getUsername());
        }
        if (signUpRequest.getEmail() != null) {
            user.setEmail(signUpRequest.getEmail());
        }

        if (signUpRequest.getAddress() != null) {
            user.setAddress(signUpRequest.getAddress());
        }
        if (signUpRequest.getPassword() != null) {
            user.setPassword(encoder.encode(signUpRequest.getPassword()));
        }
        if (signUpRequest.getContact() != null) {
            user.setContact(signUpRequest.getContact());
        }

        Set<String> strRoles = signUpRequest.getRole();
        Set<UserRole> userRoles = new HashSet<>();

        if (strRoles != null) {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        UserRole adminUserRole = userRoleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: UserRole is not found."));
                        userRoles.add(adminUserRole);
                        break;
                    case "mod":
                        UserRole modUserRole = userRoleRepository.findByName(ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Error: UserRole is not found."));
                        userRoles.add(modUserRole);
                        break;
                    default:
                        UserRole userRole = userRoleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: UserRole is not found."));
                        userRoles.add(userRole);
                }
            });
        }

        user.setRoles(userRoles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageUserResponse("User updated successfully!"));
    }


    // THIS METHOD IS USE FOR FETCH USER BY ID
    @Override
    public ResponseEntity<?> fetchUserByID(@PathVariable("id") Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.badRequest().body(new MessageUserResponse("Error: User not found"));
        }
    }

    // THIS METHOD IS USE FOR CHANGE PASSWORD OF USER
    @Override
    public ResponseEntity<String> changePassword(@RequestBody SignupRequest signupRequest) {
        // String username; // Get the username from the authenticated Principal

        String username = signupRequest.getUsername();

        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        User user = optionalUser.get();

        // Verify old password
        if (!encoder.matches(signupRequest.getCurrentPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password is incorrect");
        }

        // Verify new password and confirmation
        String newPassword = signupRequest.getNewPassword();
        String confirmPassword = signupRequest.getComfirmpassword();
        if (!Objects.equals(newPassword, confirmPassword)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("New password and confirmation do not match");
        }

        // Check if the newPassword is not null and not empty
        if (StringUtils.isEmpty(newPassword)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("New password cannot be empty");
        }

        // Update the password
        try {
            user.setPassword(encoder.encode(newPassword));
            user.setComfirmPassword(newPassword);
            userRepository.save(user);
        } catch (Exception e) {
            // Handle any potential exceptions that might occur during password update
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update password");
        }

        return ResponseEntity.ok("Password changed successfully");
    }

    // THIS METHOD IS USE FOR FORGET PASSWORD
    @Override
    public ResponseEntity<?> forgotPassword(@RequestBody SignupRequest signupRequest) {
        String email = signupRequest.getEmail();

        // Check if the email is associated with any existing store account
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Store account not found for the given email");
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


        return ResponseEntity.ok("OTP sent successfully to the provided email address");
    }

    // THIS METHOD IS USED FOR VERIFYING THE OTP AND UPDATING THE PASSWORD
    @Override
    public ResponseEntity<String> resetPassword(@RequestBody Map<String, String> resetRequest) {
        String email = resetRequest.get("email");
        String otp = resetRequest.get("otp");
        String newPassword = resetRequest.get("newPassword");
        String confirmPassword = resetRequest.get("comfirmPassword");

        // Check if the provided email exists in the emailToOtpMap and the OTP matches
        if (!emailToOtpMap.containsKey(email) || !emailToOtpMap.get(email).equals(otp)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid OTP or Email");
        }

        // Check if the new password and confirmation match
        if (!Objects.equals(newPassword, confirmPassword)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("New password and confirmation do not match");
        }

        // Check if the newPassword is not null and not empty
        if (StringUtils.isEmpty(newPassword)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("New password cannot be empty");
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
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update password");
        }

        // Remove the email entry from the emailToOtpMap after successful password
        // update
        emailToOtpMap.remove(email);

        return ResponseEntity.ok("Password updated successfully");
    }

    // THIS METHOD IS USE FOR DELETE USER BY ID
    @Override
    public ResponseEntity<?> deleteUser(@PathVariable("Serial_no") Integer Serial_no) {
        try {
            userRepository.deleteById(Long.valueOf(Serial_no));
            return ResponseEntity.ok(new MessageUserResponse("User deleted successfully!"));
        } catch (EmptyResultDataAccessException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageUserResponse("Error: User not found"));
        }
    }

    @Override
    public ResponseEntity<List<User>> getUsersByStoreId(@PathVariable Integer storeid) {
        List<User> users = userRepository.findByStoreId(storeid);
        if (users.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(users);
    }

    @Override
    public ResponseEntity<?> logoutUser(@RequestParam String sessionToken) {
        String username = getUsernameFromSessionToken(sessionToken);
        if (username == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid session token.");
        }

        // Remove the session token from the active sessions map
        removeUserSession(username, sessionToken);

        // Optionally, you can invalidate the JWT token here (e.g., by blacklisting it)

        return ResponseEntity.ok("Logged out successfully.");
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
