package com.syntiaro_pos_system.controllerimpl.v1;

import com.syntiaro_pos_system.controller.v1.UserSidebarController;
import com.syntiaro_pos_system.entity.v1.UserSidebar;
import com.syntiaro_pos_system.repository.v1.UserSidebarRepo;
import com.syntiaro_pos_system.service.v1.UserSidebarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class UserSidebarControllerImpl implements UserSidebarController {
    @Autowired
    UserSidebarService userSidebarService;
    @Autowired
    UserSidebarRepo userSidebarRepo;

    //THIS METHOD IS USE FOR POST USER
    @Override
    public List<UserSidebar> getUser() {
        return userSidebarService.getUsers();
    }

    // THIS METHOD IS USE FOR FETCH USER BY ID
    @Override
    public ResponseEntity<?> fetchDetailsById(Integer id) {
        Optional<UserSidebar> usersidebar = userSidebarService.getUserDetailsById(id);
        if (usersidebar.isPresent()) {
            return ResponseEntity.ok(usersidebar.get());
        } else {
            return ResponseEntity.notFound().build();
        }

    }

    //THIS METHOD IS USE FOR UPDATE USER
    @Override // For Update Data
    public UserSidebar updateUser(@RequestBody UserSidebar userSidebar) {
        return this.userSidebarService.updateUser(userSidebar);
    }
    //THIS METHOD IS USE FOR POST USER

    @Override
    public ResponseEntity<String> saveUser(@RequestBody UserSidebar userSidebar) {
        boolean isUsernameTaken = userSidebarService.isUsernameTaken(userSidebar.getUsername());
        if (isUsernameTaken) {
            return ResponseEntity.badRequest().body("Error: Username is already taken!");
        }

        userSidebarService.saveUser(userSidebar);
        return ResponseEntity.ok("UserSidebar saved successfully!");
    }

    //THIS METHOD IS USE FOR GET USER BY USERNAME
    @Override
    public ResponseEntity<UserSidebar> getUserByUsername(@PathVariable String username) {
        UserSidebar userSidebar = userSidebarRepo.findByUsername(username);
        if (userSidebar != null) {
            return ResponseEntity.ok(userSidebar);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // THIS METHOD IS USE FOR FETCH USERSIDEBAR BY STOREID
    @Override
    public ResponseEntity<List<UserSidebar>> getUsersByStoreId(@PathVariable String storeId) {
        List<UserSidebar> users = userSidebarRepo.getUsersByStoreId(storeId);
        if (!users.isEmpty()) {
            return ResponseEntity.ok(users);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //THIS METHOD IS USE FOR DELETE USER
    @Override
    public ResponseEntity<HttpStatus> deleteUser(String user_id) {
        try {
            this.userSidebarRepo.deleteById(Integer.parseInt(user_id));
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}


