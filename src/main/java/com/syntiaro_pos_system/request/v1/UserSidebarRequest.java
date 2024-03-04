package com.syntiaro_pos_system.request.v1;

import com.syntiaro_pos_system.entity.v1.UserSidebar;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.awt.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserSidebarRequest {
    private Menu menu;
    private UserSidebar userSidebar;
    private String username;

}
