package com.syntiaro_pos_system.controllerimpl.v1;

import com.syntiaro_pos_system.controller.v1.MenuController;
import com.syntiaro_pos_system.entity.v1.Menu;
import com.syntiaro_pos_system.repository.v1.MenuRepo;
import com.syntiaro_pos_system.service.v1.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class MenuControllerImpl implements MenuController {
    @Autowired
    MenuService menuService;

    @Autowired
    MenuRepo menuRepo;
    private Object parseInt;

    // THIS METHOD IS USE POST MENU
    @Override
    public String saveMenu(Menu menu) {
        return menuService.addMenu(menu);
    }

    // THIS METHOD IS USE FOR GET ALL LIST OF MENU
    @Override
    public List<Menu> getMenu() {
        return menuService.getMenu();
    }

    // THIS METHOD IS USE FOR DELETE MENU
    @Override
    public ResponseEntity<HttpStatus> deleteMenu(String menu_id) {
        try {
            this.menuService.deletemenu(Integer.parseInt(menu_id));
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    // THIS METHOD IS USE FOR UPDATE MENU
    @Override
    public ResponseEntity<Menu> updateMenuFields(
            @PathVariable("id") int id,
            @RequestBody Map<String, String> updatedFields) {
        Menu menu = menuService.getMenuById((Integer) parseInt);// getSubMenuById
        // Update fields based on the provided values
        if (updatedFields.containsKey("title")) {
            String updatedTitle = updatedFields.get("title");
            menu.setTitle(updatedTitle);
        }
        if (updatedFields.containsKey("path")) {
            String updatedPath = updatedFields.get("path");
            menu.setPath(updatedPath);
        }
        Menu updatedMenu = menuService.updatedMenu(id, menu);
        return new ResponseEntity<>(updatedMenu, HttpStatus.OK);
    }
}
