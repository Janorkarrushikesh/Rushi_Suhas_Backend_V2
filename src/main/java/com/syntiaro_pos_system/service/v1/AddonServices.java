package com.syntiaro_pos_system.service.v1;


import com.syntiaro_pos_system.entity.v1.Addon;
import com.syntiaro_pos_system.entity.v1.ApiResponse;

import java.util.List;


public interface AddonServices {

    ApiResponse saveaddon(Addon Addon);

    ApiResponse getAddOn();

    Addon updateAddon(Addon fAddon);

    void deleteaddon(int itemid);

    // THIS METHOD IS USE FOR FETCH ADDON BY ID
    Addon getAddonDetailsById(Integer itemid);


    // THIS METHOD IS USE FOR UPDATE ADDON
    Addon updateAddon(Integer itemid, Addon updateaddon);


    // THIS METHOD IS USE FOR FETCH ADDON BY STOREID
    List<Addon> getAddonsByStoreId(String storeId);

}
