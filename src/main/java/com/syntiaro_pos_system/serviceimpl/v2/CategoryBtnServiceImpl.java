package com.syntiaro_pos_system.serviceimpl.v2;

import com.syntiaro_pos_system.entity.v1.CategoryButton;
import com.syntiaro_pos_system.entity.v2.ApiResponse;
import com.syntiaro_pos_system.repository.v2.CategoryBtnRepository;
import com.syntiaro_pos_system.service.v2.CategoryBtnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CategoryBtnServiceImpl implements CategoryBtnService {

    @Autowired
    CategoryBtnRepository categoryBtnRepository;


    @Override
    public ResponseEntity<ApiResponse> getButtonByStoreId(String storeId) {
        try {
            List<CategoryButton> buttonsList = categoryBtnRepository.findByStoreId(storeId);
            List<Map<String, Object>> buttonData = new ArrayList<>();
            if (buttonsList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse(null, false,"Id not found !",404));
            } else {
                Map<String, Object> buttonMap = new LinkedHashMap<>();
                for (CategoryButton categoryButton : buttonsList) {
                    buttonMap.put("id", categoryButton.getId());
                    buttonMap.put("name", categoryButton.getName());
                    buttonMap.put("storeId", categoryButton.getStoreId());
                }
                buttonData.add(buttonMap);
                return ResponseEntity.ok(new ApiResponse(buttonData, true, 200));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(null, false,"...", 500));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> getButtonById(Long id) {
        try {
            Optional<CategoryButton> existingButton = categoryBtnRepository.findById(id);
            if (existingButton.isPresent()) {
                return ResponseEntity.ok(new ApiResponse(existingButton, true, 200));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse(null, false,"Id Not Found !",404));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(null, false, "...",500));
        }
    }

    @Override
    public ResponseEntity<ApiResponse>saveButton(CategoryButton button) {
        try{
            CategoryButton existingButton = categoryBtnRepository.findButtonByIdAndButname(button.getStoreId(), button.getName());
            if (existingButton != null) {
                return ResponseEntity.status(HttpStatus.ALREADY_REPORTED)
                        .body(new ApiResponse(null,false,"A button with the same id and butname already exists.",208));
            }
            return ResponseEntity.ok().body(new ApiResponse(categoryBtnRepository.save(button),true,"Added Successfully",200));
        }catch (Exception e){

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(null, false,"...",500));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> updatecategoryById(Long id, CategoryButton categoryButton) {
        try{
            Optional<CategoryButton> existingButton = categoryBtnRepository.findById(id);
            if (existingButton.isPresent()) {
                CategoryButton updatedButton = existingButton.get();
                if (categoryButton.getName() != null) {
                    updatedButton.setName(categoryButton.getName());
                }
                if(categoryButton.getUpdateBy()!=null) {
                    updatedButton.setUpdateBy(categoryButton.getUpdateBy());
                }
                if (categoryButton.getStoreId()!=null){
                    updatedButton.setStoreId(categoryButton.getStoreId());
                }
                return ResponseEntity.ok().body(new ApiResponse( categoryBtnRepository.save(updatedButton),true,"Updated Successfully",200));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(null,false,"Id Not Found",404));
            }
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse( null,false,"...",500));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> deleteById(Long id) {
        try{
            Optional<CategoryButton> existingButton= categoryBtnRepository.findById(id);
            if(existingButton.isPresent())
            {
                categoryBtnRepository.deleteById(id);
                return ResponseEntity.ok().body(new ApiResponse(null,true,"deleted Successfully",200));
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(null,false,"Id Not Found",404));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(null,false,"....",500));
        }
    }

}