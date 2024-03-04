package com.syntiaro_pos_system.controllerimpl.v1;

import com.syntiaro_pos_system.controller.v1.BillController;
import com.syntiaro_pos_system.entity.v1.*;
import com.syntiaro_pos_system.repository.v1.BalanceRepository;
import com.syntiaro_pos_system.repository.v1.BillRepo;
import com.syntiaro_pos_system.repository.v1.OrderRepo;
import com.syntiaro_pos_system.request.v1.BillRequest;
import com.syntiaro_pos_system.service.v1.BillService;
import com.syntiaro_pos_system.service.v1.OrderService;
import com.syntiaro_pos_system.serviceimpl.v1.BalanceService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.print.*;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

@RestController
public class BillRestImpl implements BillController {
    @Autowired
    BillService billService;
    @Autowired
    OrderService orderService;

    @Autowired
    BalanceRepository balanceRepository;

    @Autowired
    private BillRepo billRepository;
    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private BalanceService balanceService;
    private FoodControllerIMPL targetOrder;

    public ResponseEntity<String> placebill(@RequestBody BillRequest request) {

        LocalDate today = LocalDate.now();
      //  Balance existingBalance = balanceRepository.findByStoreIdAndDate(request.getBill().getStoreId(), today);
        // check final closing status .
        // rushikesh
//        if(existingBalance.getFinalClosingBalance() != null){
//            return ResponseEntity.badRequest().body(" After Final closing you dont have access");
//        }

        List<OrderFood> orderFoodList = request.getOrderFood();
        LocalDate billdate = LocalDate.now();
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
        String formattedDate = dateFormat.format(date);
        String orddate = formattedDate;
        Integer lastBillNumber = billRepository.findLastBillNumberForStore(request.getBill().getStoreId() , billdate );
        request.getBill().setBillId(lastBillNumber != null ? lastBillNumber + 1 : 1);
        Integer lastBillNumbers = orderRepo.findLastOrderNumberForStore(request.getBill().getStoreId(),billdate);
        request.getBill().getOrder().get(0).setOrderId(lastBillNumbers != null ? lastBillNumbers + 1 : 1);


        if (orderFoodList != null && !orderFoodList.isEmpty()) {
            for (OrderFood orderFood : orderFoodList) {
                orderService.placeOrders(orderFood);
            }
        }
        billRepository.save(request.getBill());
        return ResponseEntity.ok().body("Bill Generated Succesfully..");
    }

    private void printBill(Bill bill, String printerName) {
        String billText = formatBillAsString(bill);
        try {
            System.out.println(billText);
            // Get an array of all available printers
            PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
            // Find the desired printer by name
            PrintService printService = Arrays.stream(printServices)
                    .filter(service -> service.getName().equalsIgnoreCase(printerName))
                    .findFirst()
                    .orElse(null);
            if (printService != null) {
                // Create a print job for the selected printer
                DocPrintJob job = printService.createPrintJob();
                // Use DocFlavor.INPUT_STREAM.AUTOSENSE instead of DocFlavor.STRING.TEXT_PLAIN
                DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
                Doc doc = new SimpleDoc(new ByteArrayInputStream(billText.getBytes()), flavor, null);
                // Set print attributes, e.g., number of copies
                PrintRequestAttributeSet attributes = new HashPrintRequestAttributeSet();
                attributes.add(new Copies(1));
                // Print the document using the selected printer
                job.print(doc, attributes);
            } else {
                System.out.println("Printer '" + printerName + "' not found.");
            }
        } catch (PrintException e) {
            // Handle the exception more gracefully, e.g., log it
            e.printStackTrace();
        }
    }

    private void printBillkot(Bill bill, String printerName) {
        String billText = formatBillAsStringkot(bill);
        try {
            System.out.println(billText);
            // Get an array of all available printers
            PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
            // Find the desired printer by name
            PrintService printService = Arrays.stream(printServices)
                    .filter(service -> service.getName().equalsIgnoreCase(printerName))
                    .findFirst()
                    .orElse(null);
            if (printService != null) {
                // Create a print job for the selected printer
                DocPrintJob job = printService.createPrintJob();
                // Use DocFlavor.INPUT_STREAM.AUTOSENSE instead of DocFlavor.STRING.TEXT_PLAIN
                DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
                Doc doc = new SimpleDoc(new ByteArrayInputStream(billText.getBytes()), flavor, null);
                // Set print attributes, e.g., number of copies
                PrintRequestAttributeSet attributes = new HashPrintRequestAttributeSet();
                attributes.add(new Copies(1));
                // Prin the document using the selected printer
                job.print(doc, attributes);
            } else {
                System.out.println("Printer '" + printerName + "' not found.");
            }
        } catch (PrintException e) {
            // Handle the exception more gracefully, e.g., log it
            e.printStackTrace();
        }
    }



