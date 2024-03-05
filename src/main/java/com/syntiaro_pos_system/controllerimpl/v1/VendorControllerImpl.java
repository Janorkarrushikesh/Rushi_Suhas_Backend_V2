package com.syntiaro_pos_system.controllerimpl.v1;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.syntiaro_pos_system.controller.v1.VendorController;
import com.syntiaro_pos_system.entity.v1.Vendor;
import com.syntiaro_pos_system.repository.v1.VendorRepo;
import com.syntiaro_pos_system.service.v1.VendorService;
import com.syntiaro_pos_system.serviceimpl.v1.VendorServiceIMPL;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@RestController
public class VendorControllerImpl implements VendorController {

    @Autowired
    VendorService vendorService;

    @Autowired
    VendorServiceIMPL vendorServiceIMPL;

    @Autowired
    VendorRepo vendorRepo;

    //THIS METHOD IS USE FOR POST VENDOR
    @Override
    public String saveVendor(Vendor vendor) {

        return vendorService.addVendor(vendor);
    }

    //THIS METHOD IS USE FOR GET ALL LIST OF VENDOR
    @Override
    public List<Vendor> getVendor() {
        return vendorService.getVendors();
    }

    //THIS METHOD IS USE FOR UPDATE VENDOR
    @Override
    public Vendor updatevendor(Vendor vendor) {
        return this.vendorService.updateVendor(vendor);
    }

