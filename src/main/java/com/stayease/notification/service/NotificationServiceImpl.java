package com.stayease.notification.service;

import com.stayease.common.exception.ResourceNotFoundException;
import com.stayease.iam.service.UserService;
import com.stayease.notification.dto.NotificationRequest;
import com.stayease.notification.dto.NotificationResponse;
import com.stayease.notification.entity.Notification;
import com.stayease.notification.enums.NotificationStatus;
import com.stayease.notification.mapper.NotificationMapper;
import com.stayease.notification.repository.NotificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository repository;
    private final UserService userService;

    public NotificationServiceImpl(NotificationRepository repository, UserService userService) {
        this.repository = repository;
        this.userService = userService;
    }

    @Override
    public NotificationResponse create(NotificationRequest request) {
        ensureUserExists(request.userId());
        return NotificationMapper.toResponse(repository.save(NotificationMapper.toEntity(request)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationResponse> getAll(Long userId, NotificationStatus status) {
        List<Notification> list;
        if (userId != null && status != null) {
            list = repository.findByUserIdAndStatus(userId, status);
        } else if (userId != null) {
            list = repository.findByUserId(userId);
        } else {
            list = repository.findAll();
        }
        return list.stream().map(NotificationMapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public NotificationResponse getById(Long id) {
        return NotificationMapper.toResponse(findOrThrow(id));
    }

    @Override
    public NotificationResponse update(Long id, NotificationRequest request) {
        Notification entity = findOrThrow(id);
        ensureUserExists(request.userId());
        NotificationMapper.updateEntity(entity, request);
        return NotificationMapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        repository.delete(findOrThrow(id));
    }

    private Notification findOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Notification not found with id " + id));
    }

    private void ensureUserExists(Long userId) {
        if (!userService.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id " + userId);
        }
    }
}
