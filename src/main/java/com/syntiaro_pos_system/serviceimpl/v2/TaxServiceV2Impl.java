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

import java.util.List;
import java.util.Optional;

@Service
public class TaxServiceV2Impl implements TaxService {

    @Autowired
    TaxRepository taxRepository;

    @Override
    public ApiResponse saveTax(Tax tax) {

        try {
            // Optional<Store> store = storeRepository.findById(Long.valueOf(tax.getStoreidfk()));
            Long lastBillNumber = taxRepository.findLastNumberForStore(tax.getStoreidfk());
            tax.setTaxId(lastBillNumber != null ? lastBillNumber + 1 : 1);
            Tax createdTax = taxRepository.save(tax);
            return new ApiResponse(createdTax, true, 200);
        } catch (Exception e) {
            return new ApiResponse(null, false, "Store Not Found ", 400);
        }
    }

    @Override
    public ResponseEntity<ApiResponse> getTaxById(Long serialNo) {

        try {
            Optional<Tax> taxData = taxRepository.findById(serialNo);
            if (taxData.isPresent()) {
                return ResponseEntity.ok().body(new ApiResponse(taxData, true, 200));
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(null, false, "Id Not Found", 404));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(null, false, ".....", 500));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> getTaxByStoreId(String storeid) {
        try {
            List<Tax> taxData = taxRepository.findByStoreIdFk(storeid);
            if (taxData.isEmpty()) {
                return ResponseEntity.ok().body(new ApiResponse(null, false, "Id Not Found", 404));
            }
            return ResponseEntity.ok().body(new ApiResponse(taxData, true, 200));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(null, false, ".....", 500));
        }
    }


    @Override
    public ResponseEntity<ApiResponse> updateTaxById(Long serialNo, Tax tax) {

        try{
            Optional<Tax> existing = taxRepository.findById(serialNo);
            if(existing.isPresent()){
                Tax taxToUpdate = existing.get();
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
            Optional<Tax> data= taxRepository.findById(serialNo);
            if(data.isPresent())
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
