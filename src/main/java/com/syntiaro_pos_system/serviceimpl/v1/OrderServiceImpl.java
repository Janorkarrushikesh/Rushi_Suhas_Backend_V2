package com.syntiaro_pos_system.serviceimpl.v1;

import com.syntiaro_pos_system.entity.v1.*;
import com.syntiaro_pos_system.repository.v1.InventoryRepo;
import com.syntiaro_pos_system.repository.v1.OrderRepo;
import com.syntiaro_pos_system.repository.v1.ReceipeRepository;
import com.syntiaro_pos_system.service.v1.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderRepo orderRepo;

    @Autowired
    InventoryRepo inventoryRepository;
    @Autowired
    ReceipeRepository receipeRepository;


    //THIS METHOD IS USE FOR ADD ORDER
    @Override
    public int addOrder(Orders order) {
        orderRepo.save(order);
        return order.getOrderId();
    }

    //THIS METHOD IS USE FOR UPDATE ORDER
    @Override
    public Orders updateOrder(Orders order) {
        orderRepo.save(order);
        return order;
    }

    //THIS METHOD IS USE FOR DELETE ORDER
    @Override
    public void deleteorder(int parseInt) {
        Orders entity = orderRepo.getOne(parseInt);
        orderRepo.delete(entity);
    }

    //THIS METHOD IS USE FOR GET ORDER BY ID
    @Override
    public Optional<Orders> getorderbyid(Integer orderid) {
        return orderRepo.findById(orderid);
    }

    public int getorders(Orders orders) {
        orderRepo.save(orders);
        return orders.getOrderId();
    }

    // THIS METHOD IS USE FOR ORDER LIST
    @Override
    public List<Orders> getorder() {
        return orderRepo.findAll();
    }


    //THIS METHOD IS USE FOR GET ORDER LIST BY STOREID
    @Override
    public List<Orders> getOrdersByStoreId(String storeId) {
        return orderRepo.findBystoreId(storeId);
    }


    @Override
    public Orders placeOrders(OrderFood orderFood) {
        Receipe receipe = receipeRepository.findByNameAndStoreId(orderFood.getFoodName(), orderFood.getStoreId());

        if (receipe == null) {

        } else {
            int requestedQuantity = orderFood.getQuantity();
            List<Ingredient> ingredients = receipe.getIngredients();
            for (Ingredient ingredient : ingredients) {
                Inventory inventory = inventoryRepository.findByStoreIdAndName(ingredient.getStoreid(), ingredient.getName());
                if (inventory != null) {
                    float currentQuantity = inventory.getQuantity();
                    float requiredQuantity = ingredient.getQuantity() * requestedQuantity;
                    float newQuantity = currentQuantity - requiredQuantity;

                    if (newQuantity < 0) {
                        throw new RuntimeException("Insufficient quantity for: " + ingredient.getName());
                    } else {
                        inventory.setQuantity(newQuantity);
                        inventoryRepository.save(inventory);
                    }
                } else {
                    throw new RuntimeException("Inventory item not found: " + ingredient.getName());
                }
            }
        }
        return new Orders(); // Replace this with your response.
    }

}
