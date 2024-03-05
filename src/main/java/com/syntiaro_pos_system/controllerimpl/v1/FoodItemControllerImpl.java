package com.syntiaro_pos_system.controllerimpl.v1;

import com.syntiaro_pos_system.controller.v1.FoodItemController;
import com.syntiaro_pos_system.entity.v1.Receipe;
import com.syntiaro_pos_system.repository.v1.ReceipeRepository;
import com.syntiaro_pos_system.serviceimpl.v1.ReciepeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class FoodItemControllerImpl implements FoodItemController {

    private final ReciepeService reciepeService;
    @Autowired
    private ReceipeRepository receipeRepository;

    @Autowired
    public FoodItemControllerImpl(ReciepeService reciepeService) {
        this.reciepeService = reciepeService;
    }

    // POST endpoint for creating a new food item
    @Override
    public ResponseEntity<?> createFoodItem(@RequestBody Receipe receipe) {
        // Check if the food name is available in the specified store ID
        boolean foodNameExists = receipeRepository.checkFoodNameExists(receipe.getStoreId(), receipe.getName());

        if (foodNameExists) {
            return new ResponseEntity<>("Food name already exists in the specified store", HttpStatus.BAD_REQUEST);
        }
        Long lastBillNumber = receipeRepository.findLastNumberForStore(receipe.getStoreId());
        receipe.setId(lastBillNumber != null ? lastBillNumber + 1 : 1);

        Receipe createdReceipe = receipeRepository.save(receipe);
        return new ResponseEntity<>(createdReceipe, HttpStatus.CREATED);
    }

    @Override
    public List<Receipe> getallfood() {
        return reciepeService.getAllFoodItems();
    }

    // {--------FOR GET FOOD ITEM BY ID -------}
    @Override // WRITE BY RUSHIKESH
    public ResponseEntity<?> getFoodById(@PathVariable Long Ser_no) {
        Optional<Receipe> foodItem = reciepeService.getFoodItemById(Ser_no);
        if (foodItem != null) {
            return new ResponseEntity<>(foodItem, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public List<Receipe> getFoodItemsByStoreId(@PathVariable String storeid) {
        return receipeRepository.getFoodItemsByStoreId(storeid);
    }

    @Override
    public ResponseEntity<String> deleteFoodItemById(@PathVariable Long Ser_no) {
        if (receipeRepository.existsById(Ser_no)) {
            receipeRepository.deleteById(Ser_no);
            return ResponseEntity.ok("Food item deleted successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // ----for fooditem update
    @Override // MADE BY RUSHIKESH
    public ResponseEntity<?> updateFoodItem(@PathVariable Long Ser_no, @RequestBody Receipe updatedReceipe) {
        Optional<Receipe> existingFoodItem = reciepeService.getFoodItemById(Ser_no);
        if (!existingFoodItem.isPresent()) {
            return new ResponseEntity<>("Food item not found", HttpStatus.NOT_FOUND);
        }
        Receipe updatedItem = reciepeService.updateFoodItem(Ser_no, updatedReceipe);
        return new ResponseEntity<>(updatedItem, HttpStatus.OK);
    }

}
