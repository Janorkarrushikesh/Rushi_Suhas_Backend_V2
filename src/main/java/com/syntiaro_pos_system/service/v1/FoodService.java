package com.syntiaro_pos_system.service.v1;

import com.syntiaro_pos_system.entity.v1.Food;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface FoodService {
    String addFood(Food food);

    List<Food> getFood();

    Food updateFood(Food food);

    void deletefood(int parseInt);


    // THIS METHOD IS USE FOR UPDATE FOOD
    Food updateFood(Integer food_id, Food food);


    //THIS METHOD IS USE FOR NOT INSERT SAME FOOD NAME
    Food saveFood(Food food);

    // THIS METHOD IS USE FOR FETCH FOOD BY ID
    Optional<Food> getfoodbyid(Integer food_id);


    List<Food> processExcelFile(String storeId, MultipartFile file) throws IOException;


    // THIS METHOD IS USE FOR FETCH FOOD BY STOREID
    List<Food> fetchFoodsByStoreId(String storeId);


}
