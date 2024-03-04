package com.syntiaro_pos_system.service.v1;

import com.syntiaro_pos_system.entity.v1.Menu;
import java.util.List;

public interface MenuService  {


    String addMenu(Menu menu);

    List<Menu> getMenu();

    void deletemenu(int i);


    Menu updatedMenu(int id, Menu menu);

    Menu getMenuById(int parseInt);
}