    //THIS METHOD IS USE FOR DELETE VENDOR
    @Override
    public ResponseEntity<HttpStatus> deleteVendor(Long Serial_no) {
        try {
            vendorRepo.deleteById(Serial_no);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // THIS METHOD IS USE FOR UPDATE VENDOR
    @Override
    public ResponseEntity<Vendor> updateVendors(@PathVariable("vendor_id") Integer vendor_id, @RequestBody Vendor vendor) {
        try {
            Vendor updateVendor = vendorService.updateVendor(vendor_id, vendor);
            if (updateVendor != null) {
                return new ResponseEntity<>(updateVendor, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // THIS METHOD IS USE FOR FETCH VENDOR BY STOREID
    @Override
    public List<Vendor> getVendorByStoreId(@PathVariable Integer storeId) {
        return vendorService.fetchVendorsBystoreId(storeId);
    }


    // THIS METHOD IS USE FOR FETCH VENDOR BY ID
    @Override
    public ResponseEntity<Vendor> fetchDetailsById(@PathVariable Long id) {
        Vendor vendor = vendorService.getVendorDetailsById(Math.toIntExact(id));
        if (vendor == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(vendor);
    }


    @Override
    public ResponseEntity<byte[]> generateExcelByStoreId(Integer store_id) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Vendor Data");
            Row headerRow = sheet.createRow(0);

            String[] headerss = {"Vendor Id", "Vendor Name", "Vendor Address", "Bank Name", "Account No", "IFSC Code", "GST No", "Mobile No", "Store ID", "Vendor Code"};

            for (int i = 0; i < headerss.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headerss[i]);
            }
            List<Vendor> vendorsList = vendorRepo.findBystore_id(store_id);
            int rowNum = 1;
            for (Vendor vendor : vendorsList) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(vendor.getVendorId());
                row.createCell(1).setCellValue(vendor.getVendorName());
                row.createCell(2).setCellValue(vendor.getVendorAddress());
                row.createCell(3).setCellValue(vendor.getBankName());
                row.createCell(4).setCellValue(vendor.getAccountNo());
                row.createCell(5).setCellValue(vendor.getIfscCode());
                row.createCell(6).setCellValue(vendor.getGstNo());
                row.createCell(7).setCellValue(vendor.getMobileNo());
                row.createCell(8).setCellValue(vendor.getStoreId());
                row.createCell(9).setCellValue(vendor.getVendorCode());
            }
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "Vendor_data.xlsx");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(outputStream.toByteArray());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    // remove gst no because it was not shown in frontend .
    @Override
    public ResponseEntity<byte[]> generateExcelByStoreIdWithDateRange(
            @RequestParam Integer store_id,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") String startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") String endDate) {

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Vendor Data");
            Row headerRow = sheet.createRow(0);

            String[] headers = {
                    "Vendor Id", "Vendor Name", "Vendor Address", "Bank Name", "Account No",
                    "IFSC Code", "Mobile No", "Store ID", "Vendor Code"
            };

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }

            // Replace this with your actual data retrieval logic
            List<Vendor> vendorsList = vendorRepo.findByStoreIdAndDateRange(store_id, startDate, endDate);
            int rowNum = 1;

            for (Vendor vendor : vendorsList) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(vendor.getVendorId());
                row.createCell(1).setCellValue(vendor.getVendorName());
                row.createCell(2).setCellValue(vendor.getVendorAddress());
                row.createCell(3).setCellValue(vendor.getBankName());
                row.createCell(4).setCellValue(vendor.getAccountNo());
                row.createCell(5).setCellValue(vendor.getIfscCode());
                row.createCell(6).setCellValue(vendor.getMobileNo());
                row.createCell(7).setCellValue(vendor.getStoreId());
                row.createCell(8).setCellValue(vendor.getVendorCode());
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            HttpHeaders headerss = new HttpHeaders();
            headerss.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headerss.setContentDispositionFormData("attachment", "Vendor_data.xlsx");

            return ResponseEntity.ok()
                    .headers(headerss)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(outputStream.toByteArray());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @Override
    public ResponseEntity<?> generatePDF(
            @RequestParam Integer store_id,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") String startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") String endDate) throws DocumentException {
        List<Vendor> vendorlist;
        Date startDates = null;
        Date endDates = null;

        if (store_id != null) {
            // Fetch payments for a specific store ID
            vendorlist = vendorRepo.findByStoreIdAndCreatedDateBetween(store_id, startDate, endDate);

        } else if (startDate != null && endDate != null) {
            try {
                // Parse date strings into java.util.Date
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                startDates = dateFormat.parse(startDate);
                endDates = dateFormat.parse(endDate);
            } catch (ParseException ex) {
                // Handle the parsing error here, e.g., return an error response
                return ResponseEntity.badRequest().body("Invalid date format");
            }

            // Filter the stores based on the date range
            vendorlist = vendorRepo.findByStoreIdAndCreatedDateBetween(store_id, startDate, endDate);
        } else {
            // If no date range is specified, retrieve all stores
            vendorlist = vendorRepo.findAll();
        }

        if (vendorlist.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(document, byteArrayOutputStream);

        document.open();

        Paragraph title = new Paragraph("VENDOR DETAILS", new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD));
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);// Add spacing between title and table


        Paragraph spacing = new Paragraph(" "); // Empty paragraph
        spacing.setSpacingAfter(10f); // Adjust the spacing as needed
        document.add(spacing);


        PdfPTable table = new PdfPTable(8); // Number of columns
        table.setWidthPercentage(100);

        PdfPCell cell = new PdfPCell(new Phrase("Sr No", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Vendor Name", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Vendor Code", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Vendor Address", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Bank Name", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Branch Name", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Account No", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("UPI Id", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        table.addCell(cell);

        int serialNumber = 1;

        for (Vendor vendor : vendorlist) {
            table.addCell(String.valueOf(serialNumber++));
            table.addCell(vendor.getVendorName());
            table.addCell(vendor.getVendorCode());
            table.addCell(vendor.getVendorAddress());
            table.addCell(vendor.getBankName());
            table.addCell((vendor.getBranch()));
            table.addCell(String.valueOf(vendor.getAccountNo()));
            table.addCell(vendor.getUpiId());
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
    public ResponseEntity<?> generatePDFByStoreid(
            @PathVariable Integer store_id) throws DocumentException {
        List<Vendor> vendorlist;
        Date startDates = null;
        Date endDates = null;

        if (store_id != null) {
            // Fetch payments for a specific store ID
            vendorlist = vendorRepo.findBystore_id(store_id);

        } else {
            // If no date range is specified, retrieve all stores
            vendorlist = vendorRepo.findAll();
        }

        if (vendorlist.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(document, byteArrayOutputStream);

        document.open();

        Paragraph title = new Paragraph("VENDOR DETAILS", new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD));
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);// Add spacing between title and table


        Paragraph spacing = new Paragraph(" "); // Empty paragraph
        spacing.setSpacingAfter(10f); // Adjust the spacing as needed
        document.add(spacing);


        PdfPTable table = new PdfPTable(8); // Number of columns
        table.setWidthPercentage(100);

        PdfPCell cell = new PdfPCell(new Phrase("Sr No", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Vendor Name", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Vendor Code", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Vendor Address", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Bank Name", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Branch Name", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Account No", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("UPI Id", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        table.addCell(cell);
        int serialNumber = 1;
        for (Vendor vendor : vendorlist) {
            table.addCell(String.valueOf(serialNumber++));
            table.addCell(vendor.getVendorName());
            table.addCell(vendor.getVendorCode());
            table.addCell(vendor.getVendorAddress());
            table.addCell(vendor.getBankName());
            table.addCell((vendor.getBranch()));
            table.addCell(String.valueOf(vendor.getAccountNo()));
            table.addCell(vendor.getUpiId());
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

    /*----changes made by trupti------*/
    // THIS METHOD IS USE FOR UPDATE VENDOR
    @Override
    public ResponseEntity<?> updateVendor(@PathVariable("vendor_id") Long vendor_id, @RequestBody Vendor vendor) {
        try {
            ResponseEntity<?> responseEntity = vendorService.updateVendorWithValidation(vendor_id, vendor);
            return responseEntity;
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
