package com.syntiaro_pos_system.controllerimpl.v1;

import com.syntiaro_pos_system.controller.v1.SuperAdminController;
import com.syntiaro_pos_system.entity.v1.ERole;
import com.syntiaro_pos_system.entity.v1.SuperAdmin;
import com.syntiaro_pos_system.entity.v1.SuperAdminRole;
import com.syntiaro_pos_system.repository.v1.*;
import com.syntiaro_pos_system.request.v1.SuperAdminLoginRequest;
import com.syntiaro_pos_system.request.v1.SuperAdminSignupRequest;
import com.syntiaro_pos_system.response.MessageResponse;
import com.syntiaro_pos_system.response.MessageUserResponse;
import com.syntiaro_pos_system.response.SuperAdminJwtResponse;
import com.syntiaro_pos_system.security.jwt.SuperAdminJwtUtils;
import com.syntiaro_pos_system.security.services.EmailSenderService;
import com.syntiaro_pos_system.security.services.SuperAdminDetailsImpl;
import com.syntiaro_pos_system.security.services.SuperAdminService;
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
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;


@RestController
public class SuperadminAuthControllerImpl implements SuperAdminController {
    private static final int MAX_SESSIONS_PER_USER = 150;
    /// {----------------------MADE BY RUSHIKESH-----------------START HERE--------------------}
    private final Map<String, Set<String>> userSessions = new ConcurrentHashMap<>();
    private final Map<String, String> emailToOtpMap = new HashMap<>();
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    SuperAdminRepository superAdminRepository;
    @Autowired
    StoreRepository storeRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    TechRepository techRepository;
    @Autowired
    SuperAdminRoleRepository superAdminRoleRepository;
    @Autowired
    SuperAdminService superAdminService;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    SuperAdminJwtUtils superAdminJwtUtils;
    @Autowired
    EmailSenderService emailSenderService;

    /// {----------------------MADE BY RUSHIKESH-----------------END HERE--------------------}
    @Autowired
    JavaMailSender javaMailSender;
    private final String otp = OTPUtil.generateOTP(6);

