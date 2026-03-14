package com.finance.alert_engine.service;

import com.finance.alert_engine.dto.request.NotificationUpdateRequest;
import com.finance.alert_engine.model.NotificationModel;
import com.finance.alert_engine.repository.NotificationRepository;
import com.finance.alert_engine.util.date.DateUtilz;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {
    
    private final NotificationRepository notificationRepository;
//    private final TelegramComponent telegramBot;
    
    // Create notification from Telegram message
    public NotificationModel createFromTelegram(String title, String value, String extra, long timestamp) {
        NotificationModel notification = new NotificationModel();
        notification.setTitle(title);
        notification.setValue(value);
        notification.setExtra(extra);
        notification.setSentAt(DateUtilz.fromUnixSeconds(timestamp));
        notification.setHasRead(false);
        
        return notificationRepository.save(notification);
    }

    public List<NotificationModel> fetchUnreadMessages() {
        return notificationRepository.findByHasReadFalse();
    }

    public void updateStatus(NotificationUpdateRequest notificationUpdateRequest) {
        log.info("Fetching notifications with IDs: {}", notificationUpdateRequest.getMessageIds());

        if (notificationUpdateRequest.getMessageIds() == null || notificationUpdateRequest.getMessageIds().isEmpty()) {
            return;
        }

         notificationRepository.markAsReadByIds(notificationUpdateRequest.getMessageIds());
    }
    // Send notification to Telegram and save
//    public void sendAndSaveNotification(Long chatId, String title, String message) {
//        try {
//            // Send to Telegram
//            SendMessage telegramMessage = new SendMessage();
//            telegramMessage.setChatId(chatId.toString());
//            telegramMessage.setText("🔔 " + title + "\n\n" + message);
//            telegramBot.execute(telegramMessage);
//
//            // Save to database
//            NotificationModel notification = new NotificationModel();
//            notification.setTitle(title);
//            notification.setValue(message);
//            notification.setHasRead(false);
//            notificationRepository.save(notification);
//
//            log.info("Notification sent and saved: {}", title);
//        } catch (TelegramApiException e) {
//            log.error("Failed to send notification: {}", e.getMessage());
//        }
//    }
}