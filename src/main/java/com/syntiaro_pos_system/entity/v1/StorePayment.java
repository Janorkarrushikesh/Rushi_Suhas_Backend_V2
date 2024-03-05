package com.syntiaro_pos_system.entity.v1;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@Entity
@Table(name = "store_payment")
@AllArgsConstructor
@NoArgsConstructor
public class StorePayment {


    // ----------------------RUSHIKESH ADDED THIS NEW CODE----------------------
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long SerialNo;


    @Column(name = "payment_id")
    private Long paymentId;

    @Column(name = "store_name")
    private String storeName;

    @Column(name = "account_no")
    private String accountNo;

    @Column(name = "upi_id")
    private String upiId;

    @Column(name = "bank_name")
    private String bankName;

    @Column(name = "branch_name")
    private String branchName;

    @Column(name = "ifsc_code")
    private String ifscCode;
    @Column(name = "store_id")
    private Long storeId;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_date")
    private String createdDate;

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "updated_date")
    private String updatedDate;
    private String storeidfk;


    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getUpiId() {
        return upiId;
    }

//    public void setUpiId(String upiId) {
//        this.upiId ="upi://pay?pa="+upiId;
//    }

    public void setUpiId(String upiId) {
        this.upiId = upiId;
    }


    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getIfscCode() {
        return ifscCode;
    }

    public void setIfscCode(String ifscCode) {
        this.ifscCode = ifscCode;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }


    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getStoreidfk() {
        return storeidfk;
    }

    public void setStoreidfk(String storeidfk) {
        this.storeidfk = storeidfk;
    }

    @PostPersist
    public void generateStoreCode() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
        String formattedDate = dateFormat.format(date);
        this.updatedDate = formattedDate;
        this.createdDate = formattedDate;
    }

}
