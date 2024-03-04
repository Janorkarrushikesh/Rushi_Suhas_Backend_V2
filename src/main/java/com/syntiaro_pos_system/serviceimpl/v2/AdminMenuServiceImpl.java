package com.syntiaro_pos_system.serviceimpl.v2;

import com.syntiaro_pos_system.entity.v2.AdminMenu;
import com.syntiaro_pos_system.entity.v2.AdminSubMenu;
import com.syntiaro_pos_system.entity.v2.ApiResponse;
import com.syntiaro_pos_system.exception.v2.EntityNotFoundExceptionById;
import com.syntiaro_pos_system.repository.v2.AdminMenuRepository;
import com.syntiaro_pos_system.request.v2.AdminMenuDto;
import com.syntiaro_pos_system.request.v2.AdminSubMenuDto;
import com.syntiaro_pos_system.service.v2.AdminMenuService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AdminMenuServiceImpl implements AdminMenuService {

    private static final Logger logger = LoggerFactory.getLogger(AdminMenuService.class);

    @Autowired
    AdminMenuRepository adminMenuRepository;

    @Override
    public ResponseEntity<ApiResponse> SaveAdminemenu(List<AdminMenu> adminMenu) {
        try {
            List<AdminMenu> adminMenus = adminMenuRepository.saveAll(adminMenu);

            return ResponseEntity.ok().body(new ApiResponse(adminMenus, true, "AdminMenu saved successfully", 200));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(null, false, "...", 500));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> getByStoreIdAndStatus(Long storeId) {
        try {
            List<AdminMenu> adminMenus = adminMenuRepository.findByStoreId(storeId);
            if (adminMenus != null) {
                List<AdminMenu> filteredAdminMenus = new ArrayList<>();
                for (AdminMenu adminMenu : adminMenus) {
                    if (adminMenu.getStatus() != null && adminMenu.getStatus()) {
                        List<AdminSubMenu> filteredSubMenus = adminMenu.getAdminSubMenu();

                        if (filteredSubMenus != null) {
                            filteredSubMenus = filteredSubMenus.stream().filter(subMenu -> subMenu.getStatus() != null && subMenu.getStatus()).collect(Collectors.toList());
                        } else {
                            filteredSubMenus = Collections.emptyList();
                        }
                        adminMenu.setAdminSubMenu(filteredSubMenus);
                        filteredAdminMenus.add(adminMenu);
                    }
                }
                return ResponseEntity.ok().body(new ApiResponse(filteredAdminMenus, true, 200));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(null, false, "Invalid Store Id", 404));
            }
        } catch (Exception e) {
            e.printStackTrace(); // Print or log the exception for debugging purposes
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(null, false, "... ", 500));
        }
    }
    // new code with filter

    @Override
    public ResponseEntity<ApiResponse> getStoreByStoreIdAndStatus(Long storeId) {
        try {
            List<Map<String, Object>> filteredAdminMenus = null;
            List<AdminMenu> adminMenus = adminMenuRepository.findByStoreId(storeId);
            if (adminMenus != null) {
                filteredAdminMenus = new ArrayList<>();
                for (AdminMenu adminMenu : adminMenus) {
                    if (adminMenu.getStatus() != null && adminMenu.getStatus()) {
                        Map<String, Object> adminmenudetaillsit = new LinkedHashMap<>();
                        adminmenudetaillsit.put("title", adminMenu.getTitle());
                        adminmenudetaillsit.put("icon", adminMenu.getIcon());
                        adminmenudetaillsit.put("path", adminMenu.getPath());
                        adminmenudetaillsit.put("status", adminMenu.getStatus());
                        List<Map<String, Object>> adminSubmenuList = new ArrayList<>();
                        for (AdminSubMenu adminsubmenu : adminMenu.getAdminSubMenu()) {
                            if (adminsubmenu.getStatus() != null && adminsubmenu.getStatus()) {
                                Map<String, Object> submenuMap = new LinkedHashMap<>();
                                submenuMap.put("title", adminsubmenu.getTitle());
                                submenuMap.put("path", adminsubmenu.getPath());
                                submenuMap.put("icon", adminsubmenu.getIcon());
                                submenuMap.put("status", adminsubmenu.getStatus());
                                adminSubmenuList.add(submenuMap);
                            }
                        }
                        adminmenudetaillsit.put("subMenu", adminSubmenuList);
                        filteredAdminMenus.add(adminmenudetaillsit);
                    }
                }
                return ResponseEntity.ok().body(new ApiResponse(filteredAdminMenus, true, 200));
            } else
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(null, false, "Id not found ", 404));
        } catch (Exception e) {
            e.printStackTrace(); // Print or log the exception for debugging purposes
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(null, false, "..", 500));

        }
    }

    @Override
    public ResponseEntity<ApiResponse> updateAdminMenu(Long adminMenuId, AdminMenu adminMenu) {
        try {
            AdminMenu existingEntity = adminMenuRepository.findById(adminMenuId).orElseThrow(() -> new EntityNotFoundExceptionById("Inavlid Id  provided"));
            existingEntity.setTitle(adminMenu.getTitle());
            existingEntity.setPath(adminMenu.getPath());
            existingEntity.setStatus(adminMenu.getStatus());
            existingEntity.setStoreId(adminMenu.getStoreId());
            existingEntity.setAdminSubMenu(adminMenu.getAdminSubMenu());

            AdminMenu saveEntity = adminMenuRepository.save(existingEntity);
            return ResponseEntity.ok().body(new ApiResponse(saveEntity, true, "Updated Successfully ", 200));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(null, false, "...", 500));
        }

    }

    @Override
    public ResponseEntity<ApiResponse> getByStoreId(Long storeId) {
        try {
            List<AdminMenu> adminMenus = adminMenuRepository.findByStoreId(storeId);
            if (adminMenus != null && !adminMenus.isEmpty()) {

                List<AdminMenuDto> adminMenuDtos = new ArrayList<>();
                for (AdminMenu adminMenu : adminMenus) {
                    AdminMenuDto adminMenuDto = new AdminMenuDto();
                    adminMenuDto.setTitle(adminMenu.getTitle());
                    adminMenuDto.setPath(adminMenu.getPath());
                    adminMenuDto.setIcon(adminMenu.getIcon());
                    adminMenuDto.setStoreId(adminMenu.getStoreId());
                    adminMenuDto.setStatus(adminMenu.getStatus());

                    List<AdminSubMenuDto> adminSubMenuDtos = new ArrayList<>();
                    for (AdminSubMenu adminSubMenu : adminMenu.getAdminSubMenu()) {
                        AdminSubMenuDto adminSubMenuDto = new AdminSubMenuDto();
                        adminSubMenuDto.setTitle(adminSubMenu.getTitle());
                        adminSubMenuDto.setPath(adminSubMenu.getPath());
                        adminSubMenuDto.setIcon(adminSubMenu.getIcon());
                        adminSubMenuDto.setStoreId(adminSubMenu.getStoreId());
                        adminSubMenuDto.setStatus(adminSubMenu.getStatus());
                        adminSubMenuDtos.add(adminSubMenuDto);
                    }
                    adminMenuDto.setAdminSubMenudeto(adminSubMenuDtos);
                    adminMenuDtos.add(adminMenuDto);
                }
                return ResponseEntity.ok().body(new ApiResponse(adminMenuDtos, true, 200));

            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(null, false, "Id not found ", 404));
            }
        } catch (Exception e) {
            logger.error("An error occurred while fetching admin menu for store ID: {}", storeId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(null, false, "..", 500));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> getByTitleAndStoreId(String title, Long storeId) {
        try {
            AdminMenu adminMenu = adminMenuRepository.findByTitleAndStoreId(title, storeId);
            if (adminMenu != null) {
                if (adminMenu.getStatus() != null && adminMenu.getStatus()) {
                    List<AdminSubMenu> filteredSubMenus = adminMenu.getAdminSubMenu();

                    if (filteredSubMenus != null) {
                        filteredSubMenus = filteredSubMenus.stream().filter(subMenu -> subMenu.getStatus() != null && subMenu.getStatus()).collect(Collectors.toList());
                    } else {
                        // Handle case where adminMenu.getAdminSubMenu() is null
                        filteredSubMenus = Collections.emptyList();
                    }
                    adminMenu.setAdminSubMenu(filteredSubMenus);
                    return ResponseEntity.ok().body(new ApiResponse(Collections.singletonList(adminMenu), true, 200));

                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(null, false, "Admin menu is not active", 404));
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(null, false, "Id not found ", 404));
            }
        } catch (Exception e) {
            e.printStackTrace(); // Print or log the exception for debugging purposes
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(null, false, "..", 500));
        }
    }

}


