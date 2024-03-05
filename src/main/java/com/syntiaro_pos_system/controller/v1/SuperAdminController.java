package com.syntiaro_pos_system.controller.v1;


import com.syntiaro_pos_system.entity.v1.SuperAdmin;
import com.syntiaro_pos_system.request.v1.SuperAdminLoginRequest;
import com.syntiaro_pos_system.request.v1.SuperAdminSignupRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/api/auth/superadmin")
@CrossOrigin(origins = "*", maxAge = 3600)
public interface SuperAdminController {

    @PostMapping("/superSignin")
    ResponseEntity<?> authenticateUser(@Valid @RequestBody SuperAdminLoginRequest superAdminLoginRequest);

    @PostMapping("/superSignup")
    ResponseEntity<?> registerUser(@Valid @RequestBody SuperAdminSignupRequest superAdminSignupRequest);


    @DeleteMapping("/{superid}")
    ResponseEntity<?> deleteStore(@PathVariable("superid") Long techid);

    @GetMapping("/stores/{superid}")
    ResponseEntity<SuperAdmin> getStoreById(@PathVariable Long superid);

    @PostMapping("/reset-password")
    ResponseEntity<String> resetPassword(@RequestBody Map<String, String> resetRequest);

    @PostMapping("/forgot-password")
    ResponseEntity<?> forgotPassword(@RequestBody SuperAdminSignupRequest superAdminSignupRequest);


    @PostMapping("/super_change-password")
    ResponseEntity<String> changePassword(@RequestBody SuperAdminSignupRequest superAdminSignupRequest);

    @GetMapping("/getsuper")
    // @PreAuthorize("hasRole('ADMIN')")
    List<SuperAdmin> getStore();

    @GetMapping("/updatesuperadmin/{superid}")
    ResponseEntity<?> getUpdatedStoreInfo(@PathVariable Long superid);

    // THIS METHOD FOR FETCH ALL STORES
    @GetMapping("/allsuper")
    ResponseEntity<List<SuperAdmin>> getAllSuper();

    @PatchMapping("/updatetsuper/{superid}")
    ResponseEntity<String> updatestore(
            @PathVariable Long superid,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String saddress,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String contact,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String state,
            @RequestParam(required = false) Date date) throws IOException;


    @PostMapping("/Logout")
    ResponseEntity<?> logoutUser(@RequestParam String sessionToken);
}
