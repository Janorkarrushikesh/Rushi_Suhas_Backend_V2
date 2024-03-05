package com.syntiaro_pos_system.request.v1;

import com.syntiaro_pos_system.entity.v1.Store;
import com.syntiaro_pos_system.entity.v1.StorePayment;
import com.syntiaro_pos_system.entity.v1.Tax;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class StoreRequest {


    private Store store;
    private List<Tax> taxes;
    private List<StorePayment> storePayment;

}
