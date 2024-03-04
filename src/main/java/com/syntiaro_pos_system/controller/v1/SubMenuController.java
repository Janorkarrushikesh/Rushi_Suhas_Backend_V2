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
    public String saveSubMenu(@RequestBody SubMenu subMenu);

    @GetMapping(path = "/allSubMenu")
    public List<SubMenu> getSubMenu();

    @DeleteMapping(path = "/delete/{subMenu_id}")
    public ResponseEntity<HttpStatus> deleteSubMenu(@PathVariable String subMenu_id);

    @PatchMapping("/patchsubmenu/{id}")
    public ResponseEntity<SubMenu> updateSubMenuFields(
            @PathVariable("id") int id,
            @RequestBody Map<String, String> updatedFields) ;

}
