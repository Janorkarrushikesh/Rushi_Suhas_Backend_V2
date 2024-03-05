package com.syntiaro_pos_system.serviceimpl.v1;

import com.syntiaro_pos_system.entity.v1.Ingredient;
import com.syntiaro_pos_system.entity.v1.Receipe;
import com.syntiaro_pos_system.repository.v1.FoodItemRepository;
import com.syntiaro_pos_system.repository.v1.InventoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FoodItemService {

    @Autowired
    InventoryRepo inventoryRepository;
    private final FoodItemRepository foodItemRepository;

    @Autowired
    public FoodItemService(FoodItemRepository foodItemRepository) {
        this.foodItemRepository = foodItemRepository;
    }

    // Create a new food item
    public Receipe createFoodItem(Receipe foodItem) {


        Long lastBillNumber = foodItemRepository.findLastNumberForStore(foodItem.getStoreId());
        foodItem.setId(lastBillNumber != null ? lastBillNumber + 1 : 1);


        return foodItemRepository.save(foodItem);
    }

    // Read all food items
    public List<Receipe> getAllFoodItems() {
        return foodItemRepository.findAll();
    }

    // Read a single food item by ID
    public Optional<Receipe> getFoodItemById(Long id) {
        return foodItemRepository.findById(id);
    }

    // Update an existing food item
    public Receipe updateFoodItem(Long id, Receipe foodItem) {
        return foodItemRepository.save(foodItem);
    }

    // Delete a food item by ID
    public void deleteFoodItem(Long id) {
        foodItemRepository.deleteById(id);
    }


    public void reduceIngredientsFromInventorys(Long foodItemId) {
        Receipe foodItem = foodItemRepository.findById(foodItemId).orElse(null);
        if (foodItem == null) {
            // FoodItem not found, handle error
            return;
        }

        List<Ingredient> ingredients = foodItem.getIngredients();
        for (Ingredient ingredient : ingredients) {

        }
    }

    //// this code multiple foodname and chec the invenotry quntity and show error

    public void reduceIngredientsFromInventoryByFoodName(String foodName) {
        Receipe foodItem = foodItemRepository.findByName(foodName);
        if (foodItem == null) {
            throw new RuntimeException("Food item not found.");
        }

        List<Ingredient> ingredients = foodItem.getIngredients();
        for (Ingredient ingredient : ingredients) {

        }
    }

    public void reduceIngredientsFromInventory(List<String> foodItemNames) {
        for (String foodItemName : foodItemNames) {
            Optional<Receipe> optionalFoodItem = Optional.ofNullable(foodItemRepository.findByName(foodItemName));
            if (optionalFoodItem.isPresent()) {
                Receipe foodItem = optionalFoodItem.get();
                reduceIngredientsLogic(foodItem);
                foodItemRepository.save(foodItem);
                //updateInventoryQuantities(foodItem);
            }
        }
    }

    private void reduceIngredientsLogic(Receipe foodItem) {
        List<Ingredient> ingredients = foodItem.getIngredients();

        for (Ingredient ingredient : ingredients) {
            reduceIngredientQuantity(ingredient);
        }
    }

    private void reduceIngredientQuantity(Ingredient ingredient) {
        Float currentQuantity = ingredient.getQuantity();
        Float reductionAmount = calculateReductionAmount(ingredient);

        if (currentQuantity >= reductionAmount) {
            ingredient.setQuantity(currentQuantity - reductionAmount);
        } else {
            throw new InsufficientIngredientException("Insufficient quantity of " + ingredient.getName());
        }
    }

    private Float calculateReductionAmount(Ingredient ingredient) {
        double reductionPercentage = 0.10; // 10%
        Float currentQuantity = ingredient.getQuantity();
        Float reductionAmount = (float) (currentQuantity * reductionPercentage);

        return reductionAmount;
    }

    public class InsufficientIngredientException extends RuntimeException {
        public InsufficientIngredientException(String message) {
            super(message);
        }
    }

}

