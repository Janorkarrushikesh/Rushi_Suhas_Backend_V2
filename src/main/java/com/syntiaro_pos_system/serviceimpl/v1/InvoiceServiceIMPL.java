package com.syntiaro_pos_system.serviceimpl.v1;


import com.syntiaro_pos_system.entity.v1.VendorInventory;
import com.syntiaro_pos_system.repository.v1.InvoiceRepo;
import com.syntiaro_pos_system.service.v1.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class InvoiceServiceIMPL implements InvoiceService {


    @Autowired
    InvoiceRepo invoiceRepo;


    //THIS METHOD IS USE FOR ADD INVOICE
    @Override
    public String addInvoice(VendorInventory vendorInventory) {
        Integer lastBillNumber = invoiceRepo.findLastNumberForStore(vendorInventory.getStoreId());
        vendorInventory.setInvoiceId(lastBillNumber != null ? lastBillNumber + 1 : 1);
        invoiceRepo.save(vendorInventory);
        return vendorInventory.getItemName();
    }


    //THIS METHOD IS USE FOR GET ALL LIST OF INVOICE
    @Override
    public List<VendorInventory> getInvoice() {
        return this.invoiceRepo.findAll();
    }


    //THIS METHOD IS USE FOR UPDATE INVOICE
    @Override
    public VendorInventory updateInvoice(VendorInventory vendorInventory) {
        invoiceRepo.save(vendorInventory);
        return vendorInventory;
    }


    // THIS METHOD IS USE FOR FETCH INVOICE BY STOREID
    @Override
    public List<VendorInventory> fetchInvoicesByStoreId(Integer storeId) {
        return invoiceRepo.findByStore_id(storeId);
    }

    @Override
    public void deleteInvoiceById(Integer invoiceId) {
        Optional<VendorInventory> invoiceOptional = invoiceRepo.findById(invoiceId);
        if (invoiceOptional.isPresent()) {
            invoiceRepo.deleteById(invoiceId);
        } else {
            throw new EntityNotFoundException("Invoice with ID " + invoiceId + " not found");
        }
    }

    // THIS METHOD IS USE FOR UPDATE INVOICE
    @Override
    public VendorInventory updateInvoice(Integer invoice_id, VendorInventory vendorInventory) {
        Optional<VendorInventory> existingInvoice = invoiceRepo.findById(Integer.parseInt(String.valueOf((invoice_id))));
        if (existingInvoice.isPresent()) {
            VendorInventory updateinvoice = existingInvoice.get();
            if (vendorInventory.getStoreId() != null) {
                updateinvoice.setStoreId(vendorInventory.getStoreId());
            }
            if (vendorInventory.getVendorName() != null) {
                updateinvoice.setVendorName(vendorInventory.getVendorName());
            }
            if (vendorInventory.getItemName() != null) {
                updateinvoice.setItemName(vendorInventory.getItemName());
            }
            if (vendorInventory.getInvoiceDate() != null) {
                updateinvoice.setInvoiceDate(vendorInventory.getInvoiceDate());
            }
            if (vendorInventory.getPrice() != null) {
                updateinvoice.setPrice(vendorInventory.getPrice());
            }
            if (vendorInventory.getQuantity() != null) {
                updateinvoice.setQuantity(vendorInventory.getQuantity());
            }
            if (vendorInventory.getDiscount() != null) {
                updateinvoice.setDiscount(vendorInventory.getDiscount());
            }
            if (vendorInventory.getPaymentStatus() != null) {
                updateinvoice.setPaymentStatus(vendorInventory.getPaymentStatus());
            }
            if (vendorInventory.getTotal() != null) {
                updateinvoice.setTotal(vendorInventory.getTotal());
            }
            if (vendorInventory.getGstNo() != null) {
                updateinvoice.setGstNo(vendorInventory.getGstNo());
            }
            if (vendorInventory.getCreateDate() != null) {
                updateinvoice.setCreatedBy(vendorInventory.getCreatedBy());
            }
            if (vendorInventory.getUpdatedBy() != null) {
                updateinvoice.setUpdatedBy(vendorInventory.getUpdatedBy());
            }
            if (vendorInventory.getInventoryCode() != null) {
                updateinvoice.setInventoryCode(vendorInventory.getInventoryCode());
            }
            invoiceRepo.save(updateinvoice);
            return updateinvoice;
        } else {
            return null;
        }
    }
}