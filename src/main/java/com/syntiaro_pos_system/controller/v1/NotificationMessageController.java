package com.syntiaro_pos_system.controller.v1;

import com.syntiaro_pos_system.entity.v1.NotificationMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/sys/notification")
@CrossOrigin(origins = "*", maxAge = 3600)
public interface NotificationMessageController {

    @PostMapping("/create")
    ResponseEntity<String> createNotificationMessage(@RequestBody NotificationMessage notificationMessage);

    @GetMapping("/last24")
    List<NotificationMessage> getNotificationMessagesLast24Hours();

    @PatchMapping("/update/{id}")
    ResponseEntity<String> updateNotificationMessage(@PathVariable Long id, @RequestBody NotificationMessage updatedMessage);

}
