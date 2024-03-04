package com.syntiaro_pos_system.serviceimpl.v2;

import com.syntiaro_pos_system.entity.v1.Menu;
import com.syntiaro_pos_system.entity.v1.SubMenu;
import com.syntiaro_pos_system.entity.v1.UserSidebar;
import com.syntiaro_pos_system.entity.v2.ApiResponse;
import com.syntiaro_pos_system.repository.v2.UserSideBarRepositry;
import com.syntiaro_pos_system.service.v2.UserSideBarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
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
                        .body(new ApiResponse(null,false,"Already User register !!",409));
            }
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(null,false,"...",500));
        }
    }




    @Override
    public ResponseEntity<ApiResponse> getUserSidebarByStoreId(String storeId) {
        try {
            List<UserSidebar> userSidebarList = userSideBarRepositry.findByStoreId(storeId);
            if (userSidebarList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse(null, false,"User sidebar not found", 404));
            } else {
                return ResponseEntity.ok(new ApiResponse(userSidebarList, true, HttpStatus.OK.value()));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(null, false, "Failed to retrieve user sidebar",500));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> getUserSidebarById(Integer userId) {
        try {
            Optional<UserSidebar> existingUserSidebar = userSideBarRepositry.findById(userId);
            if (existingUserSidebar.isPresent()) {
                return ResponseEntity.ok(new ApiResponse(existingUserSidebar.get(), true, 200));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse("User sidebar not found", false,404));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Failed to retrieve user sidebar", false,500));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> updateUsersidebar(Integer userId , UserSidebar userSidebar) {
        try{
            Optional<UserSidebar> existingUserSidebar = userSideBarRepositry.findById(userId);
            if(existingUserSidebar.isPresent()){
                return ResponseEntity.ok(new ApiResponse(userSideBarRepositry.save(userSidebar),true,200));
            }else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse(null,false,"User sidebar not found",404));
            }
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(null,false,"....",500));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> getByStatusAndStoreId(String storeId ) {
        try {

            List list = new ArrayList<>();
            List<UserSidebar> userSidebarList = userSideBarRepositry.findByStoreId(storeId);
            if (userSidebarList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse(null, false, "User sidebar for given store Id  not found", 404));
            }
            for (UserSidebar userSidebar : userSidebarList) {
                List<Menu> menuList = userSidebar.getMenu();
                for (Menu menu : menuList) {
                    if (menu.isStatus()) {

                        List<SubMenu> subMenuList = menu.getSubMenu();
                        for (SubMenu subMenu : subMenuList) {
                            if (subMenu.isStatus()) {
                                list.add(userSidebar);
                                list.add(menu);
                                list.add(subMenu);
                                return ResponseEntity.ok()
                                        .body(new ApiResponse(list, false, 200));
                            }
                        }
                    }
                }
            }
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body(new ApiResponse(null, false, 404));
                    }catch(Exception e){
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(new ApiResponse(null, false, "....", 500));
                    }
                }

}
