package com.syntiaro_pos_system.serviceimpl.v2;

import com.syntiaro_pos_system.entity.v2.ApiResponse;
import com.syntiaro_pos_system.entity.v1.Receipe;
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
    ReceipeRepositoryV2 receiperepository;
    @Override
    public com.syntiaro_pos_system.entity.v2.ApiResponse savereceipe(Receipe receipe) {

        try{

            return new ApiResponse(receiperepository.save(receipe),true,200);
        }
        catch(Exception e){
            return new ApiResponse(null,false,400);
        }

    }


    @Override
    public ResponseEntity<ApiResponse> getById(Long SerialNo) {
        Optional<Receipe> fdata= receiperepository.findById(SerialNo);
        if(fdata.isPresent()){
            return  ResponseEntity.ok().body(new ApiResponse(fdata,true,"Data Found",200));
        }
        else {
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(fdata,true,"Id Not Found",404));
        }

    }

    @Override
    public ResponseEntity<ApiResponse> getByStoreId(String storeId) {

        try {
            List<Receipe> fData = receiperepository.findByStoreid(storeId);
            if(fData.isEmpty())
            {

                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(fData,true,"Id Not Found",404));
            }
            else {
                return ResponseEntity.ok().body(new ApiResponse(fData, true, "Data Found", 200));
            }
        }catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(null,false,"....",500));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> updatereceipe(Long serialNo, Receipe updatedreceipe) {
        try {
            Optional<Receipe> existingreceipe = receiperepository.findById(serialNo);

            if (existingreceipe.isPresent()) {
                Receipe update = existingreceipe.get();
                if(updatedreceipe.getId()!=null){
                    update.setId(updatedreceipe.getId());
                }
                if (updatedreceipe.getName()!=null){
                    update.setName(updatedreceipe.getName());
                }
                if(updatedreceipe.getStoreId()!=null)
                {
                    update.setStoreId(updatedreceipe.getStoreId());
                }
                if(updatedreceipe.getCreatedBy()!=null)
                {
                    update.setCreatedBy(updatedreceipe.getCreatedBy());
                }
                if(updatedreceipe.getUpdatedBy()!=null){
                    update.setUpdatedBy(updatedreceipe.getUpdatedBy());
                }
                return ResponseEntity.ok().body(new ApiResponse(receiperepository.save(update), true, "Updated Successfully", 200));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(null, true, "Id Not Found", 404));
            }
        }catch(Exception e)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(null,false,"....",500));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> deleteItemById(Long serialNo) {
        try{
            Optional<Receipe> data= receiperepository.findById(serialNo);
            if(data.isPresent())
            {
                receiperepository.deleteById(serialNo);
                return ResponseEntity.ok().body(new ApiResponse(null,true,"deleted Successfully",200));
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(null,false,"Id Not Found",404));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(null,false,"....",500));
        }
    }
    
}
