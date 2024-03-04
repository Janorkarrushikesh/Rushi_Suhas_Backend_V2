package com.syntiaro_pos_system.controllerimpl.v1;

import com.syntiaro_pos_system.controller.v1.StorePaymentController;
import com.syntiaro_pos_system.entity.v1.StorePayment;
import com.syntiaro_pos_system.repository.v1.StorePaymentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.glxn.qrgen.QRCode;
import net.glxn.qrgen.image.ImageType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletResponse;

@RestController
public class StorePaymentControllerImpl implements StorePaymentController {

    @Autowired
    private StorePaymentRepository storePaymentRepository;

    @Autowired
    private ObjectMapper objectMapper;


    // THIS METHOD IS USE FOR GETALL STOREPAYMENT
    @Override
    public ResponseEntity<List<StorePayment>> getAllStorePayments() {
        List<StorePayment> storePayments = storePaymentRepository.findAll();
        return ResponseEntity.ok(storePayments);
    }

    //////////// rushikesh add this new code///

    // for upi payment code //
    @Override
    public ResponseEntity<String> getUpiByStoreId(@PathVariable Long storeId) {
        List<StorePayment> storePayments = storePaymentRepository.findByStoreId(storeId);
        if (!storePayments.isEmpty()) {
            // Assuming you want to return the UPI ID for the first store payment in the
            // list.
            String upiId = storePayments.get(0).getUpiId();
            return ResponseEntity.ok(upiId);
        } else {
            // Handle the case where the store payment for the given store ID is not found.
            return ResponseEntity.notFound().build();
        }
    }

    // THIS METHOD IS USE FOR GETBYID STOREPAYMENT
    @Override
    public ResponseEntity<StorePayment> getStorePaymentById(@PathVariable Long paymentId) {
        StorePayment storePayment = storePaymentRepository.findById(paymentId).orElse(null);
        if (storePayment != null) {
            return ResponseEntity.ok(storePayment);
        } else {
            return ResponseEntity.notFound().build();
        }
    }



    // THIS METHOD IS USE FOR DELETE STOREPAYMENT
    @Override
    public ResponseEntity<String> deleteStorePayment(@PathVariable Long paymentId) {
        storePaymentRepository.deleteById(paymentId);
        return ResponseEntity.ok("Store Payment deleted successfully.");
    }

    // THIS METHOD IS USE FOR GET STOREPAYMENTS BY STORE ID
    @Override
    public ResponseEntity<List<StorePayment>> getStorePaymentsByStoreId(@PathVariable Long storeId) {
        List<StorePayment> storePayments = storePaymentRepository.findByStoreId(storeId);

        if (!storePayments.isEmpty()) {
            return ResponseEntity.ok(storePayments);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // ------------------------- THIS COD FOR GENRATE OR CODE-------------------
    @Override
    public void generateQRCode(@RequestParam String text, HttpServletResponse response) throws IOException {
        // Generate the QR code as a byte array
        byte[] qrCodeBytes = QRCode.from(text).to(ImageType.PNG).stream().toByteArray();

        // Set the response headers
        response.setContentType("image/png");
        response.setContentLength(qrCodeBytes.length);
        // Write the QR code image to the response output stream
        response.getOutputStream().write(qrCodeBytes);
        response.getOutputStream().flush();
    }


    //Logic Changed beacuse it was taking repeted  account number/ upi id while adding
    @Override
    public ResponseEntity<?> createStorePayment(@RequestBody StorePayment storePayment) {
        // Check if the account number or UPI ID already exists for the same store ID
        boolean accountNoExists = storePaymentRepository.existsByAccountNoAndStoreId(storePayment.getAccountNo(), storePayment.getStoreId());
        boolean upiIdExists = storePaymentRepository.existsByUpiIdAndStoreId(storePayment.getUpiId(), storePayment.getStoreId());

        if (accountNoExists || upiIdExists) {
            // Return an error response indicating that the account number or UPI ID already exists
            String errorMessage = accountNoExists ? "Account number already exists for this store" : "UPI ID already exists for this store";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        }

        // Set the createdDate using LocalDateTime
        storePayment.setCreatedDate(String.valueOf(new Date()));

        try {
            // Get the last paymentId for the store
            Long lastPaymentId = storePaymentRepository.findMaxPaymentIdByStoreId(storePayment.getStoreId());
            // Generate the new paymentId
            storePayment.setPaymentId((lastPaymentId != null) ? lastPaymentId + 1 : 1);
            // Save the StorePayment
            StorePayment createdStorePayment = storePaymentRepository.save(storePayment);
            return ResponseEntity.ok(createdStorePayment);
        } catch (Exception e) {
            // Handle exceptions (e.g., database errors)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    // THIS METHOD IS USE FOR UPDATE STOREPAYMENT
    // Logic Changed beacuse it was taking repeted  account number/ upi id while updating
    /*------changes made by trupti---------------*/
    @Override
    public ResponseEntity<String> updateStorePayment(@PathVariable Long paymentId,
                                                     @RequestBody StorePayment updatedPayment) {
        StorePayment storePayment = storePaymentRepository.findById(paymentId)
                .orElseThrow(() -> new EntityNotFoundException("Store Payment not found"));

        try {
            // Assuming you have a validation method or check for unique constraints
            // For example, checking if the updated account number or UPI ID already exists
            if (!updatedPayment.getAccountNo().equals(storePayment.getAccountNo()) && storePaymentRepository.existsByAccountNo(updatedPayment.getAccountNo())) {
                return ResponseEntity
                        .badRequest()
                        .body("Error: Account number is already in use!");
            }

            if (!updatedPayment.getUpiId().equals(storePayment.getUpiId()) && storePaymentRepository.existsByUpiId(updatedPayment.getUpiId())) {
                return ResponseEntity
                        .badRequest()
                        .body("Error: UPI ID is already in use!");
            }

            // Update all fields from the updatedPayment object
            storePayment.setStoreName(updatedPayment.getStoreName());
            storePayment.setAccountNo(updatedPayment.getAccountNo());
            storePayment.setUpiId(updatedPayment.getUpiId());
            storePayment.setBankName(updatedPayment.getBankName());
            storePayment.setBranchName(updatedPayment.getBranchName());
            storePayment.setIfscCode(updatedPayment.getIfscCode());
            storePayment.setStoreId(updatedPayment.getStoreId());
            storePayment.setCreatedBy(updatedPayment.getCreatedBy());
            storePayment.setCreatedDate(updatedPayment.getCreatedDate());
            storePayment.setUpdatedBy(updatedPayment.getUpdatedBy());
            storePayment.setUpdatedDate(updatedPayment.getUpdatedDate());

            storePaymentRepository.save(storePayment);

            return ResponseEntity.ok("Store Payment updated successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating Store Payment.");
        }
    }


}