    private String formatBillAsString(Bill bill) {
        // Format the bill details as a string
        // You can customize this method based on your bill structure
        StringBuilder billText = new StringBuilder();
        billText.append("Bill ID: ").append(bill.getBillId()).append("\n");
        billText.append("Contact: ").append(bill.getContact()).append("\n");
        billText.append("Payment Mode: ").append(bill.getPaymentMode()).append("\n");
        billText.append("GST: ").append(bill.getGst()).append("\n");
        billText.append("Total: ").append(bill.getTotal()).append("\n");
        billText.append("Store ID: ").append(bill.getStoreId()).append("\n");

        // Loop through orders
        List<Orders> orders = bill.getOrder();
        for (int i = 0; i < orders.size(); i++) {
            Orders order = orders.get(i);


            billText.append("Order ").append(i + 1).append(":\n");
            billText.append("Order Status: ").append(order.getOrderStatus()).append("\n");
            billText.append("Created By: ").append(order.getCreatedName()).append("\n");
            billText.append("Updated By: ").append(order.getUpdatedBy()).append("\n");
            billText.append("Table Number: ").append(order.getTableNo()).append("\n");

            // Loop through order foods
            List<OrderFood> orderFoods = order.getOrderFoods();
            for (int j = 0; j < orderFoods.size(); j++) {
                OrderFood orderFood = orderFoods.get(j);

                billText.append("+--------------------------------------------------+\n");
                billText.append(String.format("| %-15s | %-30s |\n", "Food " + (j + 1), ""));
                billText.append("+--------------------------------------------------+\n");
                billText.append(String.format("| %-15s | %-30s |\n", "Food Name", orderFood.getFoodName()));
                billText.append(String.format("| %-15s | %-30s |\n", "Price", orderFood.getPrice()));
                billText.append(String.format("| %-15s | %-30s |\n", "Quantity", orderFood.getQuantity()));
                billText.append(String.format("| %-15s | %-30s |\n", "Total", orderFood.getQuantity() * orderFood.getPrice()));
            }
            billText.append("+--------------------------------------------------+\n");
            billText.append("\n");
        }
        return billText.toString();
    }



    private String formatBillAsStringkot(Bill bill) {
        StringBuilder billText = new StringBuilder();
        billText.append("Bill ID: ").append(bill.getBillId()).append("\n");

        List<Orders> orders = bill.getOrder();
        for (Orders order : orders) {
            billText.append("Order Number: ").append(order.getOrderId()).append("\n");
            billText.append("Order Status: ").append(order.getOrderStatus()).append("\n");
            billText.append("Table Number: ").append(order.getTableNo()).append("\n");

            List<OrderFood> orderFoods = order.getOrderFoods();

            billText.append("+-----------------------------+\n");
            billText.append(String.format(" %-25s  %-5s  \n", "Name", "Qty"));
            billText.append("+-----------------------------+\n");

            for (OrderFood orderFood : orderFoods) {
                String limitedFoodName = limitString(orderFood.getFoodName(), 20);

                billText.append(String.format(" %-25s  %-5s  \n",
                        limitedFoodName,
                        orderFood.getQuantity()
                ));
            }
            billText.append("+-----------------------------+\n");
            billText.append("\n");
            billText.append("\n");
            //billText.append(" THANK YOU VISIT AGAIN !!!!");
//            billText.append("\n");
//            billText.append("\n");
//            billText.append("\n");
        }

        return billText.toString();
    }

    // Helper method to limit the string length
    private String limitString(String str, int maxLength) {
        if (str.length() <= maxLength) {
            return str;
        } else {
            return str.substring(0, maxLength);
        }
    }


    @Override
    public Bill placebilld(@RequestBody BillRequest request) {
        List<OrderFood> orderFoodList = request.getOrderFood();
        List<Object[]> lastBillNumber = billRepository.findMaxIdByDay(request.getBill().getStoreId());
        if (lastBillNumber != null && !lastBillNumber.isEmpty()) {
            Object[] result = lastBillNumber.get(1); // Assuming you expect one row in the result
            if (result != null && result.length > 1) {
                Integer maxId = (Integer) result[1]; // Assuming the maximum id is at index 1
                System.out.println(maxId);
                System.out.println(result);
                request.getBill().setBillId(maxId != null ? maxId + 1 : 1);
                request.getOrder().set(0,new Orders()).setOrderId(maxId != null ? maxId + 1 : 1);
            } else {
                request.getBill().setBillId(1); // Set to 1 if there are no previous records
            }
        } else {
            request.getBill().setBillId(1); // Set to 1 if there are no previous records
        }
        String id = request.getBill().getOrder().get(0).getStoreId() ;
        Integer lastBillNumbers = orderRepo.findLastNumberForStore(id);
        request.getBill().getOrder().get(0).setOrderId(lastBillNumbers != null ? lastBillNumbers + 1 : 1);
        if (orderFoodList != null && !orderFoodList.isEmpty()) {
            for (OrderFood orderFood : orderFoodList) {
                // Process each orderFood item as needed
                orderService.placeOrders(orderFood);
            }
        }
        return billRepository.save(request.getBill());
    }

