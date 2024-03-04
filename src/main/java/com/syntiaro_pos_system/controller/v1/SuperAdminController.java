package com.syntiaro_pos_system.controller.v1;


import com.syntiaro_pos_system.entity.v1.SuperAdmin;
import com.syntiaro_pos_system.request.v1.SuperAdminLoginRequest;
import com.syntiaro_pos_system.request.v1.SuperAdminSignupRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/v1/api/auth/superadmin")
@CrossOrigin(origins = "*", maxAge = 3600)
public interface SuperAdminController {

    @PostMapping("/superSignin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody SuperAdminLoginRequest superAdminLoginRequest) ;

    @PostMapping("/superSignup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SuperAdminSignupRequest superAdminSignupRequest) ;


    @DeleteMapping("/{superid}")
    public ResponseEntity<?> deleteStore(@PathVariable("superid") Long techid) ;

    @GetMapping("/stores/{superid}")
    public ResponseEntity<SuperAdmin> getStoreById(@PathVariable Long superid) ;

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody Map<String, String> resetRequest) ;

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody SuperAdminSignupRequest superAdminSignupRequest) ;


    @PostMapping("/super_change-password")
    public ResponseEntity<String> changePassword(@RequestBody SuperAdminSignupRequest superAdminSignupRequest) ;

    @GetMapping("/getsuper")
    // @PreAuthorize("hasRole('ADMIN')")
    public List<SuperAdmin> getStore() ;

    @GetMapping("/updatesuperadmin/{superid}")
    public ResponseEntity<?> getUpdatedStoreInfo(@PathVariable Long superid) ;

    // THIS METHOD FOR FETCH ALL STORES
    @GetMapping("/allsuper")
    public ResponseEntity<List<SuperAdmin>> getAllSuper() ;

    @PatchMapping("/updatetsuper/{superid}")
    public ResponseEntity<String> updatestore(
            @PathVariable Long superid,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String saddress,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String contact,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String state,
            @RequestParam(required = false) Date date) throws IOException ;



    @PostMapping("/Logout")
    public ResponseEntity<?> logoutUser(@RequestParam String sessionToken);
}
