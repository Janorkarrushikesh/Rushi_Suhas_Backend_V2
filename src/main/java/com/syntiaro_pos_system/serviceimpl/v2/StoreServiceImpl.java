package com.syntiaro_pos_system.serviceimpl.v2;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.syntiaro_pos_system.entity.v1.ERole;
import com.syntiaro_pos_system.entity.v1.Store;
import com.syntiaro_pos_system.entity.v1.StoreRole;
import com.syntiaro_pos_system.entity.v2.AdminMenu;
import com.syntiaro_pos_system.entity.v2.ApiResponse;
import com.syntiaro_pos_system.repository.v2.*;
import com.syntiaro_pos_system.request.v1.StoreLoginRequest;
import com.syntiaro_pos_system.request.v1.StoreSignupRequest;
import com.syntiaro_pos_system.response.v2.StoreJwtResponse;
import com.syntiaro_pos_system.security.jwt.StoreJwtUtils;
import com.syntiaro_pos_system.security.services.EmailSenderService;
import com.syntiaro_pos_system.security.services.StoreDetailsImpl;
import com.syntiaro_pos_system.service.v2.AdminMenuService;
import com.syntiaro_pos_system.service.v2.StoreServiceV2;
import com.syntiaro_pos_system.utils.OTPUtil;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;


@Service
public class StoreServiceImpl implements StoreServiceV2 {


    @Autowired
    StoreRepositry storeRepository;
    @Autowired
    UserRepositoryV2 userRepository;

    @Autowired
    TechRepositoryV2 techRepository;

    @Autowired
    SuperAdminRepositoryV2 superAdminRepository;
    @Autowired
    StoreRoleRepositoryV2 storeRoleRepository;
    @Autowired
    AdminMenuRepository adminMenuRepository;
    @Autowired
    BalanceRepositry balanceRepository;


    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    StoreJwtUtils storeJwtUtils;

    @Autowired
    AdminMenuService adminMenuService;

    @Autowired
    BalanceServiceImpl balanceService;

    @Autowired
    FoodServiceImpl foodserviceimpl;

    @Autowired
    DashBoardServiceImpl dashBoardServiceimpl;

    @Autowired
    JavaMailSender javaMailSender;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    EmailSenderService emailSenderService;


    private final Map<String, String> emailToOtpMap = new HashMap<>();

    private static final int MAX_SESSIONS_PER_USER = 2;
    private final Map<String, Set<String>> storeSessions = new ConcurrentHashMap<>();

    private static final Logger loggers = LogManager.getLogger(StoreServiceImpl.class);

