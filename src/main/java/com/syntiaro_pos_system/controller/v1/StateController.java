package com.syntiaro_pos_system.controller.v1;

import com.syntiaro_pos_system.entity.v1.State;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/v1/sys/states")
@CrossOrigin(origins = "*", maxAge = 3600)
public interface StateController {

    @GetMapping("/getallstates")
    public List<State> getAllStates() ;

    @GetMapping("/{id}")
    public ResponseEntity<State> getStateById(@PathVariable Long id) ;
    @PostMapping
    public ResponseEntity<State> createState(@RequestBody State state) ;


}
