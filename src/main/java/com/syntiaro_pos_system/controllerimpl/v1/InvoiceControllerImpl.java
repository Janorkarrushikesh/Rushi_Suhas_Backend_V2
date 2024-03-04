package com.syntiaro_pos_system.controllerimpl.v1;

import com.syntiaro_pos_system.controller.v1.InvoiceController;
import com.syntiaro_pos_system.entity.v1.VendorInventory;
import com.syntiaro_pos_system.repository.v1.InvoiceRepo;
import com.syntiaro_pos_system.service.v1.InvoiceService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
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
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
public class InvoiceControllerImpl implements InvoiceController {

    @Autowired
    InvoiceService invoiceService;

    @Autowired
    InvoiceRepo invoiceRepo;

    @Override
    public String saveInvoice(VendorInventory vendorInventory) {
        return invoiceService.addInvoice(vendorInventory);
    }

    // THIS METHOD IS USE FOR UPDATE INVOICE
    @Override
    public VendorInventory updateinvoice(VendorInventory vendorInventory) {
        return this.invoiceService.updateInvoice(vendorInventory);
    }

    // THIS METHOD IS USE FOR GET ALL LIST OF INVOICE
    @Override
    public List<VendorInventory> getInvoice() {
        return this.invoiceService.getInvoice();
    }

