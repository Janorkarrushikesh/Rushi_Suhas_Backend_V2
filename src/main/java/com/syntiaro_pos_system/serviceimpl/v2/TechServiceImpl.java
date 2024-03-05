package com.syntiaro_pos_system.serviceimpl.v2;

import com.syntiaro_pos_system.entity.v1.ERole;
import com.syntiaro_pos_system.entity.v1.Tech;
import com.syntiaro_pos_system.entity.v1.TechRole;
import com.syntiaro_pos_system.entity.v2.ApiResponse;
import com.syntiaro_pos_system.repository.v2.*;
import com.syntiaro_pos_system.request.v1.TechLoginRequest;
import com.syntiaro_pos_system.request.v1.TechSignupRequest;
import com.syntiaro_pos_system.response.TechJwtResponse;
import com.syntiaro_pos_system.security.jwt.TechJwtUtils;
import com.syntiaro_pos_system.security.services.EmailSenderService;
import com.syntiaro_pos_system.security.services.TechDetailsImpl;
import com.syntiaro_pos_system.service.v2.TechService;
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
public class TechServiceImpl implements TechService {

    private static final int MAX_SESSIONS_PER_USER = 250;
    private final Map<String, Set<String>> userSessions = new ConcurrentHashMap<>();
    private final Map<String, String> emailToOtpMap = new HashMap<>();
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    TechJwtUtils techJwtUtils;
    @Autowired
    TechRepositoryV2 techRepository;
    @Autowired
    StoreRepositry storeRepositry;
    @Autowired
    UserRepositoryV2 userRepository;
    @Autowired
    SuperAdminRepositoryV2 superAdminRepository;
    @Autowired
    TechRoleRepositoryV2 techRoleRepository;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    EmailSenderService emailSenderService;
    @Autowired
    EmailUsernameValidation validation;
    @Autowired
    JavaMailSender javaMailSender;

    @Override
    public ResponseEntity<ApiResponse> authenticateTech(TechLoginRequest techLoginRequest) {
        try {
            String username = techLoginRequest.getUsername();
            if (hasExceededSessionLimit(username)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse(null, false, "Too many active sessions for this user.", 401));
            }
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(techLoginRequest.getUsername(),
                            techLoginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = techJwtUtils.generateJwtToken(authentication);
            addUserSession(username, jwt);
            TechDetailsImpl userDetails = (TechDetailsImpl) authentication.getPrincipal();
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(item -> item.getAuthority())
                    .collect(Collectors.toList());
            Optional<Tech> existingTech = techRepository.findByUsername(techLoginRequest.getUsername());
            if (existingTech.isPresent()) {
                Tech tech = existingTech.get();
            }

            return ResponseEntity.ok().body(new ApiResponse(new TechJwtResponse(jwt,
                    userDetails.getId(),
                    userDetails.getUsername(),
                    userDetails.getEmail(),
                    userDetails.getGstno(),
                    roles), true, 200));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(null, false, "...", 500));

        }
    }

    // Helper methods to manage Tech sessions
    private boolean hasExceededSessionLimit(String username) {
        return userSessions.getOrDefault(username, Collections.emptySet()).size() >= MAX_SESSIONS_PER_USER;
    }

    private void addUserSession(String username, String sessionToken) {
        userSessions.computeIfAbsent(username, k -> new HashSet<>()).add(sessionToken);
    }

