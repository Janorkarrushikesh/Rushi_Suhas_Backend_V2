package com.syntiaro_pos_system.service.v1;

import com.syntiaro_pos_system.entity.v1.UserSidebar;

import java.util.List;
import java.util.Optional;

public interface UserSidebarService {
    //String addUser(UserSidebar user);
    boolean isUsernameTaken(String username);

    void saveUser(UserSidebar userSidebar);

    List<UserSidebar> getUsers();


    void deleteuser(int i);


    // THIS METHOD IS USE FOR FETCH USERSIDEBAR BY ID
    Optional<UserSidebar> getUserDetailsById(Integer id);

    UserSidebar updateUser(UserSidebar userSidebar);


}

