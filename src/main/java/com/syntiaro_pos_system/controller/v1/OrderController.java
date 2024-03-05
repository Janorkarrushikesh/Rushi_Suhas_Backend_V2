package com.syntiaro_pos_system.controller.v1;

import com.itextpdf.text.DocumentException;
import com.syntiaro_pos_system.entity.v1.Orders;
import com.syntiaro_pos_system.request.v1.OrderRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/v1/sys/order")
@CrossOrigin(origins = "*", maxAge = 3600)
public interface OrderController {

    @PostMapping(path = "/postorder")
    Orders placeOrder(@RequestBody OrderRequest request);

    // THIS METHOD IS USE FOR UPDATE ORDER
    @PutMapping(path = "/updateorder")
    Orders updateOrder(@RequestBody Orders order);

    // THIS METHOD IS USE FOR DELETE ORDER
    @DeleteMapping(path = "/orders/{oid}")
    ResponseEntity<HttpStatus> deleteorder(@PathVariable String oid);

    // THIS METHOD IS USE FOR GET ALL LIST OF ORDER
    @GetMapping(path = "/getorder")
    List<Orders> getorder();

    // THIS METHOD IS USE FOR GET ORDER BY ID
    @GetMapping(path = "/order/{orderid}")
    Optional<Orders> getorderbyid(@PathVariable Integer orderid);

    // THIS METHOD IS USE FOR GET ORDER BY STOREID
    @GetMapping("/bystore/{storeId}")
    List<Orders> getOrdersByStoreId(@PathVariable("storeId") String storeId);

    // THIS METHOD IS USE FOR POST ORDER
    @PostMapping(path = "/postorders")
    int addOrder(@RequestBody Orders order);

    // THIS METHOD IS USE FOR GET ORDER BY ID
    @GetMapping(path = "/getOrderByID/{oid}")
    ResponseEntity<Orders> fetchorderbyid(@PathVariable Integer oid);

    // THIS METHOD IS USE FOR UPDATE ORDER
    @PatchMapping(path = "/updateorder/{orderid}")
    ResponseEntity<?> updateOrderPartial(@PathVariable String orderid, @RequestBody OrderRequest request);


    @PatchMapping(path = "/updateorderStatus/{Serial_no}")
    ResponseEntity<?> updateOrderStatus(@PathVariable Integer Serial_no);

    @PatchMapping(path = "/updateorderStatusto/{Serial_no}")
    ResponseEntity<?> updateOrderStatus2(@PathVariable Integer Serial_no);


    @PostMapping("/generate-pdf-order/")
    ResponseEntity<?> generatePDF(
            @RequestParam(required = false) Integer store_id,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) throws DocumentException;
}
