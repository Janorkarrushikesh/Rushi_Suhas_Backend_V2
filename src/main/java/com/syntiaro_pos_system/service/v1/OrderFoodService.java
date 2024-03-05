package com.syntiaro_pos_system.service.v1;

import com.syntiaro_pos_system.entity.v1.OrderFood;

import java.util.List;

public interface OrderFoodService {
    String addFood(OrderFood orderFood);

    List<OrderFood> getFood();

    OrderFood updateFood(OrderFood orderFood);

    void deletefood(int parseInt);

    // THIS METHOD IS USE FOR FETCH ORDERFOOD BY STOREID
    List<OrderFood> getOrderFoodByStoreId(String storeId);


}
