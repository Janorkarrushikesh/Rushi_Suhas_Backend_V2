package com.syntiaro_pos_system.serviceimpl.v1;

import com.syntiaro_pos_system.entity.v1.Food;
import com.syntiaro_pos_system.repository.v1.FoodRepo;
import com.syntiaro_pos_system.service.v1.FoodService;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.*;


@Service
public class FoodServiceIMPL implements FoodService {

	@Autowired
	private FoodRepo foodRepo;

	//THIS METHOD IS USE FOR POST FOOD
	@Override
	public String addFood(Food food) {
		foodRepo.save(food);
		return food.getFoodName();
	}

	//THIS METHOD IS USE FOR GET ALL LIST OF FOOD
	@Override
	public List<Food> getFood() {
		// TODO Auto-generated method stub
		return foodRepo.findAll();
	}

	//THIS METHOD IS USE FOR UPDATE FOOD
	@Override
	public Food updateFood(Food food) {
		foodRepo.save(food);
		return food;
	}

	//THIS METHOD IS USE FOR DELETE FOOD
	@Override
	public void deletefood(int parseInt) {
		Food entity = foodRepo.getOne(parseInt);
		foodRepo.delete(entity);
	}
	//THIS METHOD IS USE FOR NOT INSERT SAME FOOD NAME
	@Override
	public Food saveFood(Food food) {
		String foodName = food.getFoodName();
		String storeId = food.getStoreId();
		// Check if the food name already exists in the store
		boolean foodExistsInStore = foodRepo.existsByFoodNameAndStoreId(foodName, storeId);
		if (foodExistsInStore) {
			return null;
		}
		return foodRepo.save(food);
	}


	// THIS METHOD IS USE FOR FETCH FOOD BY ID
	@Override
	public Optional<Food> getfoodbyid(Integer food_id) {
		return foodRepo.findById(food_id);
	}


	// THIS METHOD IS USE FOR UPDATE FOOD
	@Override
	public Food updateFood(Integer food_id, Food food) {
		Optional<Food> existingFood = foodRepo.findById((int) Integer.parseInt(String.valueOf((food_id))));
		if (existingFood.isPresent()) {
			Food updatefood = existingFood.get();
			if (food.getFoodName() != null) {
				updatefood.setFoodName(food.getFoodName());
			}
			if (food.getDescription() != null) {
				updatefood.setDescription(food.getDescription());
			}
			if (food.getCategory() != null) {
				updatefood.setCategory(food.getCategory());
			}
			if (food.getSubCategory() != null) {
				updatefood.setSubCategory(food.getSubCategory());
			}
			if (food.getUpdateBy() != null) {
				updatefood.setUpdateBy(food.getUpdateBy());
			}
			if (food.getGstNo() != null) {
				updatefood.setGstNo(food.getGstNo());
			}
			if (food.getCreatedBy() != null) {
				updatefood.setCreatedBy(food.getCreatedBy());
			}
			if (food.getStoreId() != null) {
				updatefood.setStoreId(food.getStoreId());
			}
			if (food.getPrice() != null) {
				updatefood.setPrice(food.getPrice());
			}
			if (food.getFoodCode() != null) {
				updatefood.setFoodCode(food.getFoodCode());
			}
			foodRepo.save(updatefood);
			return updatefood;
		} else {
			return null;
		}
	}


	// THIS METHOD IS USE FOR FETCH FOOD BY STOREID
	@Override
	public List<Food> fetchFoodsByStoreId(String storeId) {
		return foodRepo.findByStore_id(storeId);
	}

	@Override
	public List<Food> processExcelFile(String storeId, MultipartFile file) throws IOException {
		List<Food> foodList = new ArrayList<>();

		Workbook workbook = WorkbookFactory.create(file.getInputStream());
		Sheet sheet = workbook.getSheetAt(0);
		Iterator<Row> rowIterator = sheet.iterator();
		rowIterator.next(); // Skip the header row if present
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();
			String foodName = row.getCell(0).getStringCellValue();

			// Check if the food with the same name already exists
			if (foodRepo.existsByFoodNameAndStoreId(foodName, storeId)) {
				continue;
			}

			Food food = new Food();
			food.setStoreId(storeId);
			food.setFoodName(foodName);
			food.setFoodId((int) row.getCell(1).getNumericCellValue());
			food.setCategory(row.getCell(2).getStringCellValue());
			food.setDescription(row.getCell(3).getStringCellValue());
			food.setSubCategory(row.getCell(4).getStringCellValue());
			food.setPrice((int) row.getCell(5).getNumericCellValue());
			food.setFoodCode(row.getCell(6).getStringCellValue());

			// Save the food only if it doesn't already exist
			foodRepo.save(food);
			foodList.add(food);
		}
		workbook.close();
		return foodList;
	}



}	