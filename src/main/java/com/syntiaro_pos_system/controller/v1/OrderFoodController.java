package com.syntiaro_pos_system.controller.v1;

import com.syntiaro_pos_system.entity.v1.OrderFood;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(path = "/v1/sys/orderfood")
@CrossOrigin(origins = "*", maxAge = 3600)
public interface OrderFoodController {
    @GetMapping(path = "/getfood")
    List<OrderFood> getFood(String food);

    // THIS METHOD IS USE FOR POST FOOD
    @PostMapping(path = "/postfood")
    String addFood(@RequestBody OrderFood orderFood);

    @PostMapping(path = "/foodimg")
    ResponseEntity<OrderFood> createFood(
            @RequestParam("fname") String fname,
            @RequestParam("category") String category,
            @RequestParam("subcategory") String subcategory,
            @RequestParam("store_id") String store_id,
            @RequestParam("price") Integer price,
            @RequestParam("quantity") Integer quantity);

    // THIS METHOD IS USE FOR UPDATE FOOD
    @PutMapping(path = "/updatefood")
    OrderFood updateFood(@RequestBody OrderFood orderFood, @RequestParam("image") MultipartFile image);

    // THIS METHOD IS USE FOR DELETE FOOD
    @DeleteMapping(path = "/food/{foodid}")
    ResponseEntity<HttpStatus> deleteFood(@PathVariable String foodid);

    // THIS METHOD IS USE FOR FETCH ORDERFOOD BY STOREID
    @GetMapping("/orderfood/{storeId}")
    List<OrderFood> getOrderFoodByStoreId(@PathVariable String storeId);

    // ----------------------RUSHIKESH ADDED THIS NEW CODE --------------
    @GetMapping("/food-quantity/{storeId}")
    OrderFood getTotalQuantityByFoodName(@PathVariable String storeId);
}
