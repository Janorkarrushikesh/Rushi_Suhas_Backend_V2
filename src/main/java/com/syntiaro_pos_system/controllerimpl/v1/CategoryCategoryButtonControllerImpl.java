package com.syntiaro_pos_system.controllerimpl.v1;

import com.syntiaro_pos_system.controller.v1.CategoryButtonController;
import com.syntiaro_pos_system.entity.v1.CategoryButton;
import com.syntiaro_pos_system.repository.v1.CategoryBtnRepo;
import com.syntiaro_pos_system.service.v1.ButtonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class CategoryCategoryButtonControllerImpl implements CategoryButtonController {


    @Autowired
    CategoryBtnRepo categoryBtnRepo;


    @Autowired
    ButtonService buttonService;

    @Override
    public ResponseEntity<List<CategoryButton>> getFoodsByStoreId(@PathVariable String store_id) {
        List<CategoryButton> categoryButtons = categoryBtnRepo.findbyStoerid(store_id);
        return new ResponseEntity<>(categoryButtons, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> createEntity(@RequestBody CategoryButton categoryButton) {
        // Check if a button with the same id and butname exists
        CategoryButton existingCategoryButton = categoryBtnRepo.findButtonByIdAndButname(categoryButton.getStoreId(), categoryButton.getName());

        if (existingCategoryButton != null) {
            // If a button with the same id and butname exists, return an error response
            return ResponseEntity.badRequest().body("A button with the same id and butname already exists.");
        }

        // If no button with the same id and butname exists, save the entity to the database
        CategoryButton savedCategoryButton = categoryBtnRepo.save(categoryButton);

        return ResponseEntity.ok(savedCategoryButton);
    }


   @Override
    public ResponseEntity<CategoryButton> updateButton(@PathVariable("id") Long id, @RequestBody CategoryButton categoryButton) {
        try {
            CategoryButton updateCategoryButton = buttonService.updateButton(Long.valueOf(id), categoryButton);
            if (updateCategoryButton != null) {
                return new ResponseEntity<>(updateCategoryButton, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public CategoryButton fetchDetailsById(@PathVariable Long id) {
        if (id != null) {
            return categoryBtnRepo.findById(id).orElse(null);
        } else {
            // Handle the case where id is null, e.g., by returning an appropriate response
            return null; // or return a custom response
        }
    }



    @Override
    public ResponseEntity<HttpStatus> deleteButton(@PathVariable Long id) {
        try {
            if (id != null) {
                categoryBtnRepo.deleteById(id);
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                // Handle the case where id is null, e.g., by returning an appropriate response
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // or return a custom response
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



}
