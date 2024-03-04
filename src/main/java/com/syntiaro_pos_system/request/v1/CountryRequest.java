package com.syntiaro_pos_system.request.v1;

import com.syntiaro_pos_system.entity.v1.Country;
import com.syntiaro_pos_system.entity.v1.State;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CountryRequest {

    private Country country;
    private List<State> states;

}
