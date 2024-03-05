package com.syntiaro_pos_system.serviceimpl.v1;

import com.syntiaro_pos_system.entity.v1.Ingredient;
import com.syntiaro_pos_system.entity.v1.Receipe;
import com.syntiaro_pos_system.repository.v1.InventoryRepo;
import com.syntiaro_pos_system.repository.v1.ReceipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReciepeService {

    @Autowired
    InventoryRepo inventoryRepository;
    private final ReceipeRepository receipeRepository;

    @Autowired
    public ReciepeService(ReceipeRepository receipeRepository) {
        this.receipeRepository = receipeRepository;
    }

    // Create a new food item
    public Receipe createFoodItem(Receipe receipe) {


        Long lastBillNumber = receipeRepository.findLastNumberForStore(receipe.getStoreId());
        receipe.setId(lastBillNumber != null ? lastBillNumber + 1 : 1);


        return receipeRepository.save(receipe);
    }

    // Read all food items
    public List<Receipe> getAllFoodItems() {
        return receipeRepository.findAll();
    }

    // Read a single food item by ID
    public Optional<Receipe> getFoodItemById(Long id) {
        return receipeRepository.findById(id);
    }

    // Update an existing food item
    public Receipe updateFoodItem(Long id, Receipe receipe) {
        return receipeRepository.save(receipe);
    }

    // Delete a food item by ID
    public void deleteFoodItem(Long id) {
        receipeRepository.deleteById(id);
    }


    public void reduceIngredientsFromInventorys(Long foodItemId) {
        Receipe receipe = receipeRepository.findById(foodItemId).orElse(null);
        if (receipe == null) {
            // FoodItem not found, handle error
            return;
        }

        List<Ingredient> ingredients = receipe.getIngredients();
        for (Ingredient ingredient : ingredients) {

        }
    }

    //// this code multiple foodname and chec the invenotry quntity and show error

    public void reduceIngredientsFromInventoryByFoodName(String foodName) {
        Receipe receipe = receipeRepository.findByName(foodName);
        if (receipe == null) {
            throw new RuntimeException("Food item not found.");
        }

        List<Ingredient> ingredients = receipe.getIngredients();
        for (Ingredient ingredient : ingredients) {

        }
    }

    public void reduceIngredientsFromInventory(List<String> foodItemNames) {
        for (String foodItemName : foodItemNames) {
            Optional<Receipe> optionalFoodItem = Optional.ofNullable(receipeRepository.findByName(foodItemName));
            if (optionalFoodItem.isPresent()) {
                Receipe receipe = optionalFoodItem.get();
                reduceIngredientsLogic(receipe);
                receipeRepository.save(receipe);
                //updateInventoryQuantities(foodItem);
            }
        }
    }

    private void reduceIngredientsLogic(Receipe receipe) {
        List<Ingredient> ingredients = receipe.getIngredients();

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

