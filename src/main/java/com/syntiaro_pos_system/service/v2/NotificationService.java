package com.syntiaro_pos_system.service.v2;

import com.syntiaro_pos_system.entity.v1.NotificationMessage;
import com.syntiaro_pos_system.entity.v2.ApiResponse;
import org.springframework.http.ResponseEntity;


public interface NotificationService {


    ResponseEntity<ApiResponse> saveNotification(NotificationMessage notificationMessage);

    ResponseEntity<ApiResponse> allNotification();

    ResponseEntity<ApiResponse> updatebyId(Long id, NotificationMessage notificationMessage);
}
