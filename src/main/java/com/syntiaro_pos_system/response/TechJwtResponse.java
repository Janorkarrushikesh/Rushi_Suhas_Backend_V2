package com.syntiaro_pos_system.response;

import java.util.List;

public class TechJwtResponse {
    private final List<String> roles;
    private String token;
    private String type = "Bearer";
    private Long techid;
    private String username;
    private String email;
    private String gstno;


    public TechJwtResponse(String accessToken, Long techid, String username, String email, String gstno, List<String> roles) {
        this.token = accessToken;
        this.techid = techid;
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

    public Long getTechid() {
        return techid;
    }

    public void setTechid(Long techid) {
        this.techid = techid;
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

