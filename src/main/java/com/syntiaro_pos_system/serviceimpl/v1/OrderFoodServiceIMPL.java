package com.syntiaro_pos_system.serviceimpl.v1;

import com.syntiaro_pos_system.entity.v1.OrderFood;
import com.syntiaro_pos_system.repository.v1.OrderFoodRepo;
import com.syntiaro_pos_system.service.v1.OrderFoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class OrderFoodServiceIMPL implements OrderFoodService {


	@Autowired
	private OrderFoodRepo orderFoodRepo;

	//THIS METHOD IS USE FOR ADD ORDERFOOD
	@Override
	public String addFood(OrderFood orderFood) {
		orderFoodRepo.save(orderFood);
		return orderFood.getFoodName();
	}

	//THIS METHOD IS USE FOR GET ALL LIST OF ORDERFOOD
	@Override
	public List<OrderFood> getFood() {
		// TODO Auto-generated method stub
		return orderFoodRepo.findAll();
	}

	//THIS METHOD IS USE FOR UPDATE ORDERFOOD
	@Override
	public OrderFood updateFood(OrderFood orderFood) {
		orderFoodRepo.save(orderFood);
		return orderFood;
	}

	//THIS METHOD IS USE FOR DELETE ORDERFOOD
	@Override
	public void deletefood(int parseInt) {
		OrderFood entity = orderFoodRepo.getOne(parseInt);
		orderFoodRepo.delete(entity);
	}

	// THIS METHOD IS USE FOR FETCH ORDERFOOD BY STOREID
	@Override
	public List<OrderFood> getOrderFoodByStoreId(String storeId) {
		return orderFoodRepo.findByStoreId(storeId);
	}

}	