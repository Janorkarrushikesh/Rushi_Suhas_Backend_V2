package com.syntiaro_pos_system.controllerimpl.v2;

import com.syntiaro_pos_system.controller.v2.StoreController;
import com.syntiaro_pos_system.entity.v2.ApiResponse;
import com.syntiaro_pos_system.request.v1.StoreLoginRequest;
import com.syntiaro_pos_system.request.v1.StoreSignupRequest;
import com.syntiaro_pos_system.service.v2.StoreServiceV2;
import com.syntiaro_pos_system.serviceimpl.v2.StoreServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@RestController
public class StoreControllerImpl implements StoreController {

    @Autowired
    StoreServiceV2 storeServiceV2;

    public static final Logger logger = Logger.getLogger(StoreControllerImpl.class);


    @Override
    public ResponseEntity<ApiResponse> storeLogin(StoreLoginRequest storeLoginRequest) {
//        logger.info("Login By StoreAdmin = " + storeLoginRequest.getUsername() + ".  Login Status Succesfull");
//        logger.warn("Login By StoreAdmin = " + storeLoginRequest.getUsername() + ".  Login Status fail" );
        return storeServiceV2.storeLogin(storeLoginRequest);
    }

    @Override
    public ResponseEntity<ApiResponse> storeConfiguration(Long Storeid) {
        return storeServiceV2.storeConfig(Storeid);
    }

    @Override
    public ResponseEntity<ApiResponse> storeList(Integer size, Integer page) {
        return storeServiceV2.storeLsit(size, page);
    }

    @Override
    public ResponseEntity<ApiResponse> forgotPassword(StoreSignupRequest storeSignupRequest) {
        return storeServiceV2.forgotPassword(storeSignupRequest);
    }

    @Override
    public ResponseEntity<ApiResponse> resetPassword(Map<String, String> resetRequest) {
        return storeServiceV2.resetPassword(resetRequest);
    }

    @Override
    public ResponseEntity<ApiResponse> registerStore(StoreSignupRequest signUpRequestStore) {
        return storeServiceV2.registerStore(signUpRequestStore);
    }

    @Override
    public ResponseEntity<byte[]> getlogoByStoreId(Long storeId) {
        return storeServiceV2.getlogoByStoreId(storeId);
    }

    @Override
    public ResponseEntity<ApiResponse> updateStore(Long storeId, String username, String storeAddress, String email, String contact, String store_name, String gstno, String currency, String country, String state, Date date, String password, String Comfirmpassword, String pinCode, String address, String upi, MultipartFile logo) {
        return storeServiceV2.updateStore(storeId, username, storeAddress, email, contact, store_name, gstno, currency, country, state, date, password, Comfirmpassword, pinCode, address, upi, logo);
    }

    @Override
    public ResponseEntity<ApiResponse> renewSubscriptionByStoreId(String username, String email, Integer year) {
        return storeServiceV2.renewSubscriptionByStoreId(username, email, year);
    }

    @Override
    public ResponseEntity<ApiResponse> getFreeTrial(StoreSignupRequest storeSignupRequest) {
        return storeServiceV2.getFreeTrial(storeSignupRequest);
    }

    @Override
    public ResponseEntity<ApiResponse> logoutStore(String sessionToken) {
        return storeServiceV2.logoutStore(sessionToken);
    }


    @Override
    public ResponseEntity<ApiResponse> getStoreById(Long Storeid) {
        return storeServiceV2.getStoreById(Storeid);
    }


}