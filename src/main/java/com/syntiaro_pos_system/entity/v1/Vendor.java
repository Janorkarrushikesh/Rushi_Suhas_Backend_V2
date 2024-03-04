package com.syntiaro_pos_system.entity.v1;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;


//@NamedQuery(name ="Vendor.getAllVendor", query ="Select new com.SYNTIARO_POS_SYSTEM.Response.VendorWrapper (v.vendor_id, v.vendor_name, v.vendor_address, v.mobile_no, v.GST_no,  v.bank_name, v.Branch, v.account_no, v.IFSC_code, v.UPI_id )from Vendor v")


@Entity
@Table(name = "Vendor")
@Data
@DynamicUpdate
@DynamicInsert
@AllArgsConstructor
@NoArgsConstructor
public class Vendor implements Serializable {

    @Serial
    private static final long SerialVersion = 1L;

    // ----------------------RUSHIKESH ADDED THIS NEW CODE----------------------
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long serialNo;

    @Column(name = "vendor_id", length = 45)
    private Integer vendorId;

    @Column(name = "vendor_name", length = 255)
    private String vendorName;

    @Column(name = "vendor_address", length = 255)
    private String vendorAddress;

    @Column(name = "mobile_no")
    private Long mobileNo;

    @Column(name = "GST_no", length = 255)
    private String gstNo;

    @Column(name = "update_date", length = 50)
    private String updateDate;

    @Column(name = "update_by", length = 50)
    private String updatedBy;

    @Column(name = "created_date")
    private String createdDate;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "Store_id", length = 255)
    private Integer storeId;

    @Column(name = "bank_name", length = 255)
    private String bankName;

    @Column(name = "branch", length = 255)
    private String branch;

    @Column(name = "account_no", length = 255)
    private String accountNo;

    @Column(name = "ifsc_code", length = 255)
    private String ifscCode;

    @Column(name = "upi_id", length = 255)
    private String upiId;

    @Column(name = "vendor_code", length = 255)
    private String vendorCode;


    public Integer getVendorId() {
        return vendorId;
    }

    public void setVendorId(Integer vendorId) {
        this.vendorId = vendorId;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getVendorAddress() {
        return vendorAddress;
    }

    public void setVendorAddress(String vendorAddress) {
        this.vendorAddress = vendorAddress;
    }

    public Long getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(Long mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getGstNo() {
        return gstNo;
    }

    public void setGstNo(String gstNo) {
        this.gstNo = gstNo;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Integer getStoreId() {
        return storeId;
    }

    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }


    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getIfscCode() {
        return ifscCode;
    }

    public void setIfscCode(String ifscCode) {
        this.ifscCode = ifscCode;
    }

    public String getUpiId() {
        return upiId;
    }

    public void setUpiId(String upiId) {
        this.upiId = upiId;
    }

    public String getVendorCode() {
        return vendorCode;
    }

    public void setVendorCode(String vendorCode) {
        this.vendorCode = vendorCode;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    @PostPersist
    public void generateStoreCode() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = dateFormat.format(date);
        SimpleDateFormat dateFormats = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDates = dateFormats.format(date);
        this.updateDate = formattedDate;
        this.createdDate = formattedDates;
    }
}