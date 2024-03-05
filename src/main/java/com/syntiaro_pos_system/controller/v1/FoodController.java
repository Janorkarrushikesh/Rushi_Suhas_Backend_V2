package com.syntiaro_pos_system.controller.v1;

import com.itextpdf.text.DocumentException;
import com.syntiaro_pos_system.entity.v1.Food;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(path = "/v1/sys/food")
@CrossOrigin(origins = "*", maxAge = 3600)
public interface FoodController {
    @GetMapping(path = "/getfood")
    List<Food> getFood(String food);

    @PostMapping(path = "/postfood")
    String addFood(@RequestBody Food food);

    @DeleteMapping(path = "/deletefood/{food_id}")
    ResponseEntity<HttpStatus> deleteFood(@PathVariable String food_id);

    @GetMapping("/image/{id}")
    ResponseEntity<String> getFoodImage(@PathVariable Integer id);

    @PatchMapping(path = "/updatedfoods/{Serial_no}")
    ResponseEntity<Food> updateFood(@PathVariable("Serial_no") Integer Serial_no,
                                    @RequestBody Food food);

    @GetMapping("/getFoodByID/{food_id}")
    ResponseEntity<?> getFoodById(@PathVariable Integer food_id);

    @PostMapping("/addfoods")
    ResponseEntity<String> addfoods(
            @RequestParam("food_name") String food_name,
            @RequestParam("description") String description,
            @RequestParam("foodcode") String foodcode,
            @RequestParam("category") String category,
            @RequestParam("subcategory") String subcategory,
            @RequestParam("update_by") String update_by,
            @RequestParam("gst_no") String gst_no,
            @RequestParam("created_by") String created_by,
            @RequestParam("store_id") String store_id,
            @RequestParam("price") Integer price,
            @RequestParam("image") String image);

    @PostMapping("/generateExcel/{store_id}")
    ResponseEntity<byte[]> generateExcelByStoreId(@PathVariable String store_id);

    @PostMapping("/upload")
    ResponseEntity<String> uploadFoodList(@RequestParam("store_id") String storeId,
                                          @RequestParam("file") MultipartFile file);

    @GetMapping("/foods/{storeId}")
    ResponseEntity<List<Food>> getFoodsByStoreId(@PathVariable String storeId);

    @PatchMapping("/updatefood/{Serial_no}")
    ResponseEntity<String> updatefood(
            @PathVariable Integer Serial_no,
            @RequestParam(required = false) String food_id,
            @RequestParam(required = false) String food_name,
            @RequestParam(required = false) String foodcode,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String subcategory,
            @RequestParam(required = false) String update_by,
            @RequestParam(required = false) String created_by,
            @RequestParam(required = false) String gst_no,
            @RequestParam(required = false) String price,
            @RequestParam(required = false) String image) throws IOException;

    @PostMapping("/exceladdon/{store_id}")
    ResponseEntity<byte[]> generateExcelByStoreIdAndCategory(@PathVariable String store_id);

    @PostMapping("/generate-pdf-food/{store_id}")
    ResponseEntity<?> generatePDFByStoreid(
            @PathVariable String store_id) throws DocumentException;

    @PostMapping("/generate-pdf-addon/{store_id}")
    ResponseEntity<?> generatePDFByStoreidandCategory(
            @PathVariable String store_id) throws DocumentException;

}
