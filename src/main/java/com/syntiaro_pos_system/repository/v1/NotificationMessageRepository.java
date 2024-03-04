package com.syntiaro_pos_system.repository.v1;

import com.syntiaro_pos_system.entity.v1.NotificationMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationMessageRepository extends JpaRepository<NotificationMessage, Long> {

    List<NotificationMessage> findByDateTimeGreaterThanEqualAndDateTimeLessThan(LocalDateTime start, LocalDateTime end);

    List<NotificationMessage> findByDateTimeAfter(LocalDateTime dateTime);

}