//    ******************************Tech Sign Up ******************************************

    @Override
    public ResponseEntity<ApiResponse> registerTech(TechSignupRequest techsignupRequest) {
        try {
            if (validation.isDuplicateUsername(techsignupRequest.getUsername()) || validation.isDuplicateEmail(techsignupRequest.getEmail())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(null, false, "Error: Username or email is already taken!", 400));
            }
            String hashedPassword = encoder.encode(techsignupRequest.getPassword());
            if (techsignupRequest.getPassword().equals(techsignupRequest.getComfirmpassword())) {
                // Create new user's account
                Tech user = new Tech(techsignupRequest.getUsername(),
                        techsignupRequest.getSaddress(),
                        techsignupRequest.getEmail(),
                        techsignupRequest.getContact(),
                        techsignupRequest.getDate(),
                        techsignupRequest.getCountry(),
                        techsignupRequest.getState(),
                        techsignupRequest.getUpdatedby(),
                        techsignupRequest.getCreatedBy(),
                        techsignupRequest.getComfirmpassword(),
                        // techtechsignupRequest.getPassword(),
                        encoder.encode(techsignupRequest.getPassword()));
                Set<TechRole> techRoles = new HashSet<>();
                TechRole adminTechRole = techRoleRepository.findByName(ERole.ROLE_SUPPORT)
                        .orElseThrow(() -> new RuntimeException("Error: TechRole is not found."));
                techRoles.add(adminTechRole);
                user.setTechRoles(techRoles);
                user.setComfirmPassword(techsignupRequest.getPassword());
                Tech tech = techRepository.save(user);
                emailSenderService.sendRegistrationSuccessfulEmailtech(user.getEmail(), user.getUsername(),
                        techsignupRequest.getPassword());
                return ResponseEntity.ok()
                        .body(new ApiResponse(tech, true, "Tech registered successfully!", 200));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse(null, false, "PASSWORD DOES NOT MATCH!!!!!!", 400));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(null, false, "...", 500));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> forgotPassword(TechSignupRequest techSignupRequest) {
        try {
            String email = techSignupRequest.getEmail();
            Optional<Tech> existingTech = techRepository.findByEmail(email);
            if (existingTech.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse(null, false, "Given Email not found ", 400));
            }
            String otp = OTPUtil.generateOTP(6);
            //save otp to emailTootpMap to veridy later
            emailToOtpMap.put(email, otp);

            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("OTP Verification for Password Reset");
            message.setText("Your OTP for password reset is: " + otp + "(Valid for 5 Mins.)" + "\n" + "Your Reset Technician Password:-" + "https://prod.ubsbill.com/techresetpassword");
            javaMailSender.send(message);

            return ResponseEntity.ok().body(new ApiResponse(null, true, "OTP sent successfully to the provided email address", 200));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(null, false, "...", 500));
        }

    }

    @Override
    public ResponseEntity<ApiResponse> resetPasseord(Map<String, String> resetRequest) {
        try {
            String email = resetRequest.get("emailID");
            String otp = resetRequest.get("oneTimePassword");
            String password = resetRequest.get("password");
            String confirmPassword = resetRequest.get("confirmPassword");

            if (!emailToOtpMap.containsKey(email) || !emailToOtpMap.get(email).equals(otp)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse(null, false, "Invalid OTP or Email", 400));
            }
            //check passwords
            if (!password.equals(confirmPassword)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse(null, false, "New password and confirmation  not matched ", 400));
            }
            // check password is notnull and empty
            if (StringUtils.isEmpty(password)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse(null, false, "new Password can't be empty !", 400));
            }

            //update In Database
            Optional<Tech> existingTech = techRepository.findByEmail(email);
            if (existingTech.isPresent()) {
                Tech tech = existingTech.get();
                tech.setPassword(encoder.encode(password));
                tech.setComfirmPassword(confirmPassword);
                techRepository.save(tech);
                emailSenderService.sendPasswordChangedEmail(email, password);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(null, false, "Failed to update password", 500));
            }
            return ResponseEntity.ok().body(new ApiResponse(null, true, "Password Changed Successfully", 200));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(null, false, "...", 500));

        }
    }

    @Override
    public ResponseEntity<ApiResponse> updateTech(Long technicianId, Tech updateTech) {
        try {
            Tech tech = techRepository.findById(technicianId).orElseThrow(() -> new EntityNotFoundException("Technician not found"));

            if ((!tech.getUsername().equals(updateTech.getUsername()) && techRepository.existsByUsername(updateTech.getUsername()))) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(null, false, "Error : Username already taken", 400));
            }
            if (!tech.getEmail().equals(updateTech.getEmail()) && techRepository.existsByEmail(updateTech.getEmail())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(null, false, "Error : Email Id already taken ", 400));
            }

            tech.setUsername(updateTech.getUsername() != null ? updateTech.getUsername() : tech.getUsername());
            tech.setEmail(updateTech.getEmail() != null ? updateTech.getEmail() : tech.getEmail());
            tech.setAddress(updateTech.getAddress() != null ? updateTech.getAddress() : tech.getAddress());
            tech.setContact(updateTech.getContact() != null ? updateTech.getContact() : tech.getContact());
            tech.setCountry(updateTech.getCountry() != null ? updateTech.getCountry() : tech.getCountry());
            tech.setState(updateTech.getState() != null ? updateTech.getState() : tech.getState());
            tech.setUpdatedBy(updateTech.getUpdatedBy() != null ? updateTech.getUpdatedBy() : tech.getUpdatedBy());

            return ResponseEntity.ok().body(new ApiResponse(techRepository.save(tech), true, "Technician Updated Successfully", 200));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(null, false, "...", 500));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> logoutTech(String sessionToken) {
        try {
            String username = getUserNameFromSessionToken(sessionToken);
            if (username == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(null, false, "Invalid session token !", 401));
            }
            removeUserSession(username, sessionToken);
            return ResponseEntity.ok().body(new ApiResponse(null, false, "Logged out successfully !", 401));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(null, false, "...", 500));
        }
    }

    private String getUserNameFromSessionToken(String sessionToken) {
        for (Map.Entry<String, Set<String>> entry : userSessions.entrySet()) {
            if (entry.getValue().contains(sessionToken)) {
                return entry.getKey();
            }
        }
        return null;
    }

    private void removeUserSession(String username, String sessionToken) {
        userSessions.getOrDefault(username, Collections.emptySet()).remove(sessionToken);
    }

}
