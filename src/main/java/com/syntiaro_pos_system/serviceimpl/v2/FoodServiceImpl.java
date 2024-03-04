package com.syntiaro_pos_system.serviceimpl.v2;


import com.syntiaro_pos_system.entity.v1.Food;
import com.syntiaro_pos_system.entity.v2.ApiResponse;
import com.syntiaro_pos_system.repository.v2.AdminMenuRepository;
import com.syntiaro_pos_system.repository.v2.FoodRepository;
import com.syntiaro_pos_system.service.v2.FoodService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

@Service
public class FoodServiceImpl implements FoodService {

    @Autowired
    FoodRepository foodRepository;

    @Autowired
    AdminMenuRepository adminMenuRepository;

    @Override
    public ResponseEntity<ApiResponse> saveFood(Food food) throws IOException {
        boolean foodExistsInStore = foodRepository.existsByFoodNameAndStoreId(food.getFoodName(), food.getStoreId());
        if (foodExistsInStore) {
            return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body(new ApiResponse(null, false, "Food Already Exist " + food.getFoodName(), 208));
        } else {
            Integer foodId = foodRepository.findMaxFoodIdByStoreId(food.getStoreId());
            food.setFoodId((foodId != null) ? foodId + 1 : 1);
            return ResponseEntity.ok().body(new ApiResponse(foodRepository.save(food), true, 200));
        }
    }


    @Override
    public ResponseEntity<ApiResponse> getAllFood(Food food) {
        foodRepository.findAll();
        List<Food> Food_List = foodRepository.findAll();
        return ResponseEntity.ok().body(new ApiResponse(Food_List, true, 200));
    }

    @Override
    public ResponseEntity<ApiResponse> getFoodById(Integer serialNumber) {
        Optional<Food> foodOptional = foodRepository.findById(serialNumber);
        return ResponseEntity.ok().body(foodOptional.map(food -> new ApiResponse(food, true, 200)).orElse(new ApiResponse(null, false, "Food Details Not Available For This ID = " + serialNumber, 400)));
    }

