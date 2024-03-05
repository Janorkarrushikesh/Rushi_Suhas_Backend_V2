package com.syntiaro_pos_system.request.v1;

import com.syntiaro_pos_system.entity.v1.Payment;
import com.syntiaro_pos_system.entity.v1.Vendor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class paymentRequest {
    private Vendor vendor;
    private Payment payment;

    public Payment getPayment() {
        return payment;
    }
}
