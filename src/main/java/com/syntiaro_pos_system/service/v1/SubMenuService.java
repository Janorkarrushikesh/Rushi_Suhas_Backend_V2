package com.syntiaro_pos_system.service.v1;

import com.syntiaro_pos_system.entity.v1.SubMenu;

import java.util.List;

public interface SubMenuService {

    String addSubMenu(SubMenu subMenu);

    List<SubMenu> getSubMenu();

    void deletesubMenu(int i);

    SubMenu getSubMenuById(int parseInt);

    SubMenu updatedSubMenu(int id, SubMenu subMenu);
}
