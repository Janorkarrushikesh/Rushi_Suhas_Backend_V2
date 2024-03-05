package com.syntiaro_pos_system.controller.v1;

import com.syntiaro_pos_system.entity.v1.SubMenu;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/v1/sys/submenu")
@CrossOrigin(origins = "*", maxAge = 3600)
public interface SubMenuController {

    @PostMapping(path = "/saves")
    String saveSubMenu(@RequestBody SubMenu subMenu);

    @GetMapping(path = "/allSubMenu")
    List<SubMenu> getSubMenu();

    @DeleteMapping(path = "/delete/{subMenu_id}")
    ResponseEntity<HttpStatus> deleteSubMenu(@PathVariable String subMenu_id);

    @PatchMapping("/patchsubmenu/{id}")
    ResponseEntity<SubMenu> updateSubMenuFields(
            @PathVariable("id") int id,
            @RequestBody Map<String, String> updatedFields);

}
