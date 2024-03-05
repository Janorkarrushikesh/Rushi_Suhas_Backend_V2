package com.syntiaro_pos_system.controller.v1;

import com.syntiaro_pos_system.entity.v1.Addon;
import com.syntiaro_pos_system.entity.v1.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(path = "/v1/sys/addons")
public interface AddonRest {
    @PostMapping(path = "/saveaddon")
    ApiResponse saveaddon(@RequestBody Addon addon);

    @GetMapping(path = "/allAddon")
    ApiResponse getAddon();

    @PutMapping(path = "/updateaddon")
    Addon updateAddon(@RequestBody Addon addon);

    @DeleteMapping(path = "/addon/{itemid}")
    ResponseEntity<HttpStatus> deleteUser(@PathVariable String itemid);

    @GetMapping("/getAddonByID/{itemid}")
    Addon fetchDetailsById(@PathVariable Integer itemid);

    @PatchMapping(path = "/updateaddon/{itemid}")
    ResponseEntity<Addon> updateAddon(@PathVariable("itemid") Integer itemid, @RequestBody Addon foodAddon);

    @GetMapping("/addon/{storeId}")
    List<Addon> getAddonsByStoreId(@PathVariable String storeId);

}
