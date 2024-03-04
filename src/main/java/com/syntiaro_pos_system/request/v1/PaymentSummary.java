package com.syntiaro_pos_system.request.v1;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PaymentSummary {

    private String paymentMode;
    private Float totalAmount;


}
