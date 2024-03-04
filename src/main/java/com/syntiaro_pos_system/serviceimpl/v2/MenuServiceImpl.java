package com.syntiaro_pos_system.serviceimpl.v2;

import com.syntiaro_pos_system.entity.v1.Menu;
import com.syntiaro_pos_system.entity.v2.ApiResponse;
import com.syntiaro_pos_system.repository.v2.MenuRepository;
import com.syntiaro_pos_system.service.v2.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MenuServiceImpl implements MenuService {
    @Autowired
    MenuRepository menuRepository;
    @Override
    public ApiResponse getMenuById(int menuId) {
      Optional<Menu> menudata= menuRepository.findById(menuId);
      if(menudata.isPresent()){
          return new ApiResponse(menudata,true,200);
      }
      return new ApiResponse(null,false,"Id Not Found",400);
    }
}
