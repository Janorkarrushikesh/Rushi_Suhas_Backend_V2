package com.syntiaro_pos_system.serviceimpl.v2;

import com.syntiaro_pos_system.entity.v2.ApiResponse;
import com.syntiaro_pos_system.entity.v2.AppMenu;
import com.syntiaro_pos_system.entity.v2.DashBoard;
import com.syntiaro_pos_system.entity.v2.QuickAccess;
import com.syntiaro_pos_system.exception.v2.DashboardAlreadyExistsException;
import com.syntiaro_pos_system.exception.v2.EntityNotFoundExceptionById;
import com.syntiaro_pos_system.repository.v2.DashBoardRepository;
import com.syntiaro_pos_system.request.v2.AppMenuDto;
import com.syntiaro_pos_system.request.v2.DashBoardDTO;
import com.syntiaro_pos_system.request.v2.QuickAccessDto;
import com.syntiaro_pos_system.service.v2.DashBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DashBoardServiceImpl implements DashBoardService {

    @Autowired
    DashBoardRepository dashBoardRepository;

    @Override
    public ResponseEntity<ApiResponse> saveDashBoard(DashBoard dashBoard) {
        try {
            List<DashBoard> existingDashBoard = dashBoardRepository.findByStoreId(dashBoard.getStoreId());
            try {
                if (existingDashBoard != null) {
                    throw new DashboardAlreadyExistsException("Dashboard with StoreId " + dashBoard.getStoreId() + " already exists");
                }
                return ResponseEntity.ok().body(new ApiResponse(dashBoardRepository.save(dashBoard), true, 200));
            } catch (DashboardAlreadyExistsException ex) {
                return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body(new ApiResponse(dashBoard, false, ex.getMessage(), 208));
            }
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(null, false, "....", 500));
        }
    }

    //Old Method
    @Override
    public ResponseEntity<ApiResponse> getByStoreId(Long storeId) {
        try {
            List<DashBoard> dashBoardList = dashBoardRepository.findByStoreId(storeId);
            if (dashBoardList != null) {
                DashBoard dashBoard = dashBoardList.get(0);

                DashBoardDTO dashBoardDto = new DashBoardDTO();
                dashBoardDto.setStoreId(dashBoard.getStoreId());
                dashBoardDto.setStoreName(dashBoard.getStoreName());

                List<QuickAccessDto> quickAccesseslist = dashBoard.getQuickaccess().stream()
                        .map(item -> {
                            QuickAccessDto quickAccessDTO = new QuickAccessDto();
                            quickAccessDTO.setName(item.getName());
                            quickAccessDTO.setPath(item.getPath());
                            quickAccessDTO.setIcon(item.getIcon());
                            quickAccessDTO.setStatus(item.getStatus());
                            return quickAccessDTO;
                        })
                        .collect(Collectors.toList());
                dashBoardDto.setQuickaccess(quickAccesseslist);

                List<AppMenuDto> appMenuDtos = dashBoard.getAppMenu().stream()
                        .map(item -> {
                            AppMenuDto appMenuDto = new AppMenuDto();
                            appMenuDto.setName(item.getName());
                            appMenuDto.setPath(item.getPath());
                            appMenuDto.setIcon(item.getIcon());
                            appMenuDto.setStatus(item.getStatus());
                            return appMenuDto;
                        })
                        .collect(Collectors.toList());
                dashBoardDto.setAppMenu(appMenuDtos);
                return ResponseEntity.ok().body(new ApiResponse(dashBoardDto, true, 200));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(dashBoardList, false, "This Store id Does not exist" + storeId, 404));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(null, false, "....", 500));
        }
    }

    // New Method

    public ResponseEntity<ApiResponse> getByStoreIdAndStatus(Long storeId) {
        try {
            List<DashBoard> dashboardOptional = dashBoardRepository.findByStoreId(storeId);

            if (dashboardOptional != null) {
                DashBoard dashboard = dashboardOptional.get(0);
                List<QuickAccess> quickAccessItems = dashboard.getQuickaccess().stream()
                        .filter(item -> item.getStatus())
                        .collect(Collectors.toList());

                List<AppMenu> appMenuItems = dashboard.getAppMenu().stream()
                        .filter(item -> item.getStatus())
                        .collect(Collectors.toList());

                DashBoard storeMenuDTO = new DashBoard(quickAccessItems, appMenuItems);

                return ResponseEntity.ok().body(new ApiResponse(storeMenuDTO, true, 200));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(null, false, "This Store id does not exist: " + storeId, 404));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(null, false, "....", 500));
        }
    }

    public List<Map<String, Object>> getStoreByStoreIdAndStatus(Long storeId) {
        List<DashBoard> dashboardData = dashBoardRepository.findByStoreId(storeId);

        List<Map<String, Object>> dashboardList = new ArrayList<>();
        for (DashBoard dashboard : dashboardData) {
            Map<String, Object> dashboardMap = new LinkedHashMap<>();
            dashboardMap.put("id", dashboard.getId());
            dashboardMap.put("storeName", dashboard.getStoreName());
            dashboardMap.put("storeId", dashboard.getStoreId());

            List<Map<String, Object>> quickAcessList = new ArrayList<>();
            for (QuickAccess quickAccess : dashboard.getQuickaccess()) {
                if (quickAccess.getStatus() != null && quickAccess.getStatus()) {
                    Map<String, Object> quickaccessMap = new LinkedHashMap<>();
                    quickaccessMap.put("id", quickAccess.getId());
                    quickaccessMap.put("title", quickAccess.getName());
                    quickaccessMap.put("icon", quickAccess.getIcon());
                    quickaccessMap.put("path", quickAccess.getPath());
                    quickaccessMap.put("status", quickAccess.getStatus());
                    quickAcessList.add(quickaccessMap);
                }
            }
            dashboardMap.put("quickAccess", quickAcessList);

            List<Map<String, Object>> appMenuList = new ArrayList<>();
            for (AppMenu appmenu : dashboard.getAppMenu()) {
                if (appmenu.getStatus() != null && appmenu.getStatus()) {
                    Map<String, Object> appMenuMap = new LinkedHashMap<>();
                    appMenuMap.put("id", appmenu.getId());
                    appMenuMap.put("title", appmenu.getName());
                    appMenuMap.put("icon", appmenu.getIcon());
                    appMenuMap.put("path", appmenu.getPath());
                    appMenuMap.put("status", appmenu.getStatus());
                    appMenuList.add(appMenuMap);
                }
            }

            dashboardMap.put("appMenu", appMenuList);
            dashboardList.add(dashboardMap);
        }
        return dashboardList;

    }

    @Override
    public ResponseEntity<ApiResponse> updateBoardByid(Long id, DashBoard dashboard) {
        DashBoard existingEntity = dashBoardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundExceptionById("Invalid Id was provided"));
        existingEntity.setStoreName(dashboard.getStoreName());
        existingEntity.setStoreId(dashboard.getStoreId());
        existingEntity.setQuickaccess(dashboard.getQuickaccess());
        existingEntity.setAppMenu(dashboard.getAppMenu());
        try {
            DashBoard savedEntity = dashBoardRepository.save(existingEntity);
            return ResponseEntity.ok().body(new ApiResponse(savedEntity, true, 200));
        } catch (Exception e) {
            // Handle any exception that may occur during the save operation
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body( new ApiResponse(null, false, "Failed to update the dashboard",500));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> deleteById(Long id) {
        try {
            Optional<DashBoard> existingEntityOptional = dashBoardRepository.findById(id);
            if (existingEntityOptional.isPresent()) {
                dashBoardRepository.deleteById(id);
                return ResponseEntity.ok().body(new ApiResponse(null, true, "deleted successfully", 200));
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(null, false, "Id not found", 404));

        } catch (Exception e) {
            // Handle any exception that may occur during the delete operation
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(null, false, "....", 500));
        }
    }
}