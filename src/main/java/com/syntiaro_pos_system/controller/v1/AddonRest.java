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
        public ApiResponse saveaddon(@RequestBody Addon addon);
        @GetMapping(path = "/allAddon")
        public ApiResponse getAddon();
        @PutMapping(path = "/updateaddon")
        public Addon updateAddon(@RequestBody Addon addon);
        @DeleteMapping(path = "/addon/{itemid}")
        public ResponseEntity<HttpStatus> deleteUser(@PathVariable String itemid);
        @GetMapping("/getAddonByID/{itemid}")
        public Addon fetchDetailsById(@PathVariable Integer itemid);
        @PatchMapping(path = "/updateaddon/{itemid}")
        public ResponseEntity<Addon> updateAddon( @PathVariable("itemid") Integer itemid,  @RequestBody Addon foodAddon);
        @GetMapping("/addon/{storeId}")
        public List<Addon> getAddonsByStoreId(@PathVariable String storeId);

}
