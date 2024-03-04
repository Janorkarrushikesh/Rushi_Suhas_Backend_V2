package com.syntiaro_pos_system.serviceimpl.v2;

import com.syntiaro_pos_system.entity.v2.ApiResponse;
import com.syntiaro_pos_system.entity.v1.Invoice;
import com.syntiaro_pos_system.repository.v2.InvoiceRepository;
import com.syntiaro_pos_system.service.v2.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class InvoiceServiceImpl implements InvoiceService {
    @Autowired
    InvoiceRepository invoiceRepository;

    @Override
    public ResponseEntity<ApiResponse> saveInvoice(Invoice invoice) {
        try{
            Integer lastBillNumber = invoiceRepository.findLastNumberForStore(invoice.getStoreId());
            invoice.setInvoiceId(lastBillNumber != null ? lastBillNumber + 1 : 1);
            return ResponseEntity.ok().body(new ApiResponse(invoiceRepository.save(invoice), true, "Added Successfully", 200));
        }
        catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(null, false, "Failed to Add Invoice ", HttpStatus.INTERNAL_SERVER_ERROR.value()));        }
    }
    @Override
    public ApiResponse getInvoiceById(Integer SerialNo) {
        Optional <Invoice> invoiceData= invoiceRepository.findById(SerialNo);
        if (invoiceData.isPresent()){
            return  new ApiResponse(invoiceData ,true,200);
        }
        return new ApiResponse(null, false,  "Id not found",400);
    }

    @Override
    public ApiResponse getInvoiceByStoreId(Integer storeId) {
        Optional<Invoice> invoice = invoiceRepository.findInvoiceByStoreId(storeId);
        if (invoice.isPresent())
        {
            return new ApiResponse(invoice,true,200);
        }
        return new ApiResponse(null,false,"Id Not Found",400);
    }

    @Override
    public ResponseEntity<ApiResponse> updateInvoice(Integer SerialNo, Invoice invoice) {

        try{
            Optional<Invoice> existingInvoice = invoiceRepository.findById((SerialNo));
            if (existingInvoice.isPresent()) {
                Invoice updateinvoice = existingInvoice.get();
                if (invoice.getStoreId() != null) {
                    updateinvoice.setStoreId(invoice.getStoreId());
                }
                if (invoice.getVendorName() != null) {
                    updateinvoice.setVendorName(invoice.getVendorName());
                }
                if (invoice.getItemName() != null) {
                    updateinvoice.setItemName(invoice.getItemName());
                }
                if (invoice.getInvoiceDate() != null) {
                    updateinvoice.setInvoiceDate(invoice.getInvoiceDate());
                }
                if (invoice.getPrice() != null) {
                    updateinvoice.setPrice(invoice.getPrice());
                }
                if (invoice.getQuantity() != null) {
                    updateinvoice.setQuantity(invoice.getQuantity());
                }
                if (invoice.getDiscount() != null) {
                    updateinvoice.setDiscount(invoice.getDiscount());
                }
                if (invoice.getPaymentStatus() != null) {
                    updateinvoice.setPaymentStatus(invoice.getPaymentStatus());
                }
                if (invoice.getTotal() != null) {
                    updateinvoice.setTotal(invoice.getTotal());
                }
                if (invoice.getGstNo() != null) {
                    updateinvoice.setGstNo(invoice.getGstNo());
                }
                if (invoice.getCreateDate() != null) {
                    updateinvoice.setCreatedBy(invoice.getCreatedBy());
                }
                if (invoice.getUpdatedBy() != null) {
                    updateinvoice.setUpdatedBy(invoice.getUpdatedBy());
                }
                if (invoice.getInventoryCode() != null) {
                    updateinvoice.setInventoryCode(invoice.getInventoryCode());
                }

                return ResponseEntity.ok().body(new ApiResponse(  invoiceRepository.save(updateinvoice), true, "Updated Successfully", 200));
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(null, false, "Id Not Found", 404));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(null,false,"....",500));
        }

    }

    @Override
    public ResponseEntity<ApiResponse> deleteInvoiceByid(Integer serialNo) {
        try{
            Optional<Invoice> data= invoiceRepository.findById(serialNo);
            if(data.isPresent())
            {
                invoiceRepository.deleteById(serialNo);
                return ResponseEntity.ok().body(new ApiResponse(null,true,"deleted Successfully",200));
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(null,false,"Id Not Found",404));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(null,false,"....",500));
        }
    }


}
