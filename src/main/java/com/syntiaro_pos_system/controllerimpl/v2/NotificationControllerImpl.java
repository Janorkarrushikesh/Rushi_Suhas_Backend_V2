package com.syntiaro_pos_system.controllerimpl.v2;

import com.syntiaro_pos_system.controller.v2.NotificationController;
import com.syntiaro_pos_system.entity.v1.NotificationMessage;
import com.syntiaro_pos_system.entity.v2.ApiResponse;
import com.syntiaro_pos_system.service.v2.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NotificationControllerImpl implements NotificationController {

    @Autowired
    NotificationService notificationService;

    @Override
    public ResponseEntity<ApiResponse> saveNotification(NotificationMessage notificationMessage) {
        return notificationService.saveNotification(notificationMessage);
    }

    @Override
    public ResponseEntity<ApiResponse> allNotification() {
        return notificationService.allNotification();
    }

    @Override
    public ResponseEntity<ApiResponse> updateById(Long id, NotificationMessage notificationMessage) {
        return notificationService.updatebyId(id, notificationMessage);
    }
}
