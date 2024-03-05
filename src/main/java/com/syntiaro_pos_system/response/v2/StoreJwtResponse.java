package com.syntiaro_pos_system.response.v2;

import java.util.List;

public class StoreJwtResponse {
    private String token;
    private String type = "Bearer";
    private Long Storeid;
    private String username;

    private String storeName; // Add the store name field

    private List<String> roles;

    public StoreJwtResponse(String accessToken, Long Storeid, String username, String storeName, List<String> roles) {
        this.token = accessToken;
        this.Storeid = Storeid;
        this.username = username;
        this.storeName = storeName;
        this.roles = roles;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getStoreid() {
        return Storeid;
    }

    public void setStoreid(Long storeid) {
        Storeid = storeid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
