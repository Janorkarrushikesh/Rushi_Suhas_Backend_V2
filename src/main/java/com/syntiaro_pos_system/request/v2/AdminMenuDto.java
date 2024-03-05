package com.syntiaro_pos_system.request.v2;

import java.util.List;

public class AdminMenuDto {

    private String title;

    private String path;

    private String icon;

    private Long storeId;

    private Boolean status;

    private List<AdminSubMenuDto> adminSubMenudeto;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<AdminSubMenuDto> getAdminSubMenudeto() {
        return adminSubMenudeto;
    }

    public void setAdminSubMenudeto(List<AdminSubMenuDto> adminSubMenudeto) {
        this.adminSubMenudeto = adminSubMenudeto;
    }
}
