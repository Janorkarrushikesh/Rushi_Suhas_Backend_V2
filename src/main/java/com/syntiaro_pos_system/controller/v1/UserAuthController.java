package com.syntiaro_pos_system.controller.v1;

import com.syntiaro_pos_system.entity.v1.User;
import com.syntiaro_pos_system.request.v1.LoginRequest;
import com.syntiaro_pos_system.request.v1.SignupRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.*;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/v1/api/auth/user")
public interface UserAuthController {

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest);

    @PostMapping("/signup")
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) ;

    // THIS METHOD IS USE FOR UPDATE USER
    @PatchMapping("/users/{id}")
    public ResponseEntity<?> updateUser(@PathVariable("id") Long userId, @RequestBody SignupRequest signUpRequest) ;


    // THIS METHOD IS USE FOR FETCH USER BY ID
    @GetMapping("/getusersByID/{id}")
    public ResponseEntity<?> fetchUserByID(@PathVariable("id") Long id) ;

    // THIS METHOD IS USE FOR CHANGE PASSWORD OF USER
    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody SignupRequest signupRequest) ;

    // THIS METHOD IS USE FOR FORGET PASSWORD
    @PostMapping("/user-forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody SignupRequest signupRequest) ;
    // THIS METHOD IS USED FOR VERIFYING THE OTP AND UPDATING THE PASSWORD
    @PostMapping("/user-reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody Map<String, String> resetRequest) ;

    // THIS METHOD IS USE FOR DELETE USER BY ID
    @DeleteMapping("/users/{Serial_no}")
    public ResponseEntity<?> deleteUser(@PathVariable("Serial_no") Integer Serial_no) ;

    @GetMapping("/byStoreId/{storeid}")
    public ResponseEntity<List<User>> getUsersByStoreId(@PathVariable Integer storeid) ;

    @PostMapping("/Logout")
    public ResponseEntity<?> logoutUser(@RequestParam String sessionToken) ;




}
