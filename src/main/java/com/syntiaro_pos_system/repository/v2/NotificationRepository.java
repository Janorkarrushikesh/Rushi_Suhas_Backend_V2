package com.syntiaro_pos_system.repository.v2;

import com.syntiaro_pos_system.entity.v1.NotificationMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<NotificationMessage,Long> {
}
