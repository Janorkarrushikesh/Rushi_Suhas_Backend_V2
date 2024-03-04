package com.syntiaro_pos_system.serviceimpl.v2;

import com.syntiaro_pos_system.entity.v1.Store;
import com.syntiaro_pos_system.entity.v1.Tax;
import com.syntiaro_pos_system.entity.v2.ApiResponse;
import com.syntiaro_pos_system.repository.v2.TaxRepository;
import com.syntiaro_pos_system.service.v2.TaxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TaxServiceV2Impl implements TaxService {

    @Autowired
    TaxRepository taxRepository;

    @Override
    public ResponseEntity<ApiResponse> saveTax(Tax tax) {

        try {
            // Optional<Store> store = storeRepository.findById(Long.valueOf(tax.getStoreidfk()));
            Long lastTaxId = taxRepository.findLastNumberForStore(tax.getStoreidfk());
            tax.setTaxId(lastTaxId != null ? lastTaxId + 1 : 1);
            Tax createdTax = taxRepository.save(tax);
            return ResponseEntity.ok().body(new ApiResponse(createdTax, true, 200));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(null, false, ".....", 500));        }
    }

    @Override
    public ResponseEntity<ApiResponse> getTaxById(Long serialNo) {

        try {
            Optional<Tax> existingTax = taxRepository.findById(serialNo);
            if (existingTax.isPresent()) {
                return ResponseEntity.ok().body(new ApiResponse(existingTax, true, 200));
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(null, false, "Id Not Found", 404));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(null, false, "...", 500));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> getTaxByStoreId(String storeid) {
        try {
            Optional<Tax> existingTax = taxRepository.findByStoreIdFk(storeid);
            if (existingTax.isPresent()) {
                return ResponseEntity.ok().body(new ApiResponse(existingTax, true, 200));
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(null, false, "Id Not Found", 404));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(null, false, ".....", 500));
        }
    }
    @Override
    public ResponseEntity<ApiResponse> updateTaxById(Long serialNo, Tax tax) {
        try{
            Optional<Tax> existingTax = taxRepository.findById(serialNo);
            if(existingTax.isPresent()){
                Tax taxToUpdate = existingTax.get();
                if (tax.getRate() != null) {
                    taxToUpdate.setRate(tax.getRate());
                }
                if(tax.getName() != null){
                    taxToUpdate.setName(tax.getName());
                }
                if (tax.getUpdatedBy()!=null)
                {
                    taxToUpdate.setUpdatedBy(tax.getUpdatedBy());
                }
                return ResponseEntity.ok().body(new ApiResponse( taxRepository.save(taxToUpdate),true,"updated Successfully",200));
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(null,false,"Id Not Found",404));
        }catch(Exception e){
            return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(null,false,"...",500));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> deletetaxById(Long serialNo) {
        try{
            Optional<Tax> existingTax= taxRepository.findById(serialNo);
            if(existingTax.isPresent())
            {
                taxRepository.deleteById(serialNo);
                return ResponseEntity.ok().body(new ApiResponse(null,true,"deleted Successfully",200));
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(null,false,"Id Not Found",404));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(null,false,"....",500));
        }
    }

}
