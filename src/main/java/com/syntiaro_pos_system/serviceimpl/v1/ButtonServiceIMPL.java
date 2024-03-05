package com.syntiaro_pos_system.serviceimpl.v1;

import com.syntiaro_pos_system.entity.v1.CategoryButton;
import com.syntiaro_pos_system.repository.v1.ButtonRepo;
import com.syntiaro_pos_system.service.v1.ButtonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class ButtonServiceIMPL implements ButtonService {

    @Autowired
    ButtonRepo buttonRepo;

    @Override
    public CategoryButton updateButton(Long id, CategoryButton button) {
        Optional<CategoryButton> existingButton = buttonRepo.findById((long) Integer.parseInt(String.valueOf(id)));
        if (existingButton.isPresent()) {
            CategoryButton updatedButton = existingButton.get();
            // Update the properties of the updatedInventory
            if (button.getName() != null) {
                updatedButton.setName(button.getName());
            }

            buttonRepo.save(updatedButton);
            return updatedButton;
        } else {
            return null;
        }
    }


}
