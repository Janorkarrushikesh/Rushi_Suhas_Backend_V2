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
    public ResponseEntity<?> createFoodItem(@RequestBody Receipe receipe) ;

    @GetMapping("/getfood")
    public List<Receipe> getallfood() ;

    // {--------FOR GET FOOD ITEM BY ID -------}
    @GetMapping("/getbyid/{Ser_no}") // WRITE BY RUSHIKESH
    public ResponseEntity<?> getFoodById(@PathVariable Long Ser_no) ;

    @GetMapping("/getfood/{storeid}")
    public List<Receipe> getFoodItemsByStoreId(@PathVariable String storeid) ;

    @DeleteMapping("/deletefood/{Ser_no}")
    public ResponseEntity<String> deleteFoodItemById(@PathVariable Long Ser_no) ;

    // ----for fooditem update
    @PatchMapping("/updatefood/{Ser_no}") // MADE BY RUSHIKESH
    public ResponseEntity<?> updateFoodItem(@PathVariable Long Ser_no, @RequestBody Receipe updatedReceipe) ;
}
