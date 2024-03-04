package com.syntiaro_pos_system.serviceimpl.v2;

import com.syntiaro_pos_system.entity.v1.NotificationMessage;
import com.syntiaro_pos_system.entity.v2.ApiResponse;
import com.syntiaro_pos_system.repository.v2.NotificationRepository;
import com.syntiaro_pos_system.service.v2.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    NotificationRepository notificationRepository;

    @Override
    public ResponseEntity<ApiResponse> saveNotification(NotificationMessage notificationMessage) {
        return ResponseEntity.ok().body(new ApiResponse(notificationRepository.save(notificationMessage),true,200));
    }

    @Override
    public ResponseEntity<ApiResponse> allNotification() {
        try {
            List<NotificationMessage> notificationMessages = notificationRepository.findAll();
            // Get the current date and time
            LocalDateTime currentDateTime = LocalDateTime.now();
            LocalDateTime twentyFourHoursAgo = currentDateTime.minusHours(24);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            String formattedTwentyFourHoursAgo = twentyFourHoursAgo.format(formatter).trim();
            List<NotificationMessage> validNotificationMessages = new ArrayList<>();
            for (NotificationMessage message : notificationMessages) {
                String messageDateTimeStr = message.getEndDateTime().trim();
                LocalDateTime messageDateTime = LocalDateTime.parse(messageDateTimeStr, formatter);
                if (messageDateTime.isAfter(currentDateTime)) { validNotificationMessages.add(message); }
            }
            return ResponseEntity.ok().body(new ApiResponse(validNotificationMessages, true, 200));
        } catch (Exception e) {
            return ResponseEntity.ok().body(new ApiResponse(null, true,e.getMessage(), 200));
        }
    }



    @Override
    public ResponseEntity<ApiResponse> updatebyId(Long id, NotificationMessage notificationMessage) {
        try {

            Optional<NotificationMessage> existingMessage = notificationRepository.findById(id);

            if (existingMessage.isPresent()) {
                NotificationMessage existing = existingMessage.get();

                if (notificationMessage.getDateTime() != null) {
                    existing.setDateTime(notificationMessage.getDateTime());
                }
                if (notificationMessage.getEmail() != null) {
                    existing.setEmail(notificationMessage.getEmail());
                }
                if (notificationMessage.getMessage() != null) {
                    existing.setMessage(notificationMessage.getMessage());
                }
                if (notificationMessage.getSubject() != null) {
                    existing.setSubject(notificationMessage.getSubject());
                }
                return ResponseEntity.ok().body(new ApiResponse(notificationRepository.save(notificationMessage), true, "Notification message updated successfully.", 200));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(null, false, "Id Not Found.", 404));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(null, false, "...", 500));

        }

    }

}
