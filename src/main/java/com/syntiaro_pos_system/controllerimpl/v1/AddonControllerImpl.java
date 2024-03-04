package com.syntiaro_pos_system.controllerimpl.v1;

import com.syntiaro_pos_system.controller.v1.AddonRest;
import com.syntiaro_pos_system.entity.v1.Addon;
import com.syntiaro_pos_system.entity.v1.ApiResponse;
import com.syntiaro_pos_system.service.v1.AddonServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AddonControllerImpl implements AddonRest {

	@Autowired
	AddonServices addonServices;
	@Override
	public ApiResponse saveaddon(@RequestBody Addon Addon) {
		ApiResponse id = addonServices.saveaddon(Addon);
		return id;

	}
	@Override
	public ApiResponse getAddon() {
		return addonServices.getAddOn();
	}

	@Override
	public Addon updateAddon(@RequestBody Addon Addon) {
		return this.addonServices.updateAddon(Addon);
	}

	@Override
	public ResponseEntity<HttpStatus> deleteUser(@PathVariable String itemid) {
		try {
			this.addonServices.deleteaddon(Integer.parseInt(itemid));
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public Addon fetchDetailsById(Integer itemid) {
		return addonServices.getAddonDetailsById(itemid);

	}
	@Override
	public ResponseEntity<Addon> updateAddon(@PathVariable("itemid") Integer itemid, @RequestBody Addon Addon) {
		try {
			Addon updateFoodAddon = addonServices.updateAddon(itemid, Addon);
			if (updateFoodAddon != null) {
				return new ResponseEntity<>(updateFoodAddon, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@Override
	public List<Addon> getAddonsByStoreId(@PathVariable String storeId) {
		return addonServices.getAddonsByStoreId(storeId);
	}

}
