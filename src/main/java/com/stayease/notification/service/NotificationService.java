package com.stayease.notification.service;

import com.stayease.notification.dto.NotificationRequest;
import com.stayease.notification.dto.NotificationResponse;
import com.stayease.notification.enums.NotificationStatus;

import java.util.List;

public interface NotificationService {

    NotificationResponse create(NotificationRequest request);

    List<NotificationResponse> getAll(Long userId, NotificationStatus status);

    NotificationResponse getById(Long id);

    NotificationResponse update(Long id, NotificationRequest request);

    void delete(Long id);
}
