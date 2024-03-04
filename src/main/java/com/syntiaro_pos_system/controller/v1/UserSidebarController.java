package com.syntiaro_pos_system.controller.v1;

import com.syntiaro_pos_system.entity.v1.UserSidebar;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/v1/sys/usersidebar")
@CrossOrigin(origins = "*", maxAge = 3600)
public interface UserSidebarController {
    @GetMapping(path = "/allUser")
    List<UserSidebar> getUser();

    @GetMapping("/getUserBYID/{id}")
    public ResponseEntity<?> fetchDetailsById(@PathVariable Integer id);
    @PostMapping(path = "/save")
    public ResponseEntity<String> saveUser(@RequestBody UserSidebar userSidebar);

    @PutMapping(path = "/UserSidebar")
    public UserSidebar updateUser(@RequestBody UserSidebar userSidebar);

    @GetMapping(path = "/users/{username}")
    public ResponseEntity<UserSidebar> getUserByUsername(@PathVariable String username);

    @DeleteMapping(path = "/delete/{user_id}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable String user_id);

    @GetMapping("/store/{storeId}/users")
    public ResponseEntity<List<UserSidebar>> getUsersByStoreId(@PathVariable String storeId) ;

}
