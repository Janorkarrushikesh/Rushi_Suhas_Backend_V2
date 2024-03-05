package com.syntiaro_pos_system.controllerimpl.v1;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.syntiaro_pos_system.controller.v1.CustomerController;
import com.syntiaro_pos_system.entity.v1.CustomerDetails;
import com.syntiaro_pos_system.repository.v1.CustomerRepo;
import com.syntiaro_pos_system.response.MessageResponse;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
public class CustomerControllerImpl implements CustomerController {

    @Autowired
    CustomerRepo customerRepo;

    @Override
    public ResponseEntity<?> addCustomer(@RequestBody CustomerDetails customerDetails) {

        if (customerRepo.existsByCustomerNameAndStoreId(customerDetails.getCustomerName(), customerDetails.getStoreId())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Customer Name is already exist !")); //bug_08 changes to taken to exist
        }

        if (customerRepo.existsByContactAndStoreId(customerDetails.getContact(), customerDetails.getStoreId())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Contact Number is already exist !"));  //bug_08 changes to taken to exist
        }

        if (customerRepo.existsByEmailAndStoreId(customerDetails.getEmail(), customerDetails.getStoreId())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already exist !"));  //bug_08 changes to taken to exist
        }


        Integer lastBillNumbers = customerRepo.findMaxVendorIdByStoreId(customerDetails.getStoreId());
        customerDetails.setCustomerId(lastBillNumbers != null ? lastBillNumbers + 1 : 1);
        customerRepo.save(customerDetails);
        // return customerDetails.getCustomer_name();

        return ResponseEntity.ok(new MessageResponse("Data Save Succesfully"));
    }


    @Override
    public ResponseEntity<List<CustomerDetails>> getCustomerByStoreId(@PathVariable Integer storeId) {
        try {
            List<CustomerDetails> customerDetails = customerRepo.findByStore_id(storeId);
            if (customerDetails.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(customerDetails, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<?> generatePDF(
            @RequestParam Integer store_id,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") String startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") String endDate) throws DocumentException {
        List<CustomerDetails> customerlist;
        Date startDates = null;
        Date endDates = null;

        if (store_id != null) {
            // Fetch payments for a specific store ID
            customerlist = customerRepo.findByStoreIdAndDateRange(store_id, startDate, endDate);

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
            customerlist = customerRepo.findByStoreIdAndDateRange(store_id, startDate, endDate);
        } else {
            // If no date range is specified, retrieve all stores
            customerlist = customerRepo.findByStoreIdAndDateRange(store_id, startDate, endDate);
        }

        if (customerlist.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(document, byteArrayOutputStream);

        document.open();

        Paragraph title = new Paragraph("VENDOR_INVENTORY_DETAILS", new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD));
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);// Add spacing between title and table


        Paragraph spacing = new Paragraph(" "); // Empty paragraph
        spacing.setSpacingAfter(10f); // Adjust the spacing as needed
        document.add(spacing);


        PdfPTable table = new PdfPTable(5); // Number of columns
        table.setWidthPercentage(100);

        PdfPCell cell = new PdfPCell(new Phrase("Sr No", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Customer Name", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Email", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Date Of Birth", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Contact No", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        table.addCell(cell);

        int serialNumber = 1;
        for (CustomerDetails invoice : customerlist) {
            table.addCell(String.valueOf(serialNumber++));
            table.addCell(String.valueOf(invoice.getCustomerName()));
            table.addCell(invoice.getEmail());
            table.addCell(invoice.getDateOfBirth());
            table.addCell(invoice.getContact());
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
    public ResponseEntity<?> generatePDFbyStoreid(
            @PathVariable Integer store_id) throws DocumentException {
        List<CustomerDetails> customerlist;
        Date startDates = null;
        Date endDates = null;

        if (store_id != null) {
            // Fetch payments for a specific store ID
            customerlist = customerRepo.findByStore_id(store_id);

        } else {
            // If no date range is specified, retrieve all stores
            customerlist = customerRepo.findByStore_id(store_id);
        }

        if (customerlist.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(document, byteArrayOutputStream);

        document.open();

        Paragraph title = new Paragraph("VENDOR_INVENTORY_DETAILS", new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD));
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);// Add spacing between title and table


        Paragraph spacing = new Paragraph(" "); // Empty paragraph
        spacing.setSpacingAfter(10f); // Adjust the spacing as needed
        document.add(spacing);


        PdfPTable table = new PdfPTable(5); // Number of columns
        table.setWidthPercentage(100);

        PdfPCell cell = new PdfPCell(new Phrase("Sr No", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Customer Name", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Email", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Date Of Birth", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Contact No", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        table.addCell(cell);

        int serialNumber = 1;
        for (CustomerDetails invoice : customerlist) {
            table.addCell(String.valueOf(serialNumber++));
            table.addCell(String.valueOf(invoice.getCustomerName()));
            table.addCell(invoice.getEmail());
            table.addCell(invoice.getDateOfBirth());
            table.addCell(invoice.getContact());
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
