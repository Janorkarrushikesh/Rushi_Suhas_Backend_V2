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
public class TransactionRequest {
    private LocalDate date = LocalDate.now();
    private Integer store_id;
    private String cashier;
    private String expense;
    private Float amount;

}