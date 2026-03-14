package com.finance.alert_engine.repository;

import com.finance.alert_engine.model.NotificationModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationModel, Integer> {
    
    // Find unread notifications
    List<NotificationModel> findByHasReadFalse();


    @Modifying
    @Transactional
    @Query("UPDATE NotificationModel n SET n.hasRead = true, n.lastReadAt = CURRENT_TIMESTAMP WHERE n.id IN :ids")
    int markAsReadByIds(@Param("ids") List<Integer> ids);

    // Find recent notifications

    // Custom query to mark all as read
    @Modifying
    @Transactional
    @Query("UPDATE NotificationModel n SET n.hasRead = true, n.lastReadAt = CURRENT_TIMESTAMP WHERE n.hasRead = false")
    int markAllAsRead();
    
    // Find notifications by title containing text
    List<NotificationModel> findByTitleContainingIgnoreCase(String title);
}