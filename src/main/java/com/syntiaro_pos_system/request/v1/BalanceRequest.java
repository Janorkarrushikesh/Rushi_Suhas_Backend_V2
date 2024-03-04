package com.syntiaro_pos_system.request.v1;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BalanceRequest {
    private LocalDate date;
    private Double todaysopeningBalance;
    private Integer storeId;
    private String createdBy;
    private String updatedBy;
    private Double previousClosingBalance;
    private Double addMoreAmounts;
    private String finalHandedOverTo;
    private Double finalAmount;
    private Double finalClosingBalance;


    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Double getTodaysopeningBalance() {
        return todaysopeningBalance;
    }

    public void setTodaysopeningBalance(Double todaysopeningBalance) {
        this.todaysopeningBalance = todaysopeningBalance;
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

    public Double getPreviousClosingBalance() {
        return previousClosingBalance;
    }

    public void setPreviousClosingBalance(Double previousClosingBalance) {
        this.previousClosingBalance = previousClosingBalance;
    }

    public Double getAddMoreAmounts() {
        return addMoreAmounts;
    }

    public void setAddMoreAmounts(Double addMoreAmounts) {
        this.addMoreAmounts = addMoreAmounts;
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
}