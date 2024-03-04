package com.syntiaro_pos_system.controller.v2;

import com.syntiaro_pos_system.entity.v2.ApiResponse;
import com.syntiaro_pos_system.request.v1.StoreLoginRequest;
import com.syntiaro_pos_system.request.v1.StoreSignupRequest;
import com.syntiaro_pos_system.serviceimpl.v2.StoreServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.Date;
import java.util.Map;


@RequestMapping("/v2/api/auth/store")
@CrossOrigin(origins = "*", maxAge = 3600)
public interface StoreController {

    @PostMapping("/signin")
    public ResponseEntity<?> storeLogin(@Valid @RequestBody StoreLoginRequest storeLoginRequest);

    @GetMapping("/configuration/{Storeid}")
    // @Cacheable(value = "storeid")
    public ResponseEntity<?> storeConfiguration(@PathVariable Long Storeid);

    @GetMapping("/")
    public ResponseEntity<ApiResponse> storeList(@RequestParam Integer size , @RequestParam Integer page);

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse> forgotPassword(@RequestBody StoreSignupRequest storeSignupRequest);

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse> resetPassword(@RequestBody Map<String, String> resetRequest) ;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse> registerStore(@Valid @RequestBody StoreSignupRequest signUpRequestStore) ;

    @GetMapping("/logo/{storeId}")
    public ResponseEntity<byte[]> getlogoByStoreId(@PathVariable Long storeId);


    @PatchMapping("/{storeId}")
    public ResponseEntity<ApiResponse> updateStore(@PathVariable Long storeId,
                                                   @RequestParam(required = false) String username,
                                                   @RequestParam(required = false) String storeAddress,
                                                   @RequestParam(required = false) String email,
                                                   @RequestParam(required = false) String contact,
                                                   @RequestParam(required = false) String store_name,
                                                   @RequestParam(required = false) String gstno,
                                                   @RequestParam(required = false) String currency,
                                                   @RequestParam(required = false) String country,
                                                   @RequestParam(required = false) String state,
                                                   @RequestParam(required = false) Date date,
                                                   @RequestParam(required = false) String password,
                                                   @RequestParam(required = false) String Comfirmpassword,
                                                   @RequestParam(required = false) String pinCode,
                                                   @RequestParam(required = false) String address,
                                                   @RequestParam(required = false) String upi,
                                                   @RequestParam(required = false) MultipartFile logo);
    @PatchMapping("/renewSubscription")
    public ResponseEntity<ApiResponse> renewSubscriptionByStoreId(@RequestParam(required = false,name = "UserName") String username,
                                                                  @RequestParam(required = false,name ="emailID") String email,
                                                                  @RequestParam Integer year);

    @PostMapping("/freeTrialRegistration")
    public ResponseEntity<ApiResponse> getFreeTrial(@RequestBody StoreSignupRequest storeSignupRequest);

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse> logoutStore(@RequestParam String sessionToken);

    @GetMapping("/{Storeid}")
    public ResponseEntity<ApiResponse> getStoreById(@PathVariable Long Storeid);

}
