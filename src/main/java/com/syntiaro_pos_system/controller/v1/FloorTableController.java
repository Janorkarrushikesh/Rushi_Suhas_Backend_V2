package com.syntiaro_pos_system.controller.v1;

import com.syntiaro_pos_system.entity.v1.FloorTable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/v1/sys/floortable")
public interface FloorTableController {


    @PostMapping("/addfloortable")
    ResponseEntity<?> addFloorTable(@RequestBody FloorTable floorTable);

    @PatchMapping("/updatetables/{serial_no}")
    ResponseEntity<FloorTable> updateFloorTable(@PathVariable("serial_no") Long serialNo, @RequestBody FloorTable floorTable);

    @GetMapping("/getTable/{serial_no}")
    Optional<FloorTable> gettablebyserial_no(@PathVariable Long serial_no);

    @GetMapping("/getTablebystoreid/{storeid}")
    List<FloorTable> gettablebystoreid(@PathVariable Long storeid);

    @DeleteMapping("/deleteTable/{serial_no}")
    ResponseEntity<String> deleteTableById(@PathVariable long serial_no);

    @PostMapping("/addfloor")
    ResponseEntity<?> createFloortable(@RequestBody FloorTable floorTable);

}
