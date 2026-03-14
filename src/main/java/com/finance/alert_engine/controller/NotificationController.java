package com.finance.alert_engine.controller;

import com.finance.alert_engine.custom.response.ResponseBuilder;
import com.finance.alert_engine.dto.request.NotificationUpdateRequest;
import com.finance.alert_engine.model.NotificationModel;
import com.finance.alert_engine.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notification")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/fetch-unread")
    public ResponseBuilder<List<NotificationModel>> fetchUnreadMessages() {
        return ResponseBuilder.success(notificationService.fetchUnreadMessages());
    }

    @PostMapping("/update-status")
    public ResponseBuilder<String> updateStatus(@RequestBody NotificationUpdateRequest notificationUpdateRequest) {
        notificationService.updateStatus(notificationUpdateRequest);
        return ResponseBuilder.success("SUCCESS");
    }
}
