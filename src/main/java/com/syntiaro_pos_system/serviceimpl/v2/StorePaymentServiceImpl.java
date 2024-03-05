package com.syntiaro_pos_system.serviceimpl.v2;

import com.syntiaro_pos_system.entity.v1.StorePayment;
import com.syntiaro_pos_system.entity.v2.ApiResponse;
import com.syntiaro_pos_system.repository.v2.StorePaymentV2Repository;
import com.syntiaro_pos_system.service.v2.StorePaymentService;
import net.glxn.qrgen.QRCode;
import net.glxn.qrgen.image.ImageType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class StorePaymentServiceImpl implements StorePaymentService {

    @Autowired
    StorePaymentV2Repository storePaymentV2Repository;

    @Override
    public ResponseEntity<ApiResponse> saveStorePayment(StorePayment storePayment) {

        try {
            boolean accountNoExists = storePaymentV2Repository.existsByAccountNoAndStoreId(storePayment.getAccountNo(), storePayment.getStoreId());
            boolean upiIdExists = storePaymentV2Repository.existsByUpiIdAndStoreId(storePayment.getUpiId(), storePayment.getStoreId());

            if (accountNoExists || upiIdExists) {
                // Return an error response indicating that the account number or UPI ID already exists
                String errorMessage = accountNoExists ? "Account number already exists for this store" : "UPI ID already exists for this store";
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(null, false, errorMessage, 409));
            } else {
                storePayment.setCreatedDate(String.valueOf(new Date()));
                // Get the last paymentId for the store
                Long lastPaymentId = storePaymentV2Repository.findMaxPaymentIdByStoreId(storePayment.getStoreId());
                // Generate the new paymentId
                storePayment.setPaymentId((lastPaymentId != null) ? lastPaymentId + 1 : 1);

                StorePayment createdStorePayment = storePaymentV2Repository.save(storePayment);
                return ResponseEntity.ok().body(new ApiResponse(createdStorePayment, true, "Created Successfully", 200));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(null, false, ".....", 500));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> getById(Long serialNo) {
        try {
            Optional<StorePayment> existingStorePayment = storePaymentV2Repository.findById(serialNo);
            if (existingStorePayment.isPresent()) {
                return ResponseEntity.ok().body(new ApiResponse(existingStorePayment, true, "Data Found", 200));
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(null, false, "Id Not Found", 404));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(null, false, "....", 500));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> getByStoreId(Long storeId) {
        try {
            List<StorePayment> existingStorePayment = storePaymentV2Repository.findByStoreId(storeId);
            if (!existingStorePayment.isEmpty()) {
                return ResponseEntity.ok().body(new ApiResponse(existingStorePayment, true, "Data Found", 200));
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(null, false, "Id Not Found", 404));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(null, false, "....", 500));
        }
    }

    @Override
    public ResponseEntity<byte[]> generateQRCode(String text, HttpServletResponse response) {
        try {
            byte[] qrCodeBytes = QRCode.from(text).to(ImageType.PNG).stream().toByteArray();
            response.setContentType("image/png");
            response.setContentLength(qrCodeBytes.length);
            try (OutputStream outputStream = response.getOutputStream()) {
                outputStream.write(qrCodeBytes);
                outputStream.flush();
            }
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @Override
    public ResponseEntity<ApiResponse> updateStorePayment(Long serialNo, StorePayment payment) {
        StorePayment storePayment = storePaymentV2Repository.findById(serialNo)
                .orElseThrow(() -> new EntityNotFoundException("Store Payment not found"));
        try {
            if (!payment.getAccountNo().equals(storePayment.getAccountNo()) && storePaymentV2Repository.existsByAccountNo(payment.getAccountNo())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse(null, false, "Error: Account number is already in use!", 400));
            }

            if (!payment.getUpiId().equals(storePayment.getUpiId()) && storePaymentV2Repository.existsByUpiId(payment.getUpiId())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse(null, false, "Error: UPI ID is already in use!", 400));
            }
            // Update all fields from the updatedPayment object
            storePayment.setStoreName(payment.getStoreName());
            storePayment.setAccountNo(payment.getAccountNo());
            storePayment.setUpiId(payment.getUpiId());
            storePayment.setBankName(payment.getBankName());
            storePayment.setBranchName(payment.getBranchName());
            storePayment.setIfscCode(payment.getIfscCode());
            storePayment.setStoreId(payment.getStoreId());
            storePayment.setCreatedBy(payment.getCreatedBy());
            storePayment.setCreatedDate(payment.getCreatedDate());
            storePayment.setUpdatedBy(payment.getUpdatedBy());
            storePayment.setUpdatedDate(payment.getUpdatedDate());
            storePaymentV2Repository.save(storePayment);

            return ResponseEntity.ok().body(new ApiResponse(storePaymentV2Repository.save(storePayment), true, "Store Payment updated successfully.", 200));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(null, false, "....", 500));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> deleteById(Long SerialNo) {
        try {
            Optional<StorePayment> existingStorePayment = storePaymentV2Repository.findById(SerialNo);
            if (existingStorePayment.isPresent()) {
                storePaymentV2Repository.deleteById(SerialNo);
                return ResponseEntity.ok().body(new ApiResponse(null, false, "deleted Successfully", 200));
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(null, false, "Id not found", 404));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(null, false, "..", 500));
        }
    }
}