    // THIS METHOD IS USE FOR UPDATE INVOICE
    @Override
    public ResponseEntity<VendorInventory> updateInvoice(@PathVariable("invoice_id") Integer invoice_id, @RequestBody VendorInventory vendorInventory) {
        try {
            VendorInventory updateVendorInventory = invoiceService.updateInvoice(invoice_id, vendorInventory);
            if (updateVendorInventory != null) {
                return new ResponseEntity<>(updateVendorInventory, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // THIS METHOD IS USE FOR FETCH INVOICE BY STOREID
    @Override
    public List<VendorInventory> getInvoicesByStoreId(@PathVariable Integer storeId) {
        return invoiceService.fetchInvoicesByStoreId(storeId);
    }

    // THIS METHOD IS USE FOR FETCH INVOICE BY ID
    @Override
    public ResponseEntity<?> fetchDetailsById(@PathVariable Integer id) {
        Optional<VendorInventory> optionalInvoice = invoiceRepo.findById(id);
        if (optionalInvoice.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Invoice with ID " + id + " not found");
        }
        return ResponseEntity.ok(optionalInvoice.get());
    }

    // THIS METHOD IS USE FOR DELETE INVOICE
    @Override
    public ResponseEntity<String> deleteInvoice(@PathVariable Integer invoiceId) {
        try {
            invoiceService.deleteInvoiceById(invoiceId);
            return ResponseEntity.ok("Invoice with ID " + invoiceId + " deleted");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<byte[]> generateExcelByStoreId(@PathVariable Integer storeId ) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Invoice Data");
            Row headerRow = sheet.createRow(0);
            String[] headerss = { "invoice Id", "Vendor Name", "invoice Date", "item_name",
                    "Quantity", "Unit", "Total", "Store ID" };
            for (int i = 0; i < headerss.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headerss[i]);
            }
            List<VendorInventory> Lists = invoiceRepo.findByStore_id(storeId);
            int rowNum = 1;
            for (VendorInventory List : Lists) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(List.getInvoiceId());
                row.createCell(1).setCellValue(List.getVendorName());
                row.createCell(2).setCellValue(List.getInvoiceDate());
                row.createCell(3).setCellValue(List.getItemName());
                row.createCell(4).setCellValue(List.getQuantity());
                row.createCell(5).setCellValue(List.getUnit());
                row.createCell(6).setCellValue(List.getTotal());
                row.createCell(7).setCellValue(List.getStoreId());
            }
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "Invoice_data.xlsx");
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(outputStream.toByteArray());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //----------------             FOR GENRATE EXCEL BY DATE FILTER        --------------

    @Override
    public ResponseEntity<byte[]> generateExcelByStoreId(
            @RequestParam Integer store_id,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") String startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") String endDate) {

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Invoice Data");
            Row headerRow = sheet.createRow(0);
            String[] headers = { "invoice Id", "Vendor Name", "Inventory Code","Item Name","Quantity","Price", "Unit", "Discount",
                     "Total" };
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }

            // Modify your query to filter by date range
            List<VendorInventory> vendorInventories = invoiceRepo.findByStoreIdAndDateRange(store_id, startDate, endDate);

            int rowNum = 1;
            for (VendorInventory vendorInventory : vendorInventories) {
                Row row = sheet.createRow(rowNum++);
               row.createCell(0).setCellValue(vendorInventory.getInvoiceId());
                row.createCell(1).setCellValue(vendorInventory.getVendorName());
                row.createCell(2).setCellValue(vendorInventory.getInventoryCode());
               row.createCell(3).setCellValue(vendorInventory.getItemName());
                row.createCell(4).setCellValue(vendorInventory.getQuantity());
                row.createCell(5).setCellValue(vendorInventory.getPrice());
                row.createCell(6).setCellValue(vendorInventory.getUnit());
                row.createCell(7).setCellValue(vendorInventory.getDiscount());
                row.createCell(8).setCellValue(vendorInventory.getTotal());
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            HttpHeaders headerss = new HttpHeaders();
            headerss.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headerss.setContentDispositionFormData("attachment", "Invoice_data.xlsx");
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
        List<VendorInventory> vendorInventoryList;
        Date startDates = null;
        Date endDates = null;

        if (store_id != null) {
            // Fetch payments for a specific store ID
            vendorInventoryList = invoiceRepo.findByStoreIdAndDateRange(store_id , startDate , endDate);

        } else if (startDate != null && endDate != null) {
            try {
                // Parse date strings into java.util.Date
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                startDates = dateFormat.parse(String.valueOf(startDate));
                endDates = dateFormat.parse(String.valueOf(endDate));
            } catch (ParseException ex) {
                // Handle the parsing error here, e.g., return an error response
                return ResponseEntity.badRequest().body("Invalid date format");
            }

            // Filter the stores based on the date range
            vendorInventoryList = invoiceRepo.findByStoreIdAndDateRange(store_id , startDate , endDate);
        } else {
            // If no date range is specified, retrieve all stores
            vendorInventoryList = invoiceRepo.findAll();
        }

        if (vendorInventoryList.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(document, byteArrayOutputStream);

        document.open();

        Paragraph title = new Paragraph("VENDOR_INVENTORY_DETAILS" , new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD));
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);// Add spacing between title and table


        Paragraph spacing = new Paragraph(" "); // Empty paragraph
        spacing.setSpacingAfter(10f); // Adjust the spacing as needed
        document.add(spacing);



        PdfPTable table = new PdfPTable(9); // Number of columns
        table.setWidthPercentage(100);

        PdfPCell cell = new PdfPCell(new Phrase("Sr No", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        table.addCell(cell);
         cell = new PdfPCell(new Phrase("Vendor Name", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Inventory code", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Item Name", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Quantity", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Price", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Unit", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Discount(%)", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Total", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        table.addCell(cell);
        ;
        int serialNumber = 1;

        for (VendorInventory vendorInventory : vendorInventoryList) {
            table.addCell(String.valueOf(serialNumber++));
            table.addCell(String.valueOf(vendorInventory.getVendorName()));
            table.addCell(vendorInventory.getInventoryCode());
            table.addCell(vendorInventory.getItemName());
            table.addCell(vendorInventory.getQuantity());
            table.addCell(vendorInventory.getPrice());
            table.addCell(vendorInventory.getUnit());
            table.addCell(vendorInventory.getDiscount());
            table.addCell(vendorInventory.getTotal());

        }

        document.add(table);

        document.close();

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=VENDOR_INVENTORY_DETAILS.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(byteArrayInputStream));
    }

    @Override
    public ResponseEntity<?> generatePDFbystoreid(
            @PathVariable Integer store_id) throws DocumentException {
        List<VendorInventory> vendorInventoryList;
        Date startDates = null;
        Date endDates = null;

        if (store_id != null) {
            // Fetch payments for a specific store ID
            vendorInventoryList = invoiceRepo.findByStore_id(store_id);

        } else {
            // If no date range is specified, retrieve all stores
            vendorInventoryList = invoiceRepo.findAll();
        }

        if (vendorInventoryList.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(document, byteArrayOutputStream);

        document.open();

        Paragraph title = new Paragraph("VENDOR_INVENTORY_DETAILS" , new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD));
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);// Add spacing between title and table


        Paragraph spacing = new Paragraph(" "); // Empty paragraph
        spacing.setSpacingAfter(10f); // Adjust the spacing as needed
        document.add(spacing);



        PdfPTable table = new PdfPTable(9); // Number of columns
        table.setWidthPercentage(100);

        PdfPCell cell = new PdfPCell(new Phrase("Sr No", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Vendor Name", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Inventory code", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Item Name", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Quantity", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Price", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Unit", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Discount(%)", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Total", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        table.addCell(cell);
        ;
        int serialNumber = 1;

        for (VendorInventory vendorInventory : vendorInventoryList) {
            table.addCell(String.valueOf(serialNumber++));
            table.addCell(String.valueOf(vendorInventory.getVendorName()));
            table.addCell(vendorInventory.getInventoryCode());
            table.addCell(vendorInventory.getItemName());
            table.addCell(vendorInventory.getQuantity());
            table.addCell(vendorInventory.getPrice());
            table.addCell(vendorInventory.getUnit());
            table.addCell(vendorInventory.getDiscount());
            table.addCell(vendorInventory.getTotal());

        }

        document.add(table);

        document.close();

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=VENDOR_INVENTORY_DETAILS.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(byteArrayInputStream));
    }




}