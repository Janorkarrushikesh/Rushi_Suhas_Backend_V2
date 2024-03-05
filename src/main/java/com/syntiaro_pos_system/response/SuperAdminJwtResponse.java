package com.syntiaro_pos_system.response;

import java.util.List;

public class SuperAdminJwtResponse {
    private final List<String> roles;
    private String token;
    private String type = "Bearer";
    private Long superid;
    private String username;
    private String email;
    private String gstno;


    public SuperAdminJwtResponse(String accessToken, Long superid, String username, String email, String gstno, List<String> roles) {
        this.token = accessToken;
        this.superid = superid;
        this.username = username;
        this.email = email;
        this.gstno = gstno;
        this.roles = roles;

    }

    public String getAccessToken() {
        return token;
    }

    public void setAccessToken(String accessToken) {
        this.token = accessToken;
    }

    public String getTokenType() {
        return type;
    }

    public void setTokenType(String tokenType) {
        this.type = tokenType;
    }

    public String getGstno() {
        return gstno;
    }

    public void setGstno(String gstno) {
        this.gstno = gstno;
    }

    public Long getSuperid() {
        return superid;
    }

    public void setSuperid(Long superid) {
        this.superid = superid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public List<String> getRoles() {
        return roles;
    }
}
