package com.syntiaro_pos_system.serviceimpl.v1;


import com.syntiaro_pos_system.entity.v1.Addon;
import com.syntiaro_pos_system.entity.v1.ApiResponse;
import com.syntiaro_pos_system.repository.v1.AddonRepo;
import com.syntiaro_pos_system.service.v1.AddonServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Service
public class AddonServiceIMPL implements AddonServices {

    @Autowired
    private AddonRepo addonRepo;


    //THIS METHOD IS USE FOR POST ADDON
    @Override
    public ApiResponse saveaddon(Addon foodAddon) {
        // TODO Auto-generated method stub
        addonRepo.save(foodAddon);

        Map<String, Object> data = new HashMap<>();
        data.put("Addon", foodAddon);

        return new ApiResponse(data, true, 200);
    }

    //THIS METHOD IS USE FOR GET ALL LIST OF ADDON
    @Override
    public ApiResponse getAddOn() {

        Map<String, Object> data = new HashMap<>();
        data.put("Addon_list", addonRepo.findAll());


        return new ApiResponse(data, true, 200);
    }

    //THIS METHOD IS USE FOR UPDATE ADDON
    @Override
    public Addon updateAddon(Addon Addon) {
        addonRepo.save(Addon);
        return Addon;
    }

    //THIS METHOD IS USE FOR DELETE ADDON
    @Override
    public void deleteaddon(int i) {
        Addon entity = addonRepo.getOne(i);
        addonRepo.delete(entity);

    }

    // THIS METHOD IS USE FOR FETCH ADDON BY ID
    @Override
    public Addon getAddonDetailsById(Integer itemid) {
        return addonRepo.findById(itemid).orElse(null);
    }


    // THIS METHOD IS USE FOR FETCH ADDON BY STOREID
    @Override
    public List<Addon> getAddonsByStoreId(String storeId) {
        return addonRepo.findByStoreId(storeId);
    }


    // THIS METHOD IS USE FOR UPDATE ADDON
    @Override
    public Addon updateAddon(Integer itemid, Addon Addon) {
        Optional<Addon> existingAddon = addonRepo.findById(Integer.parseInt(String.valueOf((itemid))));
        if (existingAddon.isPresent()) {
            Addon updateaddon = existingAddon.get();

            // Update specific fields if provided in the request
//
            if (Addon.getItemName() != null) {
                updateaddon.setItemName(Addon.getItemName());
            }
            if (Addon.getGstNo() != null) {
                updateaddon.setGstNo(Addon.getGstNo());
            }
            if (Addon.getPrice() != null) {
                updateaddon.setPrice(Addon.getPrice());
            }
            if (Addon.getUpdateDate() != null) {
                updateaddon.setUpdateDate(Addon.getUpdateDate());
            }
            if (Addon.getUpdateBy() != null) {
                updateaddon.setUpdateBy(Addon.getUpdateBy());
            }
            if (Addon.getCreatedDate() != null) {
                updateaddon.setCreatedDate(Addon.getCreatedDate());
            }
            if (Addon.getCreatedBy() != null) {
                updateaddon.setCreatedBy(Addon.getCreatedBy());
            }
            if (Addon.getStoreId() != null) {
                updateaddon.setStoreId(Addon.getStoreId());
            }
            if (Addon.getQuantity() != null) {
                updateaddon.setQuantity(Addon.getQuantity());
            }
            if (Addon.getAddonCode() != null) {
                updateaddon.setAddonCode(Addon.getAddonCode());
            }
            addonRepo.save(updateaddon);
            return updateaddon;
        } else {
            return null;
        }
    }

}
