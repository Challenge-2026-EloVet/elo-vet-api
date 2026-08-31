package com.br.elovetapi.care.service;

import com.br.elovetapi.care.model.Notification;
import com.br.elovetapi.care.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public void createNotification(Long targetUserId, Long carePlanId, String type, String payload) {
        Notification n = new Notification();
        n.setTargetUserId(targetUserId);
        n.setCarePlanId(carePlanId);
        n.setType(type);
        n.setPayload(payload);
        n.setSentAt(LocalDateTime.now());
        notificationRepository.save(n);
    }
}