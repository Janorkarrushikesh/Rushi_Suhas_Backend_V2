package com.syntiaro_pos_system.controllerimpl.v1;

import com.syntiaro_pos_system.controller.v1.FoodController;
import com.syntiaro_pos_system.entity.v1.Food;
import com.syntiaro_pos_system.repository.v1.FoodRepo;
import com.syntiaro_pos_system.service.v1.FoodService;
import com.syntiaro_pos_system.serviceimpl.v1.BalanceService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.List;

@RestController
public class FoodControllerIMPL implements FoodController {

    @Autowired
    private FoodService foodService;

    @Autowired
    private FoodRepo foodRepo;

    @Autowired
    private BalanceService balanceService;
    private Logger logger = LoggerFactory.getLogger(FoodController.class);

    // THIS METHOD IS USE FOR GET ALL LIST OF FOOD
    @Override
    public List<Food> getFood(String food) {

        return this.foodService.getFood();

    }

    // THIS METHOD IS USE FOR POST FOOD
    @Override
    public String addFood(@RequestBody Food food) {
        String id = foodService.addFood(food);
        return id;
    }

    // THIS METHOD IS USE FOR DELETE FOOD
    @Override
    public ResponseEntity<HttpStatus> deleteFood(@PathVariable String food_id) {
        try {
            this.foodService.deletefood(Integer.parseInt(food_id));
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<String> getFoodImage(@PathVariable Integer id) {
        Food food = foodRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Food not found with id: " + id));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);

        return new ResponseEntity<>(food.getImage(), headers, HttpStatus.OK);
    }

    // THIS METHOD IS USE FOR GET FOOD BY ID
    public Optional<Food> getFoodByid(Integer food_id) {
        return this.foodService.getfoodbyid(food_id);
    }

    // THIS METHOD IS USE FOR UPDATE FOOD
    @Override
    public ResponseEntity<Food> updateFood(@PathVariable("Serial_no") Integer Serial_no, @RequestBody Food food) {
        try {
            Food updateFood = foodService.updateFood(Serial_no, food);
            if (updateFood != null) {
                return new ResponseEntity<>(updateFood, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // THIS METHOD IS USE FOR FETCH FOOD BY ID
    @Override
    public ResponseEntity<?> getFoodById(Integer food_id) {
        Optional<Food> food = foodService.getfoodbyid(food_id);
        if (food.isPresent()) {
            return ResponseEntity.ok(food.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // THIS METHOD IS USE FOR FETCH FOOD BY STOREID
    @Override
    public ResponseEntity<List<Food>> getFoodsByStoreId(@PathVariable String storeId) {
        try {
            List<Food> foods = foodService.fetchFoodsByStoreId(storeId);
            if (foods.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(foods, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

//    @GetMapping("/foods/{storeId}")
//    public CompletableFuture<ResponseEntity<List<Food>>> getFoodsByStoreId(@PathVariable String storeId) {
//        try {
//            return CompletableFuture.supplyAsync(() -> {
//               // List<Food> foods = foodService.fetchFoodsByStoreId(storeId);
//                List<Food> foods =   foodRepo.findByStore_id(storeId);
//                if (foods.isEmpty()) {
//                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//                }
//                return new ResponseEntity<>(foods, HttpStatus.OK);
//            });
//        } catch (Exception e) {
//            return CompletableFuture.completedFuture(new ResponseEntity<>(HttpStatus.NOT_FOUND));
//        }
//    }

//------------------------- REDUCING TIME ------------------------------------
//   private Map<String, List<Food>> foodCache = new HashMap<>();
//
//  // private Map<String, List<Food>> foodCache = new ConcurrentHashMap<>();
//    @GetMapping("/foods/{storeId}")
//    public ResponseEntity<List<Food>> getFoodsByStoreId(@PathVariable String storeId) {
//        // Check if the request can be served from cache
//        List<Food> cachedFoods = foodCache.get(storeId);
//        if (cachedFoods != null) {
//            return ResponseEntity.ok(cachedFoods);
//        }
//
//        // If not in cache, fetch from the database
//        List<Food> foods = foodRepo.findByStore_id(storeId);
//        if (foods.isEmpty()) {
//            return ResponseEntity.notFound().build();
//        }
//
//        // Cache the result for future requests
//        foodCache.put(storeId, foods);
//           return ResponseEntity.ok(foods);
//    }




    // THIS METHOD IS USE FOR NOT INSERT SAME FOOD NAME
    @Override
    public ResponseEntity<String> addfoods(
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
            @RequestParam("image") String image) {

        // Check if the food name already exists for the given store_id
        boolean foodExists = foodRepo.existsByFoodNameAndStoreId(food_name, store_id);
        if (foodExists) {
            System.out.println("Food with the same name already exists in this store.");
            return ResponseEntity.badRequest().body("Food with the same name already exists in this store.");
        }
        Food foods = new Food();
        foods.setFoodName(food_name);
        foods.setDescription(description);
        foods.setFoodCode(foodcode);
        foods.setCategory(category);
        foods.setSubCategory(subcategory);
        foods.setUpdateBy(update_by);
        foods.setCreatedBy(created_by);
        foods.setGstNo(gst_no);
        foods.setStoreId(store_id);
        foods.setPrice(price);
//        foods.setImage("https://drive.google.com/uc?export=view&id="+image);
        foods.setImage(image);
        // ---------------ADDED NEW CODE-----BY RUSHIKESH-----------------------
        Integer lastBillNumber = foodRepo.findLastNumberForStore(store_id);
        foods.setFoodId(lastBillNumber != null ? lastBillNumber + 1 : 1);
        Food createdFood = foodRepo.save(foods);
        return ResponseEntity.status(HttpStatus.CREATED).body(String.valueOf(createdFood));
    }

    @Override
    public ResponseEntity<String> updatefood(
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
            @RequestParam(required = false) String image) throws IOException {

        Optional<Food> optionalFood = foodRepo.findById(Serial_no);
        if (!optionalFood.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Food food = optionalFood.get();
        // Update fields if provided in the request
        if(food_id != null){
            food.setFoodId(Integer.valueOf(food_id));
        }
        if (food_name != null) {
            food.setFoodName(food_name);
        }
        if (foodcode != null) {
            food.setFoodCode(foodcode);
        }
        if (description != null) {
            food.setDescription(description);
        }
        if (category != null) {
            food.setCategory(category);
        }
        if (subcategory != null) {
            food.setSubCategory(subcategory);
        }
        if (update_by != null) {
            food.setUpdateBy(update_by);
        }
        if (created_by != null) {
            food.setCreatedBy(created_by);
        }
        if (gst_no != null) {
            food.setGstNo(gst_no);
        }
        if (price != null) {
            food.setPrice(Integer.valueOf(price));
        }

        if (image != null) {
//            food.setImage("https://drive.google.com/uc?export=view&id="+image);
            food.setImage(image);
        }
        foodRepo.save(food);
        return ResponseEntity.ok("Food updated successfully!");
    }

    @Override
    public ResponseEntity<byte[]> generateExcelByStoreId(String store_id ) { // Accept storeId as a parameter
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Food Data");
            Row headerRow = sheet.createRow(0);
            String[] headerss = { "Food Name", "Description", "Food Code", "Category", "Subcategory", "Store ID", "Price" };
            for (int i = 0; i < headerss.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headerss[i]);
            }
            List<Food> foodList = foodRepo.findByStoreId(store_id ); // Assuming you have a method to fetch food data by
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
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(outputStream.toByteArray());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<String> uploadFoodList(@RequestParam("store_id") String storeId,
            @RequestParam("file") MultipartFile file) {
        try {
            List<Food> foodList = foodService.processExcelFile(storeId, file);
            return new ResponseEntity<>("Food list uploaded successfully for store ID: " + storeId, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to upload food list: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<byte[]> generateExcelByStoreIdAndCategory(@PathVariable String store_id) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Addon Data");
            Row headerRow = sheet.createRow(0);
            String[] headers = { "Addon Name", "Addon Code", "Category", "Price" };
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }

            // Assuming Food class has a getCategory() method
            List<Food> addonList = foodRepo.findByStoreIdAndCategory(store_id, "Addon");
            int rowNum = 1;
            for (Food food : addonList) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(food.getFoodName());
                row.createCell(1).setCellValue(food.getFoodCode());
                row.createCell(2).setCellValue(food.getCategory());
                row.createCell(3).setCellValue(food.getPrice());
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);

            HttpHeaders headerss = new HttpHeaders();
            headerss.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headerss.setContentDispositionFormData("attachment", "Addons_Data.xlsx");

            return ResponseEntity.ok()
                    .headers(headerss)
                    .body(outputStream.toByteArray());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @Override
    public ResponseEntity<?> generatePDFByStoreid (
            @PathVariable String store_id) throws DocumentException {
        List<Food> foodlist;
        Date startDates = null;
        Date endDates = null;

        if (store_id != null) {
            // Fetch payments for a specific store ID
            foodlist = foodRepo.findByStore_id(store_id);

        } else {
            // If no date range is specified, retrieve all stores
            foodlist = foodRepo.findAll();
        }

//        if (foodRepo.isEmpty()) {
//            return ResponseEntity.notFound().build();
//        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(document, byteArrayOutputStream);

        document.open();

        Paragraph title = new Paragraph("FOOD DETAILS" , new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD));
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);// Add spacing between title and table


        Paragraph spacing = new Paragraph(" "); // Empty paragraph
        spacing.setSpacingAfter(10f); // Adjust the spacing as needed
        document.add(spacing);



        PdfPTable table = new PdfPTable(6); // Number of columns
        table.setWidthPercentage(100);

        PdfPCell cell = new PdfPCell(new Phrase("Sr No", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Food Name", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Food Code", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Category", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Subcategory", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Price", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        table.addCell(cell);        ;
        int serialNumber = 1;
        for (Food food : foodlist) {
            table.addCell(String.valueOf(serialNumber++));
            table.addCell(food.getFoodName());
            table.addCell(food.getFoodCode());
            table.addCell(food.getCategory());
            table.addCell(food.getSubCategory());
            table.addCell(String.valueOf(food.getPrice()));
         }

        document.add(table);
        document.close();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=Store-details.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(byteArrayInputStream));
    }




    @Override
    public ResponseEntity<?> generatePDFByStoreidandCategory(
            @PathVariable String store_id) throws DocumentException {
        List<Food> foodlist;
        Date startDates = null;
        Date endDates = null;

        if (store_id != null) {
            // Fetch payments for a specific store ID
            foodlist = foodRepo.findByStoreIdAndCategory(store_id,"Addon");

        } else {
            // If no date range is specified, retrieve all stores
            foodlist = foodRepo.findAll();
        }

//        if (foodRepo.isEmpty()) {
//            return ResponseEntity.notFound().build();
//        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(document, byteArrayOutputStream);

        document.open();

        Paragraph title = new Paragraph("ADDON DETAILS" , new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD));
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);// Add spacing between title and table


        Paragraph spacing = new Paragraph(" "); // Empty paragraph
        spacing.setSpacingAfter(10f); // Adjust the spacing as needed
        document.add(spacing);



        PdfPTable table = new PdfPTable(6); // Number of columns
        table.setWidthPercentage(100);

        PdfPCell cell = new PdfPCell(new Phrase("Sr No", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Food Name", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Food Code", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Category", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Subcategory", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Price", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        table.addCell(cell);        ;
        int serialNumber = 1;
        for (Food food : foodlist) {
            table.addCell(String.valueOf(serialNumber++));
            table.addCell(food.getFoodName());
            table.addCell(food.getFoodCode());
            table.addCell(food.getCategory());
            table.addCell(food.getSubCategory());
            table.addCell(String.valueOf(food.getPrice()));
        }

        document.add(table);
        document.close();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=Store-details.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(byteArrayInputStream));
    }





}
