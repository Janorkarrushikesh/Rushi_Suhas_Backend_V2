package com.syntiaro_pos_system.controller.v2;

import com.syntiaro_pos_system.entity.v1.NotificationMessage;
import com.syntiaro_pos_system.entity.v2.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(path = "/v2/sys/notification/")
public interface NotificationController {


    @PostMapping("/")
    public ResponseEntity<ApiResponse> saveNotification(@RequestBody NotificationMessage notificationMessage);

    @GetMapping("/getall")
    public ResponseEntity<ApiResponse> allNotification();

    @PatchMapping("/id/{id}")
    public ResponseEntity<ApiResponse> updateById(@PathVariable Long id,@RequestBody NotificationMessage notificationMessage);
}