    @Override
    public ResponseEntity<Bill> getLastBillByStoreIdd(@PathVariable("store_id") Integer storeId) {
        List<Bill> lastBillList = billRepository.findLastBillByStoreId(storeId, PageRequest.of(0, 1));

        // Retrieve the first (and only) element from the list if it's not empty
        Bill lastBill = lastBillList.isEmpty() ? null : lastBillList.get(0);

        return ResponseEntity.ok(lastBill);
    }



    @Override // For Update Data
    public Bill updateBill(@RequestBody Bill bill) {
        return this.billService.updateBill(bill);
    }
    @Override
    public ResponseEntity<HttpStatus> deletebill(@PathVariable String id) {
        try {
            this.billService.deletebill(Integer.parseInt(id));
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Override
    public Optional<Bill> getbillbyid(@PathVariable Integer id) {
        return this.billService.getbillbyid(id);

    }
    @Override // For Add Data
    public int saveBill(@RequestBody Bill bill) {
        int id = billService.addBill(bill);
        return id;
    }
    @Override
    public List<Bill> getBill() {
        return this.billService.getBill();
    }
    @Override
    public ResponseEntity<?> getBillById(@PathVariable Integer id) {
        Optional<Bill> bill = billService.getbillbyid(id);
        if (bill.isPresent()) {
            return ResponseEntity.ok(bill.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public List<Bill> getBillsByStoreId(@PathVariable Integer storeId) {
        return billService.getBillsByStoreId(storeId);
    }

    // THIS METHOD IS USE FOR FETCH BILL BY STOREID
    // this method added for filter last record  3 day  ---- by rushikesh

   @Override
    public List<Bill> pendingOrder(@PathVariable Integer storeId) {
        LocalDate DaysAgo = LocalDate.now().minusDays(3);
        return billRepository.findBillsByStoreAndStatusAndDate(storeId, DaysAgo);
    }

    // bug_04 this method added for filter kot  record  3 day  ---- by rushikesh
    @Override
    public List<Bill> kotOrder(@PathVariable Integer storeId) {
        LocalDate DaysAgo = LocalDate.now().minusDays(3);
        return billRepository.findBillsByStoreAndStatusAndDatekot(storeId, DaysAgo);
    }

    @Override
    public ResponseEntity<String> updateBill(@PathVariable("id") Integer id, @RequestBody Bill bill) {
        try {
            LocalDate today = LocalDate.now();
            Balance existingBalance = balanceRepository.findByStoreIdAndDate(bill.getStoreId(), today);
            if (existingBalance != null && existingBalance.getFinalClosingBalance() != null) {
                return ResponseEntity.badRequest().body("After Final closing you don't have access");
            }
            Bill updateBill = billService.updateBill(id, bill);
            if (updateBill != null) {
                LocalDate calculationDate = LocalDate.now(); // Get the calculation date from the bill
                billService.completeOrderAndPlaceBill(updateBill, calculationDate); // Complete order and place the bill
                // return new ResponseEntity<>(updateBill, HttpStatus.OK);
                return ResponseEntity.ok().body("Order Update Succesfully ....");

            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


    public Bill Qucikbill (@RequestBody BillRequest request) {
        List<OrderFood> orderFoodList = request.getOrderFood();
        LocalDate billdate = LocalDate.now();
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
        String formattedDate = dateFormat.format(date);
        String orddate = formattedDate;
        Integer lastBillNumber = billRepository.findLastBillNumberForStore(request.getBill().getStoreId() , billdate );
        request.getBill().setBillId(lastBillNumber != null ? lastBillNumber + 1 : 1);
        String id = request.getBill().getOrder().get(0).getStoreId() ;
        Integer lastBillNumbers = orderRepo.findLastOrderNumberForStore(request.getBill().getStoreId(),billdate);
        request.getBill().getOrder().get(0).setOrderId(lastBillNumbers != null ? lastBillNumbers + 1 : 1);
        if (orderFoodList != null && !orderFoodList.isEmpty()) {
            for (OrderFood orderFood : orderFoodList) {
                orderService.placeOrders(orderFood);
            }
        }
        return billRepository.save(request.getBill());
    }

    @Override
    public Bill patchbill(@RequestBody BillRequest request) {
        List<OrderFood> orderFoodList = request.getOrderFood();
        if (orderFoodList != null) {
            for (OrderFood orderFood : orderFoodList) {
                // Process each orderFood item as needed
                orderService.placeOrders(orderFood);
            }
        }
        //sendWhatsappMassage(request);
        return billRepository.save(request.getBill());
    }

    @Override
    public ResponseEntity<byte[]> generateExcelByStoreId(Integer store_id) { // Accept storeId as a parameter
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Bill Data");
            Row headerRow = sheet.createRow(0);
            String[] headerss = { "Bill Date", "Contact", "Paymentmod", "total", "Store ID" };
            for (int i = 0; i < headerss.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headerss[i]);
            }
            List<Bill> List = billRepository.findBySid(store_id); // Assuming you have a method to fetch food data by
            int rowNum = 1;
            for (Bill Lists : List) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(Lists.getBillDate() != null ? Lists.getBillDate().toString() : "");
                row.createCell(1).setCellValue(Lists.getContact() != null ? Lists.getContact() : "");
                row.createCell(2).setCellValue(Lists.getPaymentMode() != null ? Lists.getPaymentMode() : "");
                row.createCell(3).setCellValue(Lists.getTotal() != null ? Lists.getTotal() : 0.0);
                row.createCell(4).setCellValue(Lists.getDiscount() != null ? Lists.getDiscount() : 0.0);
            }
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "BIll_DATA.xlsx");
             return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(outputStream.toByteArray());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }



    @Override
    public ResponseEntity<Float> getTotalCashAmountByStoreIdAndToday(@PathVariable Integer storeId) {
        LocalDate today = LocalDate.now();
        Float totalCashAmount = billService.calculateTotalCashAmountByStoreIdAndDay(storeId, today);
        return ResponseEntity.ok(totalCashAmount);
    }

    @Override
    public ResponseEntity<Float> getTotalCardAmountByStoreIdAndToday(@PathVariable Integer storeId) {
        LocalDate today = LocalDate.now();
        Float totalCardAmount = billService.calculateTotalCardAmountByStoreIdAndDay(storeId, today);
        return ResponseEntity.ok(totalCardAmount);
    }
    @Override
    public ResponseEntity<Float> getTotalUpiAmountByStoreIdAndToday(@PathVariable Integer storeId) {
        LocalDate today = LocalDate.now();
        Float totalUpiAmount = billService.calculateTotalUpiAmountByStoreIdAndDay(storeId, today);
        return ResponseEntity.ok(totalUpiAmount);
    }

    @Override
    public ResponseEntity<Float> getTotalPaymentsByStoreIdAndToday(@PathVariable Integer storeId) {
        LocalDate today = LocalDate.now();
        Float totalCashAmount = billService.calculateTotalCashAmountByStoreIdAndDay(storeId, today);
        Float totalCardAmount = billService.calculateTotalCardAmountByStoreIdAndDay(storeId, today);
        Float totalUpiAmount = billService.calculateTotalUpiAmountByStoreIdAndDay(storeId, today);
        Float totalPayments = totalCashAmount + totalCardAmount + totalUpiAmount;
        return ResponseEntity.ok(totalPayments);
    }
    private String twilioAccountSid = "AC67689bd3ddd5cf78130c0c2a1c6530ff";
    private String twilioAuthToken = "44379fb1ee8c2e91ad560601bf52ee81";


    public void sendWhatsappMassage(@RequestBody BillRequest billRequest) {
        Twilio.init(twilioAccountSid, twilioAuthToken);
        Message message = Message.creator(
                new PhoneNumber("whatsapp:" + billRequest.getBill().getContact()),
                new PhoneNumber("whatsapp:" + "14155238886"),
                "Hi SYNTIARO SOLUTION PVT LTD, Deatils of your Total Bill is " + billRequest.getBill().getTotal() +
                        "\nThanks !!!!  Visit Again ..." + "\nFor Upi Payment  ="
                        + "upi://pay?pa=dkparmar78-1@okhdfcbank"

        ).create();
        String response = "WhatsApp message sent with SID: " + message.getSid();
        new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<byte[]> generatePdf(@PathVariable("billNo") Integer billNo) {
        try {

            Optional<Bill> billOptional = billRepository.findById(billNo);

            if (billOptional.isPresent()) {
                Bill bill = billOptional.get();
                PDDocument document = new PDDocument();
                PDPage page = new PDPage(PDRectangle.A4);
                document.addPage(page);
                PDPageContentStream contentStream = new PDPageContentStream(document, page);
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                contentStream.beginText();
                contentStream.newLineAtOffset(10, 800);
                contentStream.showText("Bill No: " + bill.getBillId());
                contentStream.newLineAtOffset(10, -15);
                contentStream.showText("Bill Date: " + bill.getBillDate());
                contentStream.newLineAtOffset(125, 0);
                contentStream.showText("Contact: " + bill.getContact());
                contentStream.endText();
                contentStream.close();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                document.save(byteArrayOutputStream);
                document.close();
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_PDF);
                headers.setContentDispositionFormData("filename", "bill_" + bill.getBillId() + ".pdf");

                return ResponseEntity.ok()
                        .headers(headers)
                        .contentLength(byteArrayOutputStream.size())
                        .body(byteArrayOutputStream.toByteArray());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @Override
    public ResponseEntity<List<Bill>> getDailyBalanceReport(@PathVariable(name = "store_id") Integer storeId) {
        try {
            List<Bill> dailyBalanceReportList = new ArrayList<>();
            List<Bill> allBills = billService.getBillsByStoreId(storeId);
            Map<LocalDate, Bill> dailyBalances = new HashMap<>();
            for (Bill bill : allBills) {
                LocalDate billDate = bill.getBillDate();
                Float totalAmount = bill.getTotal();
                String paymentMode = bill.getPaymentMode();
                if ("cash".equalsIgnoreCase(paymentMode)) {
                    if (dailyBalances.containsKey(billDate)) {
                        Bill dailyBalanceReport = dailyBalances.get(billDate);
                        dailyBalanceReport.setTotal(dailyBalanceReport.getTotal() + totalAmount);
                        System.out.println(dailyBalanceReport);
                    } else {
                        dailyBalances.put(billDate, new Bill(billDate, totalAmount, paymentMode, storeId));
                    }
                }
            }
            dailyBalanceReportList.addAll(dailyBalances.values());
            return ResponseEntity.ok(dailyBalanceReportList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<List<Bill>> getDailyBalanceReportCard(@PathVariable(name = "store_id") Integer storeId) {
        try {
            List<Bill> dailyBalanceReportList = new ArrayList<>();
            List<Bill> allBills = billService.getBillsByStoreId(storeId);
            Map<LocalDate, Bill> dailyBalances = new HashMap<>();
            for (Bill bill : allBills) {
                LocalDate billDate = bill.getBillDate();
                Float totalAmount = bill.getTotal();
                String paymentMode = bill.getPaymentMode();
                if ("card".equalsIgnoreCase(paymentMode)) {
                    if (dailyBalances.containsKey(billDate)) {
                        Bill dailyBalanceReport = dailyBalances.get(billDate);
                        dailyBalanceReport.setTotal(dailyBalanceReport.getTotal() + totalAmount);
                    } else {
                        dailyBalances.put(billDate, new Bill(billDate, totalAmount, paymentMode, storeId));
                    }
                }
            }
            dailyBalanceReportList.addAll(dailyBalances.values());
            return ResponseEntity.ok(dailyBalanceReportList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @Override
    public ResponseEntity<List<Bill>> getDailyBalanceReportUpi(@PathVariable(name = "store_id") Integer storeId) {
        try {

            List<Bill> dailyBalanceReportList = new ArrayList<>();
            List<Bill> allBills = billService.getBillsByStoreId(storeId);
            Map<LocalDate, Bill> dailyBalances = new HashMap<>();
            for (Bill bill : allBills) {
                LocalDate billDate = bill.getBillDate();
                Float totalAmount = bill.getTotal();
                String paymentMode = bill.getPaymentMode();
                if ("upi".equalsIgnoreCase(paymentMode)) {
                    if (dailyBalances.containsKey(billDate)) {
                        Bill dailyBalanceReport = dailyBalances.get(billDate);
                        dailyBalanceReport.setTotal(dailyBalanceReport.getTotal() + totalAmount);
                    } else {
                        dailyBalances.put(billDate, new Bill(billDate, totalAmount, paymentMode, storeId));
                    }
                }
            }
            dailyBalanceReportList.addAll(dailyBalances.values());
            return ResponseEntity.ok(dailyBalanceReportList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

//    @GetMapping("/daily-upi-balance-report/")
//    public ResponseEntity<List<Bill>> getDailyBalanceReportUpi(
//            @RequestParam(required = false) Integer storeId,
//            @RequestParam(required = false)  String startDate,
//            @RequestParam(required = false) String endDate) throws DocumentException {
//
//        try {
//            List<Bill> dailyBalanceReportList = new ArrayList<>();
//            List<Bill> allBills = billRepository.findByStoreIdAndDateRangee(storeId, startDate, endDate);
//            Map<LocalDate, Bill> dailyBalances = new HashMap<>();
//
//            for (Bill bill : allBills) {
//                LocalDate billDate = bill.getBilldate();
//                Float totalAmount = bill.getTotal();
//                String paymentMode = bill.getPaymentmode();
//
//                if ("upi".equalsIgnoreCase(paymentMode)) {
//                    if (dailyBalances.containsKey(billDate)) {
//                        Bill dailyBalanceReport = dailyBalances.get(billDate);
//                        dailyBalanceReport.setTotal(dailyBalanceReport.getTotal() + totalAmount);
//                    } else {
//                        dailyBalances.put(billDate, new Bill(billDate, totalAmount, paymentMode, storeId));
//                    }
//                }
//            }
//
//            dailyBalanceReportList.addAll(dailyBalances.values());
//            return ResponseEntity.ok(dailyBalanceReportList);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }




    @Override
    public ResponseEntity<String> createBill(@RequestBody BillRequest request) {
        List<OrderFood> orderFoodList = request.getOrderFood();
        LocalDate billDate = LocalDate.now();

        LocalDate today = LocalDate.now();
        Balance existingBalance = balanceRepository.findByStoreIdAndDate(request.getBill().getStoreId(), today);
        if (existingBalance != null && existingBalance.getFinalClosingBalance() != null) {
            return ResponseEntity.badRequest().body("After Final closing you don't have access");
        }

        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
        String formattedDate = dateFormat.format(date);

        String orderDate = formattedDate;

        Integer lastBillNumber = billRepository.findLastBillNumberForStore(request.getBill().getStoreId(), billDate);
        request.getBill().setBillId(lastBillNumber != null ? lastBillNumber + 1 : 1);

        String orderId = request.getBill().getOrder().get(0).getStoreId();
        Integer lastOrderNumber = orderRepo.findLastOrderNumberForStore(request.getBill().getStoreId(), billDate);
        request.getBill().getOrder().get(0).setOrderId(lastOrderNumber != null ? lastOrderNumber + 1 : 1);

        if (orderFoodList != null && !orderFoodList.isEmpty()) {
            for (OrderFood orderFood : orderFoodList) {
                // Process each orderFood item as needed
                orderService.placeOrders(orderFood);
            }
        }

        Bill savedBill = billRepository.save(request.getBill());

        System.out.println(savedBill);

        // Complete order and place the bill
        completeOrderAndPlaceBill(savedBill, billDate);
       // printBillkot(request.getBill() , "POS58 Printer(2)11");
        return ResponseEntity.ok(String.valueOf(savedBill));

    }


    public void completeOrderAndPlaceBill(Bill bill, LocalDate calculationDate) {
        // Logic to mark order as completed and place the bill

        // Check if the payment method is "cash"
        if (bill.getPaymentMode().equalsIgnoreCase("cash")) {
            // Calculate bill amount
            Double billAmount = Double.valueOf(bill.getTotal());

            if (billAmount == null) {
                billAmount = 0.0; // You can choose a suitable default value
            }

            Balance balance = balanceRepository.findByStoreIdAndDate(bill.getStoreId(), calculationDate);

            if (balance == null) {
                balance = new Balance();
                balance.setStoreId(bill.getStoreId());
                balance.setDate(calculationDate);
                balance.setTodays_Opening_Balance(0.0);
                balance.setRemainingBalance(billAmount);
            } else {
                Double currentBalance = balance.getRemainingBalance();

                // Update closing balance by adding bill amount
                Double updatedBalance = currentBalance + billAmount;
                balance.setRemainingBalance(updatedBalance);
            }

            // Save the balance in the database (create new or update existing)
            balanceRepository.save(balance);

            // Now you can display the updated closing balance
            System.out.println("Updated Closing Balance for Store " + balance.getStoreId() + " on " + calculationDate + ": " + balance.getRemainingBalance());
        } else {
            System.out.println("Payment method is not 'cash', skipping balance update.");
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
            billlist = billRepository.findByStoreIdAndDateRange(store_id, startDate, endDate);

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
            billlist = billRepository.findByStoreIdAndDateRange(store_id, startDate, endDate);
        } else {
            // If no date range is specified, retrieve all stores
            billlist = billRepository.findAll();
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

        PdfPTable table = new PdfPTable(7);
        table.setWidthPercentage(100);

        PdfPCell cell = new PdfPCell(new Phrase("Sr No", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Bill Date", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Table No", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Order Type", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Payment Mode", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Total", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Discount", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        table.addCell(cell);
        int serialNumber = 1;

        for (Bill bill : billlist) {
            table.addCell(String.valueOf(serialNumber++));
            table.addCell(String.valueOf(bill.getBillDate()));

            List<Orders> orders = bill.getOrder();
            if (!orders.isEmpty()) {
                table.addCell(orders.get(0).getTableNo());
                table.addCell(orders.get(0).getOrderType());
            } else {
                table.addCell("");
                table.addCell("");
            }

            table.addCell(bill.getPaymentMode());
            table.addCell(String.valueOf(bill.getTotal()));
            table.addCell(String.valueOf(bill.getDiscount()));
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





    @Override
    public ResponseEntity<byte[]> generateExcel(
            @RequestParam(required = false) Integer store_id,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        List<Bill> billList;

        if (store_id != null) {
            // Fetch bills for a specific store ID and date range
            billList = billRepository.findByStoreIdAndBillDateBetween(store_id, startDate, endDate);
        } else if (startDate != null && endDate != null) {
            // Fetch bills based on the date range
            billList = billRepository.findByStoreIdAndBillDateBetween(store_id, startDate, endDate);
        } else {
            // If no specific store or date range is specified, retrieve all bills
            billList = billRepository.findByStoreIdAndBillDateBetween(store_id, startDate, endDate);
        }

        if (billList.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Bill Data");
            Row headerRow = sheet.createRow(0);
            String[] headers = {"Bill No", "Bill Date", "Table No", "Order Type", "Payment Mode", "Total", "Discount"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }

            int rowNum = 1;
            for (Bill bill : billList) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(String.valueOf(bill.getBillId()));
                row.createCell(1).setCellValue(String.valueOf(bill.getBillDate()));

                List<Orders> orders = bill.getOrder();
                if (!orders.isEmpty()) {
                    row.createCell(2).setCellValue(orders.get(0).getTableNo());
                    row.createCell(3).setCellValue(orders.get(0).getOrderType());
                } else {
                    row.createCell(2).setCellValue("");
                    row.createCell(3).setCellValue("");
                }
                row.createCell(4).setCellValue(bill.getPaymentMode());
                row.createCell(5).setCellValue(String.valueOf(bill.getTotal()));
                row.createCell(6).setCellValue(String.valueOf(bill.getDiscount()));
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            HttpHeaders headerss = new HttpHeaders();
            headerss.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headerss.setContentDispositionFormData("attachment", "BILL_DETAILS.xlsx");

            return ResponseEntity.ok()
                    .headers(headerss)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(outputStream.toByteArray());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // No of cells increased by 1 .
    @Override
    public ResponseEntity<byte[]> getTotalPaymentGeneratePdf(
            @RequestParam Integer storeId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) throws DocumentException {

        Map<LocalDate, Map<String, Float>> totalAmountMap = new TreeMap<>();

        // Fetch total cash amount
        List<Object[]> cashResult = billRepository.calculateTotalCashAmountByStoreIdAndDates(storeId, startDate, endDate);
        for (Object[] row : cashResult) {
            LocalDate billDate = (LocalDate) row[0];
            Float totalCashAmount = ((Number) row[1]).floatValue();

            totalAmountMap.computeIfAbsent(billDate, k -> new HashMap<>()).put("cash", totalCashAmount);
        }
        // Fetch total UPI amount
        List<Object[]> upiResult = billRepository.calculateTotalupiAmountByStoreIdAndDates(storeId, startDate, endDate);
        for (Object[] row : upiResult) {
            LocalDate billDate = (LocalDate) row[0];
            Float totalUPIAmount = ((Number) row[1]).floatValue();
            totalAmountMap.computeIfAbsent(billDate, k -> new HashMap<>()).put("upi", totalUPIAmount);
        }

        // Fetch total card amount
        List<Object[]> cardResult = billRepository.calculateTotalcardAmountByStoreIdAndDates(storeId, startDate, endDate);
        for (Object[] row : cardResult) {
            LocalDate billDate = (LocalDate) row[0];
            Float totalCardAmount = ((Number) row[1]).floatValue();

            totalAmountMap.computeIfAbsent(billDate, k -> new HashMap<>()).put("card", totalCardAmount);
        }

        // Create a PDF document
        Document document = new Document();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, byteArrayOutputStream);
            document.open();

            Paragraph title = new Paragraph("DAILY INCOME REPORT", new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD));
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            Paragraph spacing = new Paragraph(" ");
            spacing.setSpacingAfter(10f);
            document.add(spacing);

            // Create a single table for all payment details
            PdfPTable table = new PdfPTable(5); // No of cells increased by 1 .

            // Add headers to the table
            table.addCell("Sr No");
            table.addCell("Date");
            table.addCell("Cash");
            table.addCell("UPI");
            table.addCell("Card");

            // Sort entries by date
            List<Map.Entry<LocalDate, Map<String, Float>>> sortedEntries = new ArrayList<>(totalAmountMap.entrySet());
            sortedEntries.sort(Map.Entry.comparingByKey());

            int serialNumber = 1;

            // Add content to the PDF
            for (Map.Entry<LocalDate, Map<String, Float>> entry : sortedEntries) {
                LocalDate billDate = entry.getKey();
                Map<String, Float> amounts = entry.getValue();
                table.addCell(String.valueOf(serialNumber++));

                // Format the date
                String formattedDate = billDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
               // Add date to the row
                table.addCell(formattedDate);
                // Add payment details to the table
                table.addCell(amounts.getOrDefault("cash", 0.0f).toString());
                table.addCell(amounts.getOrDefault("upi", 0.0f).toString());
                table.addCell(amounts.getOrDefault("card", 0.0f).toString());
            }

            // Add the table to the document
            document.add(table);

            document.close();

            // Convert the PDF content to byte array
            byte[] pdfContent = byteArrayOutputStream.toByteArray();

            // Set headers for the response
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("inline", "report.pdf");

            return new ResponseEntity<>(pdfContent, headers, HttpStatus.OK);

        } catch (DocumentException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Override
    public ResponseEntity<?> generatePDFbyStoreid(
            @PathVariable Integer store_id) throws DocumentException {
        List<Bill> billlist;
        Date startDates = null;
        Date endDates = null;

        if (store_id != null) {
            // Fetch payments for a specific store ID
            billlist = billRepository.findBySid(store_id);

        } else {
            // If no date range is specified, retrieve all stores
            billlist = billRepository.findAll();
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

        PdfPTable table = new PdfPTable(7);
        table.setWidthPercentage(100);

        PdfPCell cell = new PdfPCell(new Phrase("Sr No", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Bill Date", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Table No", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Order Type", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Payment Mode", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Total", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Discount", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        table.addCell(cell);
        int serialNumber = 1;
        for (Bill bill : billlist) {
            table.addCell(String.valueOf(serialNumber++));
            table.addCell(String.valueOf(bill.getBillDate()));

            List<Orders> orders = bill.getOrder();
            if (!orders.isEmpty()) {
                table.addCell(orders.get(0).getTableNo());
                table.addCell(orders.get(0).getOrderType());
            } else {
                table.addCell("");
                table.addCell("");
            }

            table.addCell(bill.getPaymentMode());
            table.addCell(String.valueOf(bill.getTotal()));
            table.addCell(String.valueOf(bill.getDiscount()));
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




    // No of cells increased by 1 .
    @Override
    public ResponseEntity<byte[]> getTotalPaymentGeneratePdfbyStoreid( @PathVariable Integer storeId) throws DocumentException {

        Map<LocalDate, Map<String, Float>> totalAmountMap = new TreeMap<>();

        // Fetch total cash amount
        List<Object[]> cashResult = billRepository.calculateTotalCashAmountByStoreId(storeId);
        for (Object[] row : cashResult) {
            LocalDate billDate = (LocalDate) row[0];
            Float totalCashAmount = ((Number) row[1]).floatValue();

            totalAmountMap.computeIfAbsent(billDate, k -> new HashMap<>()).put("cash", totalCashAmount);
        }

        // Fetch total UPI amount
        List<Object[]> upiResult = billRepository.calculateTotalupiAmountByStoreId(storeId);
        for (Object[] row : upiResult) {
            LocalDate billDate = (LocalDate) row[0];
            Float totalUPIAmount = ((Number) row[1]).floatValue();

            totalAmountMap.computeIfAbsent(billDate, k -> new HashMap<>()).put("upi", totalUPIAmount);
        }

        // Fetch total card amount
        List<Object[]> cardResult = billRepository.calculateTotalcardAmountByStoreId(storeId);
        for (Object[] row : cardResult) {
            LocalDate billDate = (LocalDate) row[0];
            Float totalCardAmount = ((Number) row[1]).floatValue();

            totalAmountMap.computeIfAbsent(billDate, k -> new HashMap<>()).put("card", totalCardAmount);
        }

        // Create a PDF document
        Document document = new Document();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, byteArrayOutputStream);
            document.open();

            Paragraph title = new Paragraph("DAILY INCOME REPORT", new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD));
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            Paragraph spacing = new Paragraph(" ");
            spacing.setSpacingAfter(10f);
            document.add(spacing);

            // Create a single table for all payment details
            PdfPTable table = new PdfPTable(5); // No of cells increased by 1 .

            // Add headers to the table
            table.addCell("Sr No");
            table.addCell("Date");
            table.addCell("Cash");
            table.addCell("UPI");
            table.addCell("Card");

            // Sort entries by date
            List<Map.Entry<LocalDate, Map<String, Float>>> sortedEntries = new ArrayList<>(totalAmountMap.entrySet());
            sortedEntries.sort(Map.Entry.comparingByKey());
            int serialNumber = 1;

            // Add content to the PDF
            for (Map.Entry<LocalDate, Map<String, Float>> entry : sortedEntries) {
                LocalDate billDate = entry.getKey();
                Map<String, Float> amounts = entry.getValue();
// Add payment details to the table
                table.addCell(String.valueOf(serialNumber++));
                // Format the date
                String formattedDate = billDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                // Add date to the row
                table.addCell(formattedDate);


                table.addCell(amounts.getOrDefault("cash", 0.0f).toString());
                table.addCell(amounts.getOrDefault("upi", 0.0f).toString());
                table.addCell(amounts.getOrDefault("card", 0.0f).toString());
            }

            // Add the table to the document
            document.add(table);

            document.close();

            // Convert the PDF content to byte array
            byte[] pdfContent = byteArrayOutputStream.toByteArray();

            // Set headers for the response
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("inline", "report.pdf");

            return new ResponseEntity<>(pdfContent, headers, HttpStatus.OK);

        } catch (DocumentException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<List<Bill>> getBillsByStoreId(
            @RequestParam(name = "storeId") Integer storeId,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Bill> result = billRepository.findBillsByStoreId(storeId, pageable);

        List<Bill> bills = result.getContent();
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(result.getTotalElements()));

        return new ResponseEntity<>(bills, headers, HttpStatus.OK);
    }

    // Rushikesh ADD NEW CODE
    // billreport pagination code     ---- by rushikesh
    @Override
    public ResponseEntity<ApiResponse> getBillsReport(
            @PathVariable(name = "storeId") Integer storeId,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "20") Integer size) {

        ApiResponse apiResponse=null;
        try {
            apiResponse =  billService.getBillsReport(storeId,page,size);
            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            // Log the exception or handle it appropriately
            apiResponse = new ApiResponse(null,false,500,"something went wrong!");
            return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}

