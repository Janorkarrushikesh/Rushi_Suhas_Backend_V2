package com.syntiaro_pos_system.security.services;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.syntiaro_pos_system.entity.v1.SuperAdmin;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class SuperAdminDetailsImpl implements UserDetails {
    private static final long serialVersionUID = 1L;


    private final Long id;

    private final String username;

    private final String email;

    @JsonIgnore
    private final String password;
    private String gstno;

    private String logoUrl; // Add the logo URL field

    private final Collection<? extends GrantedAuthority> authorities;


    public SuperAdminDetailsImpl(Long id, String username, String email, String password, String gstno,
                                 Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.gstno = gstno;
        this.authorities = authorities;
    }

    public static SuperAdminDetailsImpl build(SuperAdmin superAdmin) {
        List<GrantedAuthority> authorities = superAdmin.getSuperAdminRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());

        SuperAdminDetailsImpl userDetails = new SuperAdminDetailsImpl(
                superAdmin.getSuperid(),
                superAdmin.getUsername(),
                superAdmin.getEmail(),
                superAdmin.getPassword(),
                superAdmin.getGstNo(),
                authorities);

        return userDetails;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public String getGstno() {
        return gstno;
    }

    public void setGstno(String gstno) {
        this.gstno = gstno;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        SuperAdminDetailsImpl Store = (SuperAdminDetailsImpl) o;
        return Objects.equals(id, Store.id);
    }
}
