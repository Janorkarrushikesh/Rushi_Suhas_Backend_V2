package com.syntiaro_pos_system.security.services;

import com.syntiaro_pos_system.entity.v1.OrderFood;

import java.util.List;

public interface OrderFoodService {
    String addFood(OrderFood orderFood);

    List<OrderFood> getFood();

    OrderFood updateFood(OrderFood orderFood);

    void deletefood(int parseInt);


}