    @Override
    public ResponseEntity<ApiResponse> getAllFoodByStoreId(String storeId) {
        List<Food> foodData = foodRepository.findByStoreIds(storeId);
        if (foodData.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(foodData, false, "Food Details Not Available For This ID = " + storeId, 404));
        }
        return ResponseEntity.ok().body(new ApiResponse(foodData, true, 200));
    }


    @Override
    public ResponseEntity<ApiResponse> getFoodsByStoreId(String storeId, Integer page, Integer size, String startDate, String endDate) {
        try {
            List<Food> existingFood = foodRepository.findByStoreIds(storeId);
            if (existingFood.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(null, false, "Food Not found For Given Id ", 404));
            } else {
                if (page != null && size != null) {
                    return ResponseEntity.ok().body(new ApiResponse(getByPageAndSize(storeId, page, size), true, 200));
                } else if (startDate != null && endDate != null) {
                    return ResponseEntity.ok().body(new ApiResponse(getByDate(storeId, startDate, endDate), true, 200));
                } else {
                    return ResponseEntity.ok().body(new ApiResponse(existingFood, true, 200));
                }
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(null, false, "...", 500));
        }
    }
    private Object getByDate(String storeId, String startDate, String endDate) {
        List<Food> existingFood = foodRepository.findBetweenDate(storeId, startDate, endDate);
        List<Map<String, Object>> foodList = new ArrayList<>();
        if (existingFood != null) {
            Map<String, Food> map = new LinkedHashMap<>();
            for (Food food : existingFood) {
                Map<String, Object> foodMap = new LinkedHashMap<>();
                foodMap.put("Id", food.getFoodId());
                foodMap.put("productName", food.getFoodName());
                foodMap.put("foodCode", food.getFoodCode());
                foodMap.put("category", food.getCategory());
                foodMap.put("subCategory", food.getSubCategory());
                foodMap.put("image", food.getImage());
                foodMap.put("price", food.getPrice());
                foodMap.put("status", food.getStatus());
                foodList.add(foodMap);
            }
        }
        return foodList;
    }

    private Object getByPageAndSize(String storeId, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Food> foodData = foodRepository.findByStoreId(storeId, pageable);
        List<Map<String, Object>> filterFoodList = new ArrayList<>();
        for (Food foodlist : foodData) {
            Map<String, Object> foodMap = new LinkedHashMap<>();
            foodMap.put("Id", foodlist.getFoodId());
            foodMap.put("productName", foodlist.getFoodName());
            foodMap.put("foodCode", foodlist.getFoodCode());
            foodMap.put("category", foodlist.getCategory());
            foodMap.put("subCategory", foodlist.getSubCategory());
            foodMap.put("image", foodlist.getImage());
            foodMap.put("price", foodlist.getPrice());
            foodMap.put("status", foodlist.getStatus());
            foodMap.put("totalPages", foodData.getTotalPages());
            filterFoodList.add(foodMap);
        }

        return filterFoodList;
    }

    @Override
    public ResponseEntity<ApiResponse> addonlist(String storeId) {
        try {
            List<Food> addonlist = foodRepository.findByStoreIdAndCategory(storeId, "Addon");
            return ResponseEntity.ok().body(new ApiResponse(addonlist, true, 200));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(null, false, 500));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> updateBySerialNumber(Integer serialNumber, Map<String, Object> foodMap) {
        try {
            Optional<Food> foodOptional = foodRepository.findById(serialNumber);
            if (foodOptional.isPresent()) {
                Food existingFood = foodOptional.get();
                foodMap.forEach((key, value) -> {
                    Field field = ReflectionUtils.findField(Food.class, key);
                    if (field != null) {
                        field.setAccessible(true);
                        ReflectionUtils.setField(field, existingFood, value);
                    }
                });
                foodRepository.save(existingFood);
                return ResponseEntity.ok().body(new ApiResponse(existingFood, true, 200));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(null, false, "Id not found ", 400));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(null, false, "...", 500));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> deleteBySerialNumber(Integer serialNumber) {
        try {
            Optional<Food> existingFood = foodRepository.findById(serialNumber);
            if (existingFood.isPresent()) {
                foodRepository.deleteById(serialNumber);
                return ResponseEntity.ok().body(new ApiResponse(existingFood, true, 200));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(null, false, "This id does not exist = " + serialNumber, 404));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(null, false, "...", 400));
        }

    }


    @Override
    public ResponseEntity<ApiResponse> UploadExcelFile(String storeId, MultipartFile file) throws IOException {
        try {
            List<Food> foodList = new ArrayList<>();
            Workbook workbook = WorkbookFactory.create(file.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();
            rowIterator.next();
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                String foodName = row.getCell(0).getStringCellValue();
                if (foodRepository.existsByFoodNameAndStoreId(foodName, storeId)) {
                    continue;
                }
                Food food = new Food();
                food.setStoreId(storeId);
                food.setFoodName(foodName);
                food.setFoodId((int) row.getCell(1).getNumericCellValue());
                food.setCategory(row.getCell(2).getStringCellValue());
                food.setSubCategory(row.getCell(3).getStringCellValue());
                food.setDescription(row.getCell(4).getStringCellValue());
                food.setPrice((int) row.getCell(5).getNumericCellValue());
                food.setFoodCode(row.getCell(6).getStringCellValue());
                foodRepository.save(food);
                foodList.add(food);
            }
            return ResponseEntity.ok().body(new ApiResponse(foodList, true, 200));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(null, false, "...", 400));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> downloadExcelfileByStoreId(String storeId) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Food Data");
            Row headerRow = sheet.createRow(0);
            String[] headerss = {"Food Name", "Description", "Food Code", "Category", "Subcategory", "Store ID", "Price"};
            for (int i = 0; i < headerss.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headerss[i]);
            }
            List<Food> foodList = foodRepository.findByStoreIds(storeId); // Assuming you have a method to fetch food data by
            // storeId
            int rowNum = 1;
            for (Food food : foodList) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(food.getFoodName());
                row.createCell(1).setCellValue(food.getDescription());
                row.createCell(2).setCellValue(food.getFoodCode());
                row.createCell(3).setCellValue(food.getCategory());
                row.createCell(4).setCellValue(food.getSubCategory());
                row.createCell(5).setCellValue(food.getStoreId());
                row.createCell(6).setCellValue(food.getPrice());
            }
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "food_data.xlsx");
            return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_OCTET_STREAM).body(new ApiResponse(outputStream.toByteArray(), true, 200));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(null, false, "...", 500));
        }
    }

    public List<Map<String, Object>> FoodsByStoreId(String storeId) {
        try {
            List<Food> foodData = foodRepository.findByStoreIds(storeId);
            List<Map<String, Object>> filterFoodList = new ArrayList<>();
            System.out.println(foodData);
            if (foodData.isEmpty()) {
                return filterFoodList;
            } else {
                for (Food foodlist : foodData) {
                    Map<String, Object> foodMap = new LinkedHashMap<>();
                    foodMap.put("Id", foodlist.getFoodId());
                    foodMap.put("productName", foodlist.getFoodName());
                    foodMap.put("category", foodlist.getCategory());
                    foodMap.put("subCategory", foodlist.getSubCategory());
                    foodMap.put("image", foodlist.getImage());
                    foodMap.put("price", foodlist.getPrice());
                    foodMap.put("status", foodlist.getStatus());
                    filterFoodList.add(foodMap);
                }
            }
            return filterFoodList;
        } catch (Exception e) {
            return null;
        }
    }
}
