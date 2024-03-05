package com.syntiaro_pos_system.controllerimpl.v1;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.syntiaro_pos_system.controller.v1.OrderController;
import com.syntiaro_pos_system.entity.v1.Bill;
import com.syntiaro_pos_system.entity.v1.OrderFood;
import com.syntiaro_pos_system.entity.v1.Orders;
import com.syntiaro_pos_system.repository.v1.BillRepo;
import com.syntiaro_pos_system.repository.v1.OrderRepo;
import com.syntiaro_pos_system.request.v1.OrderRequest;
import com.syntiaro_pos_system.service.v1.OrderService;
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
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
public class OrderControllerImpl implements OrderController {

    @Autowired
    OrderService orderService;
    @Autowired
    BillRepo billRepo;
    @Autowired
    private OrderRepo orderRepo;

    // THIS METHOS IS USE FOR POST ORDER
    @Override
    public Orders placeOrder(@RequestBody OrderRequest request) {
        return orderRepo.save(request.getOrder());
    }

    // THIS METHOD IS USE FOR UPDATE ORDER
    @Override
    public Orders updateOrder(@RequestBody Orders order) {
        return this.orderService.updateOrder(order);
    }

    // THIS METHOD IS USE FOR DELETE ORDER
    @Override
    public ResponseEntity<HttpStatus> deleteorder(@PathVariable String oid) {
        try {
            this.orderService.deleteorder(Integer.parseInt(oid));
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // THIS METHOD IS USE FOR GET ALL LIST OF ORDER
    @Override
    public List<Orders> getorder() {
        return this.orderService.getorder();
    }

    // THIS METHOD IS USE FOR GET ORDER BY ID
    @Override
    public Optional<Orders> getorderbyid(@PathVariable Integer orderid) {
        return this.orderService.getorderbyid(orderid);
    }

    // THIS METHOD IS USE FOR GET ORDER BY STOREID
    @Override
    public List<Orders> getOrdersByStoreId(@PathVariable("storeId") String storeId) {
        return orderService.getOrdersByStoreId(storeId);
    }

    // THIS METHOD IS USE FOR POST ORDER
    @Override
    public int addOrder(@RequestBody Orders order) {
        int id = orderService.addOrder(order);
        return id;
    }

    // THIS METHOD IS USE FOR GET ORDER BY ID
    @Override
    public ResponseEntity<Orders> fetchorderbyid(@PathVariable Integer oid) {
        Optional<Orders> orderOptional = this.orderService.getorderbyid(oid);
        if (orderOptional.isPresent()) {
            Orders order = orderOptional.get();
            return ResponseEntity.ok(order);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // THIS METHOD IS USE FOR UPDATE ORDER
    @Override
    public ResponseEntity<?> updateOrderPartial(@PathVariable String orderid, @RequestBody OrderRequest request) {
        Optional<Orders> orderOptional = orderRepo.findById(Integer.parseInt(orderid));
        if (orderOptional.isPresent()) {
            Orders existingOrder = orderOptional.get();
            Orders updatedOrder = request.getOrder();

            // Patch the order properties
            if (updatedOrder.getOrderDate() != null) {
                existingOrder.setOrderDate(updatedOrder.getOrderDate());
            }
            if (updatedOrder.getTableNo() != null) {
                existingOrder.setTableNo(updatedOrder.getTableNo());
            }
            if (updatedOrder.getOrderStatus() != null) {
                existingOrder.setOrderStatus(updatedOrder.getOrderStatus());
            }
            if (updatedOrder.getOrderType() != null) {
                existingOrder.setOrderType(updatedOrder.getOrderType());
            }
            if (updatedOrder.getCreatedDate() != null) {
                existingOrder.setCreatedDate(updatedOrder.getCreatedDate());
            }
            if (updatedOrder.getUpdatedDate() != null) {
                existingOrder.setUpdatedDate(updatedOrder.getUpdatedDate());
            }
            if (updatedOrder.getCreatedName() != null) {
                existingOrder.setCreatedName(updatedOrder.getCreatedName());
            }
            if (updatedOrder.getUpdatedBy() != null) {
                existingOrder.setUpdatedBy(updatedOrder.getUpdatedBy());
            }
            if (updatedOrder.getStoreId() != null) {
                existingOrder.setStoreId(updatedOrder.getStoreId());
            }
            // Add more properties to patch as needed
            orderRepo.save(existingOrder);
            return ResponseEntity.ok("Order updated successfully!");
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @Override
    public ResponseEntity<?> updateOrderStatus(@PathVariable Integer Serial_no) {
        Optional<Orders> orderOptional = orderRepo.findbySerialno(Serial_no);

        if (orderOptional.isPresent()) {
            Orders existingOrder = orderOptional.get();
            existingOrder.setOrderStatus("Prepared");
            // Add more properties to patch as needed
            orderRepo.save(existingOrder);
            return ResponseEntity.ok("Order Status updated successfully!");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<?> updateOrderStatus2(@PathVariable Integer Serial_no) {
        Optional<Orders> orderOptional = orderRepo.findbySerialno(Serial_no);

        if (orderOptional.isPresent()) {
            Orders existingOrder = orderOptional.get();
            existingOrder.setOrderStatus("completed");
            // Add more properties to patch as needed
            orderRepo.save(existingOrder);
            return ResponseEntity.ok("Order Status updated successfully!");
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @Override
    public ResponseEntity<?> generatePDF(
            @RequestParam(required = false) Integer store_id,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) throws DocumentException {
        List<Bill> billlist;
        Date startDates = null;
        Date endDates = null;

        if (store_id != null) {
            // Fetch payments for a specific store ID
            billlist = billRepo.findByStoreIdAndDateRange(store_id, startDate, endDate);

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
            billlist = billRepo.findByStoreIdAndDateRange(store_id, startDate, endDate);
        } else {
            // If no date range is specified, retrieve all stores
            billlist = billRepo.findAll();
        }

        if (billlist.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(document, byteArrayOutputStream);

        document.open();

        Paragraph title = new Paragraph("BILL DETAILS", new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD));
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        Paragraph spacing = new Paragraph(" ");
        spacing.setSpacingAfter(10f);
        document.add(spacing);

        PdfPTable table = new PdfPTable(8);
        table.setWidthPercentage(100);

        PdfPCell cell = new PdfPCell(new Phrase("Sr No", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Order Date", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Order Type", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Order Placed By", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("table No", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Food List", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Order Status", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Payment mode", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        table.addCell(cell);

        int serialNumber = 1;
        for (Bill bill : billlist) {
            table.addCell(String.valueOf(serialNumber++));
            table.addCell(String.valueOf(bill.getBillDate()));

            List<Orders> orders = bill.getOrder();
            if (!orders.isEmpty()) {
                table.addCell(orders.get(0).getOrderType());
                table.addCell(orders.get(0).getCreatedName());
                table.addCell(orders.get(0).getTableNo());

                // Concatenate food names into a single string with line breaks
                List<OrderFood> orderFoods = orders.get(0).getOrderFoods();
                StringBuilder foodNames = new StringBuilder();
                for (OrderFood orderFood : orderFoods) {
                    foodNames.append(orderFood.getFoodName()).append("\n");
                }

                table.addCell(foodNames.toString().trim());  // Use trim to remove trailing newline
                table.addCell(orders.get(0).getOrderStatus());
            } else {
                table.addCell("");
                table.addCell("");
                table.addCell("");
                table.addCell("");
                table.addCell("");
            }

            table.addCell(bill.getPaymentMode());
        }


        document.add(table);

        document.close();

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=BILL_DETAILS.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(byteArrayInputStream));
    }


}
