package com.syntiaro_pos_system.controllerimpl.v1;

import com.syntiaro_pos_system.controller.v1.OrderFoodController;
import com.syntiaro_pos_system.entity.v1.OrderFood;
import com.syntiaro_pos_system.repository.v1.OrderFoodRepo;
import com.syntiaro_pos_system.service.v1.OrderFoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class OrderFoodControllerImpl implements OrderFoodController {

    @Autowired
    private OrderFoodService orderFoodService;

    @Autowired
    private OrderFoodRepo orderFoodRepo;

    // THIS METHOD IS USE FOR GET ALL LIST OF FOOD
    @Override
    public List<OrderFood> getFood(String food) {
        return this.orderFoodService.getFood();

    }

    // THIS METHOD IS USE FOR POST FOOD
    @Override
    public String addFood(@RequestBody OrderFood orderFood) {

        String id = orderFoodService.addFood(orderFood);
        return id;
    }

    @Override
    public ResponseEntity<OrderFood> createFood(
            @RequestParam("fname") String fname,
            @RequestParam("category") String category,
            @RequestParam("subcategory") String subcategory,
            @RequestParam("store_id") String store_id,
            @RequestParam("price") Integer price,
            @RequestParam("quantity") Integer quantity) {

        OrderFood foods = new OrderFood();
        foods.setFoodName(fname);
        foods.setCategory(category);
        foods.setSubCategory(subcategory);
        foods.setStoreId(store_id);
        foods.setPrice(price);
        foods.setQuantity(quantity);
        OrderFood createdOrderFood = orderFoodRepo.save(foods);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrderFood);
    }

    // THIS METHOD IS USE FOR UPDATE FOOD
    @Override
    public OrderFood updateFood(@RequestBody OrderFood orderFood, @RequestParam("image") MultipartFile image) {
        return this.orderFoodService.updateFood(orderFood);
    }

    // THIS METHOD IS USE FOR DELETE FOOD
    @Override
    public ResponseEntity<HttpStatus> deleteFood(@PathVariable String srno) {
        try {
            this.orderFoodService.deletefood(Integer.parseInt(srno));
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // THIS METHOD IS USE FOR FETCH ORDERFOOD BY STOREID
    @Override
    public List<OrderFood> getOrderFoodByStoreId(@PathVariable String storeId) {
        return orderFoodService.getOrderFoodByStoreId(storeId);
    }

    // ----------------------RUSHIKESH ADDED THIS NEW CODE --------------
    @Override
    public OrderFood getTotalQuantityByFoodName(@PathVariable String storeId) {
        List<Object[]> resultList = orderFoodRepo.getTotalQuantityByFoodName(storeId);

        if (!resultList.isEmpty()) {
            Object[] firstResult = resultList.get(0);
            String foodName = (String) firstResult[0]; // Assuming the first element is the food name
            Long quantity = (Long) firstResult[1]; // Assuming the second element is the quantity
            // Create a custom result object with field names
            OrderFood result = new OrderFood(foodName, Math.toIntExact(quantity));
            return result;
        } else {
            return null; // Return null if there are no results
        }
    }

}