    // THIS METHOD IS USE FOR STORE SIGNIN
    @Override
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody SuperAdminLoginRequest superAdminLoginRequest) {

        // ADDED BY RUSHIKESH --START--
        String username = superAdminLoginRequest.getUsername();
        if (hasExceededSessionLimit(username)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Too many active sessions for this user.");
        }

        // --END--

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(superAdminLoginRequest.getUsername(),
                        superAdminLoginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = superAdminJwtUtils.generateJwtToken(authentication);

        // // Add the new session token to the active sessions map
        addUserSession(username, jwt); // ADDED BY RUSHIKEH

        SuperAdminDetailsImpl userDetails = (SuperAdminDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        // Assuming you have a logo URL field in the userDetails
        Optional<SuperAdmin> techOptional = superAdminRepository.findByUsername(superAdminLoginRequest.getUsername());

        String logoUrl = null; // Default value if logo URL is not present
        String storeName = null; // Default value if store name is not present

        if (techOptional.isPresent()) {
            SuperAdmin superAdmin = techOptional.get();

        }

        return ResponseEntity.ok(new SuperAdminJwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                userDetails.getGstno(),
                roles)); // Include the logo URL in the response
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

    // THIS METHOD IS USE FOR STORE SIGNUP
    @Override
    public ResponseEntity<?> registerUser(@Valid @RequestBody SuperAdminSignupRequest superAdminSignupRequest) {
        if (storeRepository.existsByUsername(superAdminSignupRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByUsername(superAdminSignupRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageUserResponse("Error: Username is already taken!"));
        }

        if (techRepository.existsByUsername(superAdminSignupRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (superAdminRepository.existsByUsername(superAdminSignupRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (superAdminRepository.existsByEmail(superAdminSignupRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        if (storeRepository.existsByEmail(superAdminSignupRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        if (techRepository.existsByEmail(superAdminSignupRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        if (userRepository.existsByEmail(superAdminSignupRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageUserResponse("Error: Email is already in use!"));
        }
        String hashedPassword = encoder.encode(superAdminSignupRequest.getPassword());
        if (superAdminSignupRequest.getPassword().equals(superAdminSignupRequest.getComfirmpassword())) {
            // Create new user's account
            SuperAdmin user = new SuperAdmin(superAdminSignupRequest.getUsername(),
                    superAdminSignupRequest.getSaddress(),
                    superAdminSignupRequest.getEmail(),
                    superAdminSignupRequest.getContact(),
                    superAdminSignupRequest.getDate(),
                    superAdminSignupRequest.getCountry(),
                    superAdminSignupRequest.getState(),
                    encoder.encode(superAdminSignupRequest.getPassword()),
                    superAdminSignupRequest.getComfirmpassword());

            Set<String> strRoles = superAdminSignupRequest.getRole();
            Set<SuperAdminRole> superAdminRoles = new HashSet<>();

            if (strRoles == null) {
                SuperAdminRole superADSuperAdminRole = superAdminRoleRepository.findByName(ERole.ROLE_SUPER_ADMIN)
                        .orElseThrow(() -> new RuntimeException("Error: SuperAdminRole is not found."));
                superAdminRoles.add(superADSuperAdminRole);
            } else {
                strRoles.forEach(role -> {
                    switch (role) {
                        case "user":
                            SuperAdminRole userSuperAdminRole = superAdminRoleRepository.findByName(ERole.ROLE_USER)
                                    .orElseThrow(() -> new RuntimeException("Error: SuperAdminRole is not found."));
                            superAdminRoles.add(userSuperAdminRole);
                            break;

                        case "mod":
                            SuperAdminRole modSuperAdminRole = superAdminRoleRepository.findByName(ERole.ROLE_MODERATOR)
                                    .orElseThrow(() -> new RuntimeException("Error: SuperAdminRole is not found."));
                            superAdminRoles.add(modSuperAdminRole);
                            break;

                        case "admin":
                            SuperAdminRole adminSuperAdminRole = superAdminRoleRepository.findByName(ERole.ROLE_ADMIN)
                                    .orElseThrow(() -> new RuntimeException("Error: SuperAdminRole is not found."));
                            superAdminRoles.add(adminSuperAdminRole);
                            break;

                        case "support":
                            SuperAdminRole supportSuperAdminRole = superAdminRoleRepository.findByName(ERole.ROLE_SUPPORT)
                                    .orElseThrow(() -> new RuntimeException("Error: SuperAdminRole is not found."));
                            superAdminRoles.add(supportSuperAdminRole);
                            break;

                        default:
                            SuperAdminRole superADSuperAdminRole = superAdminRoleRepository.findByName(ERole.ROLE_SUPER_ADMIN)
                                    .orElseThrow(() -> new RuntimeException("Error: SuperAdminRole is not found."));
                            superAdminRoles.add(superADSuperAdminRole);
                    }
                });
            }

            user.setSuperAdminRoles(superAdminRoles);
            user.setComfirmPassword(superAdminSignupRequest.getComfirmpassword());
            superAdminRepository.save(user);

            emailSenderService.sendRegistrationSuccessfulEmailadmin(user.getEmail(), user.getUsername(),
                    superAdminSignupRequest.getPassword());

            return ResponseEntity.ok(new MessageResponse("SuperAdmin registered successfully!"));
        } else {

            return ResponseEntity.ok(new MessageResponse("PASSWORD DOES NOT MATCH!!!!!!"));

        }

    }

    // THIS METHOD IS USE FOR DELETE STORE
    @Override
    public ResponseEntity<?> deleteStore(@PathVariable("superid") Long techid) {
        Optional<SuperAdmin> storeOptional = superAdminRepository.findById(techid);

        if (storeOptional.isPresent()) {
            SuperAdmin superAdmin = storeOptional.get();

            superAdminRepository.delete(superAdmin);

            return ResponseEntity.ok(new MessageUserResponse("SuperAdmin deleted successfully!"));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // THIS METHOD IS USE FOR FETCH STORE BY ID
    @Override
    public ResponseEntity<SuperAdmin> getStoreById(@PathVariable Long superid) {
        Optional<SuperAdmin> storeOptional = superAdminRepository.findById(superid);

        if (storeOptional.isPresent()) {
            return ResponseEntity.ok(storeOptional.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // THIS METHOD IS USE FOR UPDATE STORE

    // THIS METHOD IS USED FOR VERIFYING THE OTP AND UPDATING THE PASSWORD
    @Override
    public ResponseEntity<String> resetPassword(@RequestBody Map<String, String> resetRequest) {
        String email = resetRequest.get("email");
        String otp = resetRequest.get("otp");
        String newPassword = resetRequest.get("newPassword");
        String confirmPassword = resetRequest.get("comfirmPassword");
        String username = resetRequest.get("username");

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
        Optional<SuperAdmin> optionalStore = superAdminRepository.findByEmail(email);
        if (optionalStore.isPresent()) {
            SuperAdmin superAdmin = optionalStore.get();
            superAdmin.setPassword(encoder.encode(newPassword));
            superAdmin.setComfirmPassword(newPassword);
            superAdminRepository.save(superAdmin);

            emailSenderService.sendPasswordChangedEmail(email, newPassword);

        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update password");
        }

        // Remove the email entry from the emailToOtpMap after successful password
        // update
        emailToOtpMap.remove(email);

        return ResponseEntity.ok("Password updated successfully");
    }

    // THIS METHOD IS USE FOR FORGET PASSWORD
    @Override
    public ResponseEntity<?> forgotPassword(@RequestBody SuperAdminSignupRequest superAdminSignupRequest) {
        String email = superAdminSignupRequest.getEmail();

        // Check if the email is associated with any existing store account
        Optional<SuperAdmin> optionalStore = superAdminRepository.findByEmail(email);
        if (optionalStore.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("SuperAdmin account not found for the given email");
        }

        // Generate OTP
        String otp = OTPUtil.generateOTP(6);

        // Save the OTP in the emailToOtpMap to verify later
        emailToOtpMap.put(email, otp);

        // Send OTP via email
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("OTP Verification for Password Reset");
        message.setText("Your OTP for password reset is: " + otp + "(Valid for 5 Mins.)" + "\n" + "Your Reset Super Admin Password:-" + "https://prod.ubsbill.com/superresetpassword");
        javaMailSender.send(message);
        return ResponseEntity.ok("OTP sent successfully to the provided email address");
    }

    // THIS METHOD IS USE FOR CHANGE PASSWORD OF STORE
    @Override
    public ResponseEntity<String> changePassword(@RequestBody SuperAdminSignupRequest superAdminSignupRequest) {
        String username = superAdminSignupRequest.getUsername();

        Optional<SuperAdmin> optionalStore = superAdminRepository.findByUsername(username);
        if (optionalStore.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("SuperAdmin not found");
        }

        SuperAdmin superAdmin = optionalStore.get();

        // Verify old password
        if (!encoder.matches(superAdminSignupRequest.getCurrentPassword(), superAdmin.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password is incorrect");
        }

        // Verify new password and confirmation
        String newPassword = superAdminSignupRequest.getNewPassword();
        String confirmPassword = superAdminSignupRequest.getComfirmpassword();
        if (!Objects.equals(newPassword, confirmPassword)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("New password and confirmation do not match");
        }

        // Check if the newPassword is not null and not empty
        if (StringUtils.isEmpty(newPassword)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("New password cannot be empty");
        }

        // Update the password and showpass
        try {
            superAdmin.setPassword(encoder.encode(newPassword));
            superAdmin.setComfirmPassword(newPassword);
            superAdminRepository.save(superAdmin);
        } catch (Exception e) {
            // Handle any potential exceptions that might occur during password update
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update password");
        }

        return ResponseEntity.ok("Password changed successfully");
    }

    // THIS METHOD IS USE FOR GET ALL STORE DETAIL
    @Override
    // @PreAuthorize("hasRole('ADMIN')")
    public List<SuperAdmin> getStore() {
        return this.superAdminService.getStore();
    }

    @Override
    public ResponseEntity<?> getUpdatedStoreInfo(@PathVariable Long superid) {
        Optional<SuperAdmin> optionalStore = superAdminRepository.findById(superid);

        if (optionalStore.isPresent()) {
            SuperAdmin superAdmin = optionalStore.get();

            // You can retrieve the hashed password, but you won't be able to retrieve the
            // plaintext password
            String hashedPassword = superAdmin.getPassword();

            // Retrieve other superAdmin information
            String username = superAdmin.getUsername();
            String email = superAdmin.getEmail();
            String contact = superAdmin.getContact();
            String country = superAdmin.getCountry();
            String saddress = superAdmin.getSaddress();
            String state = superAdmin.getState();

            // Add other fields you want to retrieve

            // Create a map to hold the information
            Map<String, String> storeInfo = new HashMap<>();
            // storeInfo.put("Storeid", String.valueOf(superAdmin.Storeid()));
            storeInfo.put("username", username);
            storeInfo.put("email", email);
            storeInfo.put("contact", contact);
            storeInfo.put("country", country);
            storeInfo.put("saddress", saddress);
            storeInfo.put("state", state);

            // Add other fields to the map

            return ResponseEntity.ok(storeInfo);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // THIS METHOD FOR FETCH ALL STORES
    @Override
    public ResponseEntity<List<SuperAdmin>> getAllSuper() {
        List<SuperAdmin> superAdmins = superAdminRepository.findAll();
        return ResponseEntity.ok(superAdmins);
    }

    @Override
    public ResponseEntity<String> updatestore(
            @PathVariable Long superid,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String saddress,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String contact,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String state,
            @RequestParam(required = false) Date date) throws IOException {

        Optional<SuperAdmin> optionalTech = superAdminRepository.findById(superid);
        if (!optionalTech.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        SuperAdmin superAdmin = optionalTech.get();
        // Update fields if provided in the request
        if (username != null) {
            superAdmin.setUsername(username);
        }
        if (saddress != null) {
            superAdmin.setSaddress(saddress);
        }
        if (email != null) {
            superAdmin.setEmail(email);
        }

        if (contact != null) {
            superAdmin.setContact(contact);
        }

        if (country != null) {
            superAdmin.setCountry(country);
        }
        if (state != null) {
            superAdmin.setState(state);
        }

        if (date != null) {
            superAdmin.setDate(date);
        }

        // Save the updated store to the database
        superAdminRepository.save(superAdmin);

        return ResponseEntity.ok("Super admin updated successfully!");
    }

    /*
     * {---------------------------------MADE CHANGES BY RUSHIKESH-----------------------------}
     * -----------------------FOR LOGOUT THE USER --- END THE SESSION -- FOR THIS CODE------------------------------- */

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