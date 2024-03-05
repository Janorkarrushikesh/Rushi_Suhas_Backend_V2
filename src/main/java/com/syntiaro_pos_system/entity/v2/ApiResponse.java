package com.syntiaro_pos_system.entity.v2;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiResponse {
    private Object data;
    private boolean status;
    private String message;
    private int responseCode;

    //For Status Ok
    public ApiResponse(Object data, boolean status, int responseCode) {
        this.data = data;
        this.status = status;
        this.responseCode = responseCode;
    }

    //For Status Bad
    public ApiResponse(Object data, boolean status, String message, int responseCode) {
        this.data = data;
        this.status = status;
        this.message = message;
        this.responseCode = responseCode;
    }
}
