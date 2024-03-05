package com.syntiaro_pos_system.service.v1;


import com.syntiaro_pos_system.entity.v1.OrderFood;
import com.syntiaro_pos_system.entity.v1.Orders;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    List<Orders> getorder();

    int addOrder(Orders order);

    Orders updateOrder(Orders order);

    void deleteorder(int i);

    Optional<Orders> getorderbyid(Integer orderid);

    // fetch by storeid
    List<Orders> getOrdersByStoreId(String storeId);

    Orders placeOrders(OrderFood orderFood);


}
