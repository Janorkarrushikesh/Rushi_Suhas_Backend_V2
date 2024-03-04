package com.syntiaro_pos_system.request.v1;

import com.syntiaro_pos_system.entity.v1.Bill;
import com.syntiaro_pos_system.entity.v1.OrderFood;
import com.syntiaro_pos_system.entity.v1.Orders;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BillRequest {


    //USE FOR BILL AND ORDER MAPPING!!!
    private Bill bill;
    private List<Orders> order; // Change the field name to "order"
    private List<OrderFood> orderFood;

    public List<Orders> getOrder() {
        return order;
    }

    public void setOrder(List<Orders> order) {
        this.order = order;
    }

    public List<OrderFood> getOrderFood() {
        return orderFood;
    }

    public void setOrderFood(List<OrderFood> orderFood) {
        this.orderFood = orderFood;
    }

    public Bill getBill() {
        return bill;
    }

    public void setBill(Bill bill) {
        this.bill = bill;
    }
}
