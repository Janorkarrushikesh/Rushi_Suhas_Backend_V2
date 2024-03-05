package com.syntiaro_pos_system.entity.v1;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

@Data
@NoArgsConstructor
@DynamicUpdate
@DynamicInsert
@Table(name = "Balance")
@Entity
public class Balance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date = LocalDate.now();
    private Double todays_Opening_Balance;
    private Integer storeId;
    private String createdBy;
    private String updatedBy;
    private String createdDate;
    private String updatedDate;
    private Double remainingBalance;
    private String finalHandedOverTo;
    private Double finalAmount;
    private Double finalClosingBalance;
    private Double addMoreAmount;

    public Balance(LocalDate date, Integer storeId) {
        this.date = date;
        this.storeId = storeId;

    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Double getTodays_Opening_Balance() {
        return todays_Opening_Balance;
    }

    public void setTodays_Opening_Balance(Double todays_Opening_Balance) {
        this.todays_Opening_Balance = todays_Opening_Balance;
    }

    public Integer getStoreId() {
        return storeId;
    }

    public void setStoreId(Integer storeId) {
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

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Double getRemainingBalance() {
        return remainingBalance;
    }

    public void setRemainingBalance(Double remainingBalance) {
        this.remainingBalance = remainingBalance;
    }

    public String getFinalHandedOverTo() {
        return finalHandedOverTo;
    }

    public void setFinalHandedOverTo(String finalHandedOverTo) {
        this.finalHandedOverTo = finalHandedOverTo;
    }

    public Double getFinalAmount() {
        return finalAmount;
    }

    public void setFinalAmount(Double finalAmount) {
        this.finalAmount = finalAmount;
    }

    public Double getFinalClosingBalance() {
        return finalClosingBalance;
    }

    public void setFinalClosingBalance(Double finalClosingBalance) {
        this.finalClosingBalance = finalClosingBalance;
    }

    public Double getAddMoreAmount() {
        return addMoreAmount;
    }

    public void setAddMoreAmount(Double addMoreAmount) {
        this.addMoreAmount = addMoreAmount;
    }

    //-------------------- ADDED BY RUSHIKESH THIS CODE  ----------------------------
    @PostPersist
    public void generateStoreCode() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = dateFormat.format(date);
        this.updatedDate = formattedDate;
        this.createdDate = formattedDate;
    }
}