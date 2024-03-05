package com.syntiaro_pos_system.controller.v1;


import com.syntiaro_pos_system.entity.v1.Tech;
import com.syntiaro_pos_system.request.v1.TechLoginRequest;
import com.syntiaro_pos_system.request.v1.TechSignupRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/v1/api/auth/tech")
@CrossOrigin(origins = "*", maxAge = 3600)
public interface TechAuthController {

    @PostMapping("/TechSignin")
    ResponseEntity<?> authenticateUser(@Valid @RequestBody TechLoginRequest techLoginRequest);

    @PostMapping("/techSignup")
    ResponseEntity<?> registerUser(@Valid @RequestBody TechSignupRequest techtechsignupRequest);

    // THIS METHOD IS USE FOR DELETE STORE
    @DeleteMapping("/{Techid}")
    ResponseEntity<?> deleteStore(@PathVariable("Techid") Long techid);

    // THIS METHOD IS USE FOR FETCH STORE BY ID
    @GetMapping("/stores/{techid}")
    ResponseEntity<Tech> getStoreById(@PathVariable Long techid);

    @PostMapping("/reset-password")
    ResponseEntity<String> resetPassword(@RequestBody Map<String, String> resetRequest);

    @PostMapping("/forgot-password")
    ResponseEntity<?> forgotPassword(@RequestBody TechSignupRequest techSignupRequest);

    // THIS METHOD IS USE FOR CHANGE PASSWORD OF STORE
    @PostMapping("/tech_change-password")
    ResponseEntity<String> changePassword(@RequestBody TechSignupRequest techSignupRequest);

    // THIS METHOD IS USE FOR GET ALL STORE DETAIL
    @GetMapping("/gettech")
    // @PreAuthorize("hasRole('ADMIN')")
    List<Tech> getStore();

    @GetMapping("/updated-tech/{techId}")
    ResponseEntity<?> getUpdatedStoreInfo(@PathVariable Long techid);

    // THIS METHOD FOR FETCH ALL STORES
    @GetMapping("/alltech")
    ResponseEntity<List<Tech>> getAllStores();

    @PatchMapping("/UpdateStoreInfo/{storeId}")
    ResponseEntity<?> updateStoreInfo(@PathVariable Long storeId, @Valid @RequestBody TechSignupRequest updateRequest);

    @PatchMapping("/updatetech/{techid}")
    ResponseEntity<String> updatestore(
            @PathVariable Long techid,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String contact,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String state,
            @RequestParam(required = false) Date date,
            @RequestParam(required = false) String updatedby,
            @RequestParam(required = false) String createdBy) throws IOException;

    @PostMapping("/Logout")
    ResponseEntity<?> logoutUser(@RequestParam String sessionToken);
}