    @Override
    public ResponseEntity<?> storeLogin(StoreLoginRequest storeLoginRequest) {
        try {
            Store store = storeRepository.findByUsername(storeLoginRequest.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));


            String username = storeLoginRequest.getUsername();
            if (hasExceededSessionLimit(username)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Too many active sessions for this user.");
            }


            LocalDateTime expireDate = store.getSubscriptionExpiration(); // Fetch the user's expiration date
            LocalDateTime currentDate = LocalDateTime.now();
            if (currentDate.isBefore(expireDate)) {
                Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(storeLoginRequest.getUsername(), storeLoginRequest.getPassword()));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                String jwt = storeJwtUtils.generateJwtToken(authentication);
                addUserSession(username, jwt);
                StoreDetailsImpl storeDetails = (StoreDetailsImpl) authentication.getPrincipal();
                List<String> roles = storeDetails.getAuthorities().stream()
                        .map(item -> item.getAuthority())
                        .collect(Collectors.toList());

                // Assuming you have a logo URL field in the userDetails
                Optional<Store> storeOptional = storeRepository.findByUsername(storeLoginRequest.getUsername());


                String storeName = null;

                if (storeOptional.isPresent()) {
                    Store stores = storeOptional.get();
                    storeName = stores.getStoreName();
                }
                loggers.info("Login Succesfull username =" + storeLoginRequest.getUsername());
                return ResponseEntity.ok(new StoreJwtResponse(jwt,
                        storeDetails.getId(),
                        storeDetails.getUsername(),
                        storeName,
                        roles
                ));

            } else {
                String errorMessage = " Login failed: The login period has expired.";
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("error", errorMessage);
                loggers.warn("Login By StoreAdmin =" + storeLoginRequest.getUsername() + errorMessage + " Url is = /v2/api/auth/store/signin");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body((StoreJwtResponse) errorResponse);
            }
        } catch (UsernameNotFoundException e) {
            String errorMessage = "User not found";
            loggers.error("Login failed: " + errorMessage + " Url is = /v2/api/auth/store/signin");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
        } catch (AuthenticationException e) {
            String errorMessage = "Authentication failed: Invalid username or password";
            loggers.error("Login failed: " + errorMessage + " Url is = /v2/api/auth/store/signin");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorMessage);
        }
    }


    private boolean hasExceededSessionLimit(String username) {
        return storeSessions.getOrDefault(username, Collections.emptySet()).size() >= MAX_SESSIONS_PER_USER;
    }

    private void addUserSession(String username, String sessionToken) {
        storeSessions.computeIfAbsent(username, k -> new HashSet<>()).add(sessionToken);
    }

    public List<Map<String, Object>> getStoreDetails(Long storeid) {
        Optional<Store> storeData = storeRepository.findById(storeid);
        String[] paymentFeatures = {"Cash", "Card Payment", "UPI"};
        String[] storeFeatures = {"dine_in", "take_away", "delivery"};
        String[] discount = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
        List<Map<String, Object>> storeDetailsList = new ArrayList<>();
        storeData.ifPresent(store -> {
            Map<String, Object> storeMap = new LinkedHashMap<>();
            storeMap.put("registrionNumber", store.getRegistrationNo());
            storeMap.put("id", store.getStoreid());
            storeMap.put("storeName", store.getStoreName());
            storeMap.put("email", store.getEmail());
            storeMap.put("contact", store.getContact());
            storeMap.put("country", store.getCountry());
            storeMap.put("countryCode", store.getCountryCode());
            storeMap.put("state", store.getState());
            storeMap.put("address", store.getAddress());
            storeMap.put("pinCode", store.getPinCode());
            storeMap.put("currency", store.getCurrency());
            storeMap.put("logo", store.getLogo());
            storeMap.put("upi", store.getUpi());
            storeMap.put("storeFeatures", storeFeatures);
            storeMap.put("paymentFeatures", paymentFeatures);
            storeMap.put("discount", discount);
            storeDetailsList.add(storeMap);
        });
        return storeDetailsList;
    }


    @Override
    public ApiResponse storeConfig(Long storeid) {
        String storeId = String.valueOf(storeid);
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("storeData", getStoreDetails(storeid));
        data.put("menu", adminMenuService.getStoreByStoreIdAndStatus(storeid));
        data.put("product", foodserviceimpl.FoodsByStoreId(storeId));
        data.put("dashboard", dashBoardServiceimpl.getStoreByStoreIdAndStatus(storeid));
        return new ApiResponse(data, true, HttpStatus.OK.value());
    }

    @Override
    public ResponseEntity<ApiResponse> storeLsit(Integer page, Integer size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Store> storeList = storeRepository.findAll(pageable);
            List<Map<String, Object>> storeData = new ArrayList<>();
            for (Store store : storeList) {
                Map<String, Object> storeMap = new LinkedHashMap<>();
                storeMap.put("id", store.getStoreid());
                storeMap.put("storeName", store.getStoreName());
                storeMap.put("username", store.getUsername());
                storeMap.put("address", store.getAddress());
                storeMap.put("sadd", store.getStoreAddress());
                storeMap.put("upi", store.getUpi());
                storeMap.put("pinCode", store.getPinCode());
                storeMap.put("contact", store.getContact());
                storeMap.put("state", store.getState());
                storeMap.put("country", store.getCountry());
                storeMap.put("email", store.getEmail());
                storeMap.put("countryCode", store.getCountryCode());
                storeMap.put("currency", store.getCurrency());
                storeMap.put("", store.getGstNo());
                storeMap.put("registrationNumber", store.getRegistrationNo());
                storeMap.put("subscriptionType", store.getSubscriptionType());
                storeData.add(storeMap);
            }
            return ResponseEntity.ok().body(new ApiResponse(storeData, true, 200));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(null, false, "...", 500));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> forgotPassword(StoreSignupRequest storeSignupRequest) {
        try {
            String email = storeSignupRequest.getEmail();
            Optional<Store> optionalStore = storeRepository.findByEmail(email);
            if (optionalStore.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse(null, false, "Store account not found for the given email", 404));
            }
            String otp = OTPUtil.generateOTP(6);
            emailToOtpMap.put(email, otp);
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("OTP Verification for Password Reset");
            message.setText("Your OTP for password reset is: " + otp + "(Valid for 5 Mins.)" + "\n" + "Your Reset Store Password:-" + "https://prod.ubsbill.com/resetpassword");
            javaMailSender.send(message);
            return ResponseEntity.ok()
                    .body(new ApiResponse(null, true, "OTP sent successfully to the provided email address", 200));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(null, false, "...", 500));
        }


    }

    @Override
    public ResponseEntity<ApiResponse> resetPassword(Map<String, String> resetRequest) {

        try {
            String email = resetRequest.get("emailID");
            String otp = resetRequest.get("oneTimePassword");
            String newPassword = resetRequest.get("password: ");
            String confirmPassword = resetRequest.get("confirmPassword: ");
            if (!emailToOtpMap.containsKey(email) || !emailToOtpMap.get(email).equals(otp)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(null, false, "Invalid OTP or Email", 400));
            }
            if (!Objects.equals(newPassword, confirmPassword)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(null, false, "New password and confirmation do not match", 400));
            }
            if (StringUtils.isEmpty(newPassword)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(null, false, "New password cannot be empty", 400));
            }
            Optional<Store> optionalStore = storeRepository.findByEmail(email);
            if (optionalStore.isPresent()) {
                Store store = optionalStore.get();
                store.setPassword(encoder.encode(newPassword));
                store.setComfirmPassword(newPassword);
                storeRepository.save(store);
                emailSenderService.sendPasswordChangedEmail(email, newPassword);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(null, false, "Failed to update password", 500));
            }
            emailToOtpMap.remove(email);
            return ResponseEntity.ok().body(new ApiResponse(null, true, "Password updated successfully", 200));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(null, false, "...", 500));
        }

    }

    @Override
    public ResponseEntity<byte[]> getlogoByStoreId(Long storeId) {
        try {
            // balanceService.updateRemainingBalancesForAllStores();
            Optional<Store> store = storeRepository.findById(storeId);
            if (store.isPresent() && store.get().getLogo() != null) {
                byte[] logo = store.get().getLogo();

                return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(logo);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

        }
    }

    @Override
    public ResponseEntity<ApiResponse> registerStore(StoreSignupRequest signUpRequestStore) {
        try {
            if (storeRepository.existsByUsername(signUpRequestStore.getUsername())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse(null, false, "Error: Username is already taken!", 400));
            }

            if (userRepository.existsByUsername(signUpRequestStore.getUsername())) {


                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse(null, false, "Error: Username is already taken!", 400));
            }

            if (techRepository.existsByUsername(signUpRequestStore.getUsername())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse(null, false, "Error: Username is already taken!", 400));

            }

            if (superAdminRepository.existsByUsername(signUpRequestStore.getUsername())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse(null, false, "Error: Username is already taken!", 400));

            }

            if (storeRepository.existsByEmail(signUpRequestStore.getEmail())) {

                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse(null, false, "Error: Email is already in use!", 400));

            }

            if (techRepository.existsByEmail(signUpRequestStore.getEmail())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse(null, false, "Error: Email is already in use!", 400));


            }

            if (userRepository.existsByEmail(signUpRequestStore.getEmail())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse(null, false, "Error: Email is already in use!", 400));


            }

            if (superAdminRepository.existsByEmail(signUpRequestStore.getEmail())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse(null, false, "Error: Email is already in use!", 400));


            }
            if (storeRepository.existsByContact(signUpRequestStore.getContact())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse(null, false, "Error: Contact is already in use!", 400));


            }
            String hashedPassword = encoder.encode(signUpRequestStore.getPassword());
            if (signUpRequestStore.getPassword().equals(signUpRequestStore.getComfirmpassword())) {
                // Create new user's account
                Store user = new Store(signUpRequestStore.getUsername(),
                        signUpRequestStore.getStoreAddress(),
                        signUpRequestStore.getEmail(),
                        signUpRequestStore.getStore_name(),
                        signUpRequestStore.getContact(),
                        signUpRequestStore.getGstno(),
                        signUpRequestStore.getDate(),
                        signUpRequestStore.getCurrency(),
                        signUpRequestStore.getCountry(),
                        signUpRequestStore.getCountry_code(),
                        signUpRequestStore.getState(),
                        signUpRequestStore.getCreatedby(),
                        signUpRequestStore.getUpdatedby(),
                        signUpRequestStore.getComfirmpassword(),
                        signUpRequestStore.getSubscriptionType(),
                        signUpRequestStore.getUpi(),
                        signUpRequestStore.getAddress(),
                        signUpRequestStore.getPinCode(),
                        // signUpRequestStore.getPassword(),
                        encoder.encode(signUpRequestStore.getPassword()));


                // Set subscription expiration time (8544 hours from now)
                LocalDateTime subscriptionExpiration = LocalDateTime.now().plusHours(8544);
                user.setSubscriptionExpiration(subscriptionExpiration);
                Set<String> strRoles = signUpRequestStore.getRole();
                Set<StoreRole> storeRoles = new HashSet<>();
                if (strRoles == null) {
                    StoreRole adminStoreRole = storeRoleRepository.findByName(ERole.ROLE_ADMIN)
                            .orElseThrow(() -> new RuntimeException("Error: StoreRole is not found."));
                    storeRoles.add(adminStoreRole);
                } else {
                    strRoles.forEach(role -> {
                        switch (role) {
                            case "user":
                                StoreRole userStoreRole = storeRoleRepository.findByName(ERole.ROLE_USER)
                                        .orElseThrow(() -> new RuntimeException("Error: StoreRole is not found."));
                                storeRoles.add(userStoreRole);
                                break;
                            case "mod":
                                StoreRole modStoreRole = storeRoleRepository.findByName(ERole.ROLE_MODERATOR)
                                        .orElseThrow(() -> new RuntimeException("Error: StoreRole is not found."));
                                storeRoles.add(modStoreRole);
                                break;
                            case "superAD":
                                StoreRole superADStoreRole = storeRoleRepository.findByName(ERole.ROLE_SUPER_ADMIN)
                                        .orElseThrow(() -> new RuntimeException("Error: StoreRole is not found."));
                                storeRoles.add(superADStoreRole);
                                break;
                            case "support":
                                StoreRole supportStoreRole = storeRoleRepository.findByName(ERole.ROLE_SUPPORT)
                                        .orElseThrow(() -> new RuntimeException("Error: StoreRole is not found."));
                                storeRoles.add(supportStoreRole);
                                break;
                            default:
                                StoreRole adminStoreRole = storeRoleRepository.findByName(ERole.ROLE_ADMIN)
                                        .orElseThrow(() -> new RuntimeException("Error: StoreRole is not found."));
                                storeRoles.add(adminStoreRole);
                        }
                    });
                }

                List<Store> storeList = storeRepository.findAllByDesc();

                // This Method Use For set Default dashBoard to that Store id.
                ObjectMapper objectMapper = new ObjectMapper();
                File adminmenuFile = new File("src/main/resources/AdminMenu.Json");
                try {
                    if (storeList.isEmpty()) {
                        List<AdminMenu> adminMenu = objectMapper.readValue(adminmenuFile, new TypeReference<List<AdminMenu>>() {
                        });

                        for (AdminMenu menu : adminMenu) {
                            menu.setStoreId(1L);
                        }
                        adminMenuRepository.saveAll(adminMenu);

                    } else {
                        List<AdminMenu> adminMenu = objectMapper.readValue(adminmenuFile, new TypeReference<List<AdminMenu>>() {
                        });
                        Long storeId = storeList.get(0).Storeid() + 1;
                        for (AdminMenu menu : adminMenu) {
                            menu.setStoreId(storeId);
                        }
                        adminMenuRepository.saveAll(adminMenu);

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

                user.setStoreRoles(storeRoles);
                user.setComfirmPassword(user.getComfirmPassword());
                Store store = storeRepository.save(user);
                emailSenderService.sendRegistrationSuccessfulEmailstore(user.getEmail(), user.getUsername(),
                        signUpRequestStore.getPassword());

                return ResponseEntity.ok().body(new ApiResponse(store, true, "Store registered successfully!", 200));
            } else {

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(null, false, "Password Does Not Match !!!!!!", 400));

            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(null, false, "...", 500));
        }
    }


    @Override
    public ResponseEntity<ApiResponse> updateStore(Long storeId, String username, String storeAddress, String email, String contact, String storeName, String gstNo, String currency, String country, String state,  String pinCode, String address, String upi, MultipartFile logo) {
        try {
            Optional<Store> optionalStore = storeRepository.findById(Long.valueOf(storeId));
            Store store = optionalStore.get();
            if (!optionalStore.isPresent()) {
                return ResponseEntity.notFound().build();
            }
            if (storeRepository.existsByUsername(username) && !store.getUsername().equals(username) || userRepository.existsByUsername(username) ||
                    techRepository.existsByUsername(username) || superAdminRepository.existsByUsername(username)) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse(null, false, "Error: Username is already taken!", 400));
            }

            if (storeRepository.existsByEmail(email) && !store.getEmail().equals(email) || userRepository.existsByEmail(email)
                    || superAdminRepository.existsByEmail(email) || techRepository.existsByEmail(email)) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse(null, false, "Error: Email is already in use!", 400));
            }
            if (!optionalStore.isPresent()) {
                return ResponseEntity.notFound().build();
            }
            store = optionalStore.get();
            store.setUsername(username != null ? username : store.getUsername());
            store.setStoreAddress(storeAddress != null ? storeAddress : store.getStoreAddress());
            store.setEmail(email != null ? email : store.getEmail());
            store.setStoreName(storeName != null ? storeName : store.getStoreName());
            store.setContact(contact != null ? contact : store.getContact());
            store.setGstNo(gstNo != null ? gstNo : store.getGstNo());
            store.setCurrency(currency != null ? currency : store.getCurrency());
            store.setCountry(country != null ? country : store.getCountry());
            store.setState(state != null ? state : store.getState());
            store.setLogo(logo != null ? logo.getBytes() : store.getLogo());
            store.setPinCode(pinCode != null ? pinCode : store.getPinCode());
            store.setAddress(address != null ? address : store.getAddress());
            store.setUpi(upi != null ? upi : store.getUpi());
            return ResponseEntity.ok()
                    .body(new ApiResponse(storeRepository.save(store), true, "Store updated successfully!", 200));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(null, false, "...", 500));
        }
    }


    @Override
    public ResponseEntity<ApiResponse> renewSubscriptionByStoreId(String username, String email, Integer year) {
        try {

            Optional<Store> existing = username != null ? storeRepository.findByUsername(username) : storeRepository.findByEmail(email);
            if (existing.isPresent()) {
                Store store = existing.get();
                Integer duration = 8544 * year;
                LocalDateTime currentExpiration = store.getSubscriptionExpiration();
                LocalDateTime newExpiration = currentExpiration.plusHours(duration);
                store.setSubscriptionExpiration(newExpiration);
                storeRepository.save(store);
                return ResponseEntity.ok().body(new ApiResponse(null, true, "Subscription Renewed for " + year + " year Successfully", 200));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse(null, false, "Store Not Found ", 404));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(null, false, "...", 500));

        }
    }

    @Override
    public ResponseEntity<ApiResponse> getFreeTrial(StoreSignupRequest storeSignupRequest) {
        try {
            if (storeRepository.existsByUsername(storeSignupRequest.getUsername())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(null, false, "Username already taken", 400));
            }
            if (storeRepository.existsByEmail(storeSignupRequest.getEmail())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(null, false, "Email already taken", 400));
            }
            if (storeRepository.existsByContact(String.valueOf(storeSignupRequest.getContact()))) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(null, false, "Username already taken", 400));
            }

            if (storeSignupRequest.getPassword().equals(storeSignupRequest.getComfirmpassword())) {
                Store registerStore = new Store(
                        storeSignupRequest.getUsername(),
                        storeSignupRequest.getStoreAddress(),
                        storeSignupRequest.getEmail(),
                        storeSignupRequest.getStore_name(),
                        storeSignupRequest.getContact(),
                        storeSignupRequest.getGstno(),
                        storeSignupRequest.getDate(),
                        storeSignupRequest.getCurrency(),
                        storeSignupRequest.getCountry(),
                        storeSignupRequest.getCountry_code(),
                        storeSignupRequest.getState(),
                        storeSignupRequest.getCreatedby(),
                        storeSignupRequest.getUpdatedby(),
                        storeSignupRequest.getComfirmpassword(),
                        storeSignupRequest.getSubscriptionType(),
                        storeSignupRequest.getUpi(),
                        storeSignupRequest.getAddress(),
                        storeSignupRequest.getPinCode(),
                        encoder.encode(storeSignupRequest.getPassword()));

                if (storeSignupRequest.isFreeTrialRequested()) {
                    // Determine the free trial duration based on user's choice
                    int freeTrialDurationHours = 0;
                    if (storeSignupRequest.getFreeTrialType() > 0) {
                        freeTrialDurationHours = 24 * storeSignupRequest.getFreeTrialType();
                    }
                    LocalDateTime subscriptionExpire = LocalDateTime.now().plusHours(freeTrialDurationHours);
                    registerStore.setSubscriptionExpiration(subscriptionExpire);
                    String date = String.valueOf(subscriptionExpire);

                    Set<String> strRoles = storeSignupRequest.getRole();
                    Set<StoreRole> storeRoles = new HashSet<>();
                    StoreRole adminStoreRole = storeRoleRepository.findByName(ERole.ROLE_ADMIN)
                            .orElseThrow(() -> new RuntimeException("Error: StoreRole is not found."));
                    storeRoles.add(adminStoreRole);
                    registerStore.setStoreRoles(storeRoles);

                    registerStore.setComfirmPassword(registerStore.getPassword());
                    Store freeTrialStore = storeRepository.save(registerStore);

                    emailSenderService.sendRegistrationSuccessfulEmailfreestore(registerStore.getEmail(), registerStore.getUsername(),
                            registerStore.getPassword(), storeSignupRequest.getFreeTrialType(), date);
                    return ResponseEntity.ok().body(new ApiResponse(freeTrialStore, true, "Store Registered Successfully", 200));
                }
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse(null, false, "Free trial is not requested ", 400));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(null, false, "Password Did not  matched !!", 400));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(null, false, "...", 500));
        }

    }

    @Override
    public ResponseEntity<ApiResponse> logoutStore(String sessionToken) {
        try {

            String username = getUsernameFromSessionToken(sessionToken);
            if (username.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(null, false, "Invalid session token.", 400));
            }
            removeUserSession(username, sessionToken);
            return ResponseEntity.ok().body(new ApiResponse(null, true, "Logged out successfully.", 200));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(null, false, "...", 500));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> getStoreById(Long storeid) {
        try {
            Optional<Store> store = storeRepository.findById(storeid);

            if (store.isPresent()) {
                return ResponseEntity.ok().body(new ApiResponse(store, true, 200));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(null, false, "Data Not Available", 404));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(null, false, "Not Available", 500));
        }
    }


    private void removeUserSession(String username, String sessionToken) {
        storeSessions.getOrDefault(username, Collections.emptySet()).remove(sessionToken);
    }

    private String getUsernameFromSessionToken(String sessionToken) {
        for (Map.Entry<String, Set<String>> entry : storeSessions.entrySet()) {
            if (entry.getValue().contains(sessionToken)) {
                return entry.getKey();
            }
        }
        return null;
    }


}