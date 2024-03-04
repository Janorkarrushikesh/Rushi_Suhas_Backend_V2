package com.syntiaro_pos_system.serviceimpl.v2;

import com.syntiaro_pos_system.entity.v1.UserSidebar;
import com.syntiaro_pos_system.entity.v2.ApiResponse;
import com.syntiaro_pos_system.repository.v2.UserSideBarRepositry;
import com.syntiaro_pos_system.service.v2.UserSideBarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;




@Service
public class UserSideBarServiceImpl implements UserSideBarService {

    @Autowired
    UserSideBarRepositry userSideBarRepositry;

    @Override
    public ResponseEntity<ApiResponse> saveUserSideBar(UserSidebar userSidebar) {
        try{
         boolean username =   userSideBarRepositry.existsByUsername(userSidebar.getUsername());
         if(username == false) {
             UserSidebar userSidebars = userSideBarRepositry.save(userSidebar);
             return ResponseEntity.ok().body(new ApiResponse(userSidebars, true, 200));
         }else {
             return ResponseEntity.status(HttpStatus.CONFLICT)
                     .body(new ApiResponse(null,false,"Already User register !!",HttpStatus.CONFLICT.value()));
         }
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(null,false,"...",HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }




    @Override
     public ResponseEntity<ApiResponse> getUserSidebarByStoreId(String storeId) {
        try {
            List<UserSidebar> userSidebarList = userSideBarRepositry.findByStoreId(storeId);
            if (userSidebarList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse(null, false,"User sidebar not found", HttpStatus.NOT_FOUND.value()));
            } else {
                return ResponseEntity.ok(new ApiResponse(userSidebarList, true, HttpStatus.OK.value()));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(null, false, "Failed to retrieve user sidebar",HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> getUserSidebarById(Integer userId) {
        try {
            Optional<UserSidebar> userSidebarList = userSideBarRepositry.findById(userId);
            if (userSidebarList.isPresent()) {
                return ResponseEntity.ok(new ApiResponse(userSidebarList.get(), true, HttpStatus.OK.value()));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse("User sidebar not found", false, HttpStatus.NOT_FOUND.value()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Failed to retrieve user sidebar", false, HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> updateUsersidebar(Integer userId , UserSidebar userSidebar) {
       try{
           Optional<UserSidebar> userSidebarList = userSideBarRepositry.findById(userId);
           if(userSidebarList.isPresent()){
               return ResponseEntity.ok(new ApiResponse(userSideBarRepositry.save(userSidebar),true,200));
           }else {
               return ResponseEntity.status(HttpStatus.NOT_FOUND)
                       .body(new ApiResponse(null,false,"User sidebar not found",  HttpStatus.NOT_FOUND.value()));
           }

       }
       catch (Exception e){
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                   .body(new ApiResponse(null,false,"....",500));
       }
    }


}
