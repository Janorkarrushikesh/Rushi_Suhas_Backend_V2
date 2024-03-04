package com.syntiaro_pos_system.service.v2;

import com.syntiaro_pos_system.entity.v2.ApiResponse;
import com.syntiaro_pos_system.request.v1.StoreLoginRequest;
import com.syntiaro_pos_system.request.v1.StoreSignupRequest;
import com.syntiaro_pos_system.response.v2.StoreJwtResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.Map;


public interface StoreServiceV2 {
    ResponseEntity<ApiResponse> storeLogin(StoreLoginRequest storeLoginRequest);
    ResponseEntity<ApiResponse> storeConfig(Long storeid);

    ResponseEntity<ApiResponse> storeLsit( Integer page , Integer size);

    ResponseEntity<ApiResponse> forgotPassword(StoreSignupRequest storeSignupRequest);

    ResponseEntity<ApiResponse> resetPassword(Map<String, String> resetRequest);
    ResponseEntity<byte[]> getlogoByStoreId(Long storeId);

    ResponseEntity<ApiResponse> registerStore(StoreSignupRequest signUpRequestStore);


    ResponseEntity<ApiResponse> renewSubscriptionByStoreId(String username, String email, Integer year);

    ResponseEntity<ApiResponse> getFreeTrial(StoreSignupRequest storeSignupRequest);

    ResponseEntity<ApiResponse> updateStore(Long storeId, String username, String storeAddress, String email, String contact, String storeName, String gstno, String currency, String country, String state, Date date, String password, String comfirmpassword, String pinCode, String address, String upi, MultipartFile logo);

    ResponseEntity<ApiResponse> logoutStore(String sessionToken);

    ResponseEntity<ApiResponse> getStoreById(Long storeid);
}
