package com.syntiaro_pos_system.serviceimpl.v2;

import com.syntiaro_pos_system.entity.v1.Receipe;
import com.syntiaro_pos_system.entity.v2.ApiResponse;
import com.syntiaro_pos_system.repository.v2.ReceipeRepositoryV2;
import com.syntiaro_pos_system.service.v2.ReceipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReceipeServiceImpl implements ReceipeService {

    @Autowired
    ReceipeRepositoryV2 receipeRepository;

    @Override
    public ResponseEntity<ApiResponse> savereceipe(Receipe receipe) {
        try {
                Long lastId = receipeRepository.findMaxIdByStoreId(receipe.getStoreId());
                receipe.setId( (lastId!=null?lastId+1:1));
            return ResponseEntity.ok().body(new ApiResponse(receipeRepository.save(receipe), true, 200));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(null, false, "....", 500));
        }

    }


    @Override
    public ResponseEntity<ApiResponse> getById(Long SerialNo) {
        try {
            Optional<Receipe> existingReceipe = receipeRepository.findById(SerialNo);
            if (existingReceipe.isPresent()) {
                return ResponseEntity.ok().body(new ApiResponse(existingReceipe, true, "Data Found", 200));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(null, true, "Id Not Found", 404));
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(null,false,"....",500));
        }

    }
    @Override
    public ResponseEntity<ApiResponse> getByStoreId(String storeId) {

        try {
            List<Receipe> existingReceipe = receipeRepository.findByStoreid(storeId);
            if (existingReceipe.isEmpty()) {

                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(null, false, "Id Not Found", 404));
            } else {
                return ResponseEntity.ok().body(new ApiResponse(existingReceipe, true, "Data Found", 200));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(null, false, "....", 500));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> updatereceipe(Long serialNo, Receipe updatedReceipe) {
        try {
            Optional<Receipe> existingReceipe = receipeRepository.findById(serialNo);

            if (existingReceipe.isPresent()) {
                Receipe update = existingReceipe.get();
                if (updatedReceipe.getId() != null) {
                    update.setId(updatedReceipe.getId());
                }
                if (updatedReceipe.getName() != null) {
                    update.setName(updatedReceipe.getName());
                }
                if (updatedReceipe.getStoreId() != null) {
                    update.setStoreId(updatedReceipe.getStoreId());
                }
                if (updatedReceipe.getCreatedBy() != null) {
                    update.setCreatedBy(updatedReceipe.getCreatedBy());
                }
                if (updatedReceipe.getUpdatedBy() != null) {
                    update.setUpdatedBy(updatedReceipe.getUpdatedBy());
                }
                return ResponseEntity.ok().body(new ApiResponse(receipeRepository.save(update), true, "Updated Successfully", 200));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(null, true, "Id Not Found", 404));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(null, false, "....", 500));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> deleteItemById(Long serialNo) {
        try {
            Optional<Receipe> existingReceipe = receipeRepository.findById(serialNo);
            if (existingReceipe.isPresent()) {
                receipeRepository.deleteById(serialNo);
                return ResponseEntity.ok().body(new ApiResponse(null, true, "deleted Successfully", 200));
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(null, false, "Id Not Found", 404));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(null, false, "....", 500));
        }
    }

}
