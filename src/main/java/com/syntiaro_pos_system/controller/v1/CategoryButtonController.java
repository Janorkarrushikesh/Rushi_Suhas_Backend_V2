package com.syntiaro_pos_system.controller.v1;

import com.syntiaro_pos_system.entity.v1.CategoryButton;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/v1/sys/Button")
@CrossOrigin(origins = "*", maxAge = 3600)
public interface CategoryButtonController {


    @GetMapping("/getbutton/{store_id}")
    ResponseEntity<List<CategoryButton>> getFoodsByStoreId(@PathVariable String store_id);

    @PostMapping("/postbutton")
    ResponseEntity<?> createEntity(@RequestBody CategoryButton categoryButton);


    @PatchMapping("/updateButton/{id}")
    ResponseEntity<CategoryButton> updateButton(@PathVariable("id") Long id, @RequestBody CategoryButton categoryButton);

    @GetMapping("/getcategory/{id}")
    CategoryButton fetchDetailsById(@PathVariable Long id);


    @DeleteMapping("/deletecategory/{id}")
    ResponseEntity<HttpStatus> deleteButton(@PathVariable Long id);

}
