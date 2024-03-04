package com.syntiaro_pos_system.request.v1;


import com.syntiaro_pos_system.entity.v1.OrderFood;
import com.syntiaro_pos_system.entity.v1.Orders;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderRequest {

    // USE FOR ORDER AND FOOD MAPPING
    private Orders order;
    private OrderFood orderFood;

    public Orders getOrder() {
        return order;
    }

    public void setOrder(Orders order) {
        this.order = order;
    }

    public OrderFood getOrderFood() {
        return orderFood;
    }

    public void setOrderFood(OrderFood orderFood) {
        this.orderFood = orderFood;
    }
}
