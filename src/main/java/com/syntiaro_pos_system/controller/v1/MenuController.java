package com.syntiaro_pos_system.controller.v1;

import com.syntiaro_pos_system.entity.v1.Menu;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(path = "/v1/sys/menu")
public interface MenuController {
    @PostMapping(path = "/saves")
    String saveMenu(@RequestBody Menu menu);

    @GetMapping(path = "/allMenu")
    List<Menu> getMenu();

    @DeleteMapping(path = "/delete/{menu_id}")
    ResponseEntity<HttpStatus> deleteMenu(@PathVariable String menu_id);

    @PatchMapping("/patchmenu/{id}")
    ResponseEntity<Menu> updateMenuFields(
            @PathVariable("id") int id,
            @RequestBody Map<String, String> updatedFields);
}
