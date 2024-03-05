package com.syntiaro_pos_system.controller.v1;

import com.syntiaro_pos_system.entity.v1.Receipe;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/v1/sys/fooditems")
public interface FoodItemController {

    @PostMapping("/addfood")
    ResponseEntity<?> createFoodItem(@RequestBody Receipe receipe);

    @GetMapping("/getfood")
    List<Receipe> getallfood();

    // {--------FOR GET FOOD ITEM BY ID -------}
    @GetMapping("/getbyid/{Ser_no}") // WRITE BY RUSHIKESH
    ResponseEntity<?> getFoodById(@PathVariable Long Ser_no);

    @GetMapping("/getfood/{storeid}")
    List<Receipe> getFoodItemsByStoreId(@PathVariable String storeid);

    @DeleteMapping("/deletefood/{Ser_no}")
    ResponseEntity<String> deleteFoodItemById(@PathVariable Long Ser_no);

    // ----for fooditem update
    @PatchMapping("/updatefood/{Ser_no}") // MADE BY RUSHIKESH
    ResponseEntity<?> updateFoodItem(@PathVariable Long Ser_no, @RequestBody Receipe updatedReceipe);
}
