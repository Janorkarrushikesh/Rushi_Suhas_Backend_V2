package com.syntiaro_pos_system.controller.v1;

import com.syntiaro_pos_system.entity.v1.Store;
import com.syntiaro_pos_system.request.v1.StoreLoginRequest;
import com.syntiaro_pos_system.request.v1.StoreSignupRequest;
import com.syntiaro_pos_system.response.MessageResponse;
import com.itextpdf.text.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.validation.Valid;
import java.io.IOException;
import java.util.*;
import java.util.List;

@RestController
@RequestMapping("/v1/api/auth/store")
@CrossOrigin(origins = "*", maxAge = 3600)
public interface StoreAuthController {


    @PostMapping("/storeSignin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody StoreLoginRequest storeLoginRequest) ;

    @PostMapping("/storeSignup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody StoreSignupRequest signUpRequestStore) ;

    @PatchMapping("/storeSubscribePaid")
    public ResponseEntity<?> subscribeToPaidSubscription(@RequestBody StoreSignupRequest storeSignupRequest) ;

    // THIS METHOD IS USE FOR RENEWSUBSCRIPTION USING REGISTRATION NUMBER
    @PatchMapping("/storeRenewSubscription")
    public ResponseEntity<?> renewSubscriptionByRegiNum(@RequestBody StoreSignupRequest request) ;

    // THIS METHOD IS USE FOR DELETE STORE
    @DeleteMapping("/{storeId}")
    public ResponseEntity<?> deleteStore(@PathVariable("storeId") Long storeId) ;
    @GetMapping("/getstore/{storeId}")
    public ResponseEntity<Store> getStoreById(@PathVariable Long storeId);



    @GetMapping("/{storeId}/logo") // added by Rushi
    public ResponseEntity<byte[]> getStoreLogo(@PathVariable Long storeId) ;

    // THIS METHOD IS USE FOR UPDATE STORE
    @PatchMapping("/updatestore/{storeId}")
    public ResponseEntity<MessageResponse> updatestore(
            @PathVariable Long storeId,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String saddress,
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
            @RequestParam(required = false) MultipartFile logo) throws IOException ;


    // THIS METHOD IS USED FOR VERIFYING THE OTP AND UPDATING THE PASSWORD
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody Map<String, String> resetRequest) ;
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody StoreSignupRequest storeSignupRequest) ;
    @PostMapping("/store_change-password")
    public ResponseEntity<String> changePassword(@RequestBody StoreSignupRequest storeSignupRequest) ;

    // THIS METHOD IS USE FOR GET ALL STORE DETAIL
    @GetMapping("/getstore")
    // @PreAuthorize("hasRole('ADMIN')")
    public List<Store> getStore() ;
    @GetMapping("/updated-store/{storeId}")
    public ResponseEntity<?> getUpdatedStoreInfo(@PathVariable Long storeId) ;

    @GetMapping("/allstore")
    public ResponseEntity<List<Store>> getAllStores() ;

    // THIS METHOD FOR FETCH ALL ROLE_SUPPORT
    @GetMapping("/support/allstores")
    public ResponseEntity<List<Store>> getAllSupportStores() ;

    // THIS METHOD FOR FETCH ALL ROLE_ADMIN
    @GetMapping("/admin/allstores")
    public ResponseEntity<List<Store>> getAllStoresdetail() ;

    // THIS METHOD IS USE FOR UPDATE SUPER ADMIN STORE
    @PatchMapping("/UpdateStoreInfo/{storeId}")
    public ResponseEntity<?> updateStoreInfo(@PathVariable Long storeId,
                                             @Valid @RequestBody StoreSignupRequest updateRequest) ;

    @PostMapping("/storeFreeTrial")
    public ResponseEntity<?> registerStore(@Valid @RequestBody StoreSignupRequest signUpRequestStore) ;

    // AFTER PAid Renew from old date SUBSCRIPTION by username
    @PatchMapping("/storeRenewByUsername")
    public ResponseEntity<?> renewSubscriptionByUsername(@RequestBody Map<String, String> requestBody) ;
    // AFTER PAid Renew from old date SUBSCRIPTION by regi number
    @PatchMapping("/storeRenewByregiNum")
    public ResponseEntity<?> renewSubscriptionByregiNum(@RequestBody Map<String, String> requestBody);

    @PatchMapping("/storeRenewSubscriptionstoreId")
    public ResponseEntity<?> renewSubscriptionByStoreIdS(@RequestBody StoreSignupRequest request) ;
    @PatchMapping("/storeRenewSubscriptionregiNum")
    public ResponseEntity<?> renewSubscriptionByRegiNumS(@RequestBody StoreSignupRequest request);

    @GetMapping("/generate-pdf")
    public ResponseEntity<?> generatePDF(
            @RequestParam(name = "startDate", required = false) Date startDateStr,
            @RequestParam(name = "endDate", required = false) Date endDateStr) throws IOException, DocumentException ;

    @PostMapping("/Logout")
    public ResponseEntity<?> logoutUser(@RequestParam String sessionToken) ;

}
