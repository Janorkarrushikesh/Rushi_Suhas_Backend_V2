package com.syntiaro_pos_system.request.v2;

import com.syntiaro_pos_system.entity.v2.AppMenu;
import com.syntiaro_pos_system.entity.v2.QuickAccess;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.List;

public class DashBoardDTO {


    private String storeName;

    private Long storeId;
    private List<QuickAccessDto> quickaccess;
    private List<AppMenuDto> appMenu;

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public List<QuickAccessDto> getQuickaccess() {
        return quickaccess;
    }

    public void setQuickaccess(List<QuickAccessDto> quickaccess) {
        this.quickaccess = quickaccess;
    }

    public List<AppMenuDto> getAppMenu() {
        return appMenu;
    }

    public void setAppMenu(List<AppMenuDto> appMenu) {
        this.appMenu = appMenu;
    }
}
