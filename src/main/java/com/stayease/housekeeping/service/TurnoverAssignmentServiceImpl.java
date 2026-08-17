package com.stayease.housekeeping.service;

import com.stayease.booking.service.ReservationService;
import com.stayease.common.exception.ResourceNotFoundException;
import com.stayease.housekeeping.dto.TurnoverAssignmentRequest;
import com.stayease.housekeeping.dto.TurnoverAssignmentResponse;
import com.stayease.housekeeping.entity.TurnoverAssignment;
import com.stayease.housekeeping.mapper.TurnoverAssignmentMapper;
import com.stayease.housekeeping.repository.TurnoverAssignmentRepository;
import com.stayease.iam.service.UserService;
import com.stayease.property.service.PropertyService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TurnoverAssignmentServiceImpl implements TurnoverAssignmentService {

    private final TurnoverAssignmentRepository repository;
    private final PropertyService propertyService;
    private final UserService userService;
    private final ReservationService reservationService;

    public TurnoverAssignmentServiceImpl(TurnoverAssignmentRepository repository,
                                         PropertyService propertyService,
                                         UserService userService,
                                         ReservationService reservationService) {
        this.repository = repository;
        this.propertyService = propertyService;
        this.userService = userService;
        this.reservationService = reservationService;
    }

    @Override
    public TurnoverAssignmentResponse create(TurnoverAssignmentRequest request) {
        validateReferences(request);
        return TurnoverAssignmentMapper.toResponse(
                repository.save(TurnoverAssignmentMapper.toEntity(request)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TurnoverAssignmentResponse> getAll(Long propertyId, Long assignedToId) {
        List<TurnoverAssignment> list;
        if (propertyId != null) {
            list = repository.findByPropertyId(propertyId);
        } else if (assignedToId != null) {
            list = repository.findByAssignedToId(assignedToId);
        } else {
            list = repository.findAll();
        }
        return list.stream()
                .map(t -> TurnoverAssignmentMapper.toResponse(t))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public TurnoverAssignmentResponse getById(Long id) {
        return TurnoverAssignmentMapper.toResponse(findOrThrow(id));
    }

    @Override
    public TurnoverAssignmentResponse update(Long id, TurnoverAssignmentRequest request) {
        TurnoverAssignment entity = findOrThrow(id);
        validateReferences(request);
        TurnoverAssignmentMapper.updateEntity(entity, request);
        return TurnoverAssignmentMapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        repository.delete(findOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return id != null && repository.existsById(id);
    }

    private TurnoverAssignment findOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Turnover assignment not found with id " + id));
    }

    private void validateReferences(TurnoverAssignmentRequest request) {
        if (!propertyService.existsById(request.propertyId())) {
            throw new ResourceNotFoundException("Property not found with id " + request.propertyId());
        }
        if (request.assignedToId() != null && !userService.existsById(request.assignedToId())) {
            throw new ResourceNotFoundException("Assignee (user) not found with id " + request.assignedToId());
        }
        if (request.checkOutReservationId() != null
                && !reservationService.existsById(request.checkOutReservationId())) {
            throw new ResourceNotFoundException(
                    "Check-out reservation not found with id " + request.checkOutReservationId());
        }
        if (request.checkInReservationId() != null
                && !reservationService.existsById(request.checkInReservationId())) {
            throw new ResourceNotFoundException(
                    "Check-in reservation not found with id " + request.checkInReservationId());
        }
    }
}
