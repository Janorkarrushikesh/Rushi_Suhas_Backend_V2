package com.syntiaro_pos_system.entity.v1;

import java.util.Map;

public class ApiResponse {

    private boolean status;
    private String message = null;
    private int responseCode;
    private Map<String, Object> data;

    public ApiResponse() {
    }

    public ApiResponse(boolean status, String message, int responseCode, Map<String, Object> data) {
        this.status = status;
        this.message = message;
        this.responseCode = responseCode;
        this.data = data;
    }

    public ApiResponse(Map<String, Object> data,boolean status,int responseCode, String message) {
        this.status = status;
        this.message = message;
        this.responseCode = responseCode;
        this.data = data;
    }

    public ApiResponse(Map<String, Object> data,boolean status,int responseCode) {
        this.status = status;
        this.responseCode = responseCode;
        this.data = data;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }
}
