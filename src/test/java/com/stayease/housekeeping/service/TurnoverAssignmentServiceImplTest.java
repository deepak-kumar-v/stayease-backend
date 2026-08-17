package com.stayease.housekeeping.service;

import com.stayease.booking.service.ReservationService;
import com.stayease.common.exception.ResourceNotFoundException;
import com.stayease.housekeeping.dto.TurnoverAssignmentRequest;
import com.stayease.housekeeping.dto.TurnoverAssignmentResponse;
import com.stayease.housekeeping.entity.TurnoverAssignment;
import com.stayease.housekeeping.enums.TurnoverStatus;
import com.stayease.housekeeping.repository.TurnoverAssignmentRepository;
import com.stayease.iam.service.UserService;
import com.stayease.property.service.PropertyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TurnoverAssignmentServiceImplTest {

    @Mock
    private TurnoverAssignmentRepository repository;

    @Mock
    private PropertyService propertyService;

    @Mock
    private UserService userService;

    @Mock
    private ReservationService reservationService;

    @InjectMocks
    private TurnoverAssignmentServiceImpl service;

    private TurnoverAssignmentRequest sampleRequest;
    private TurnoverAssignment sampleEntity;

    @BeforeEach
    void setUp() {
        sampleRequest = new TurnoverAssignmentRequest(
                1L, null, null, 10L,
                LocalDate.of(2026, 8, 15), null, null,
                TurnoverStatus.PENDING);

        sampleEntity = new TurnoverAssignment();
        sampleEntity.setId(100L);
        sampleEntity.setPropertyId(1L);
        sampleEntity.setAssignedToId(10L);
        sampleEntity.setAssignedDate(LocalDate.of(2026, 8, 15));
        sampleEntity.setStatus(TurnoverStatus.PENDING);
    }

    // ---- EASIEST 3 METHODS (start here) ----

    @Test
    void existsById_shouldReturnFalseForNull() {
        assertFalse(service.existsById(null));
    }

    @Test
    void existsById_shouldReturnTrueWhenExists() {
        when(repository.existsById(100L)).thenReturn(true);

        assertTrue(service.existsById(100L));
    }

    @Test
    void delete_shouldRemoveEntity() {
        when(repository.findById(100L)).thenReturn(Optional.of(sampleEntity));

        service.delete(100L);

        verify(repository).delete(sampleEntity);
    }


    @Test
    void getById_shouldReturnResponse() {
        when(repository.findById(100L)).thenReturn(Optional.of(sampleEntity));

        TurnoverAssignmentResponse response = service.getById(100L);

        assertEquals(100L, response.id());
        assertEquals(10L, response.assignedToId());
    }

    @Test
    void getById_shouldThrowWhenNotFound() {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.getById(999L));
    }

    @Test
    void getAll_withNoFilters_shouldReturnAll() {
        when(repository.findAll()).thenReturn(List.of(sampleEntity));

        List<TurnoverAssignmentResponse> results = service.getAll(null, null);

        assertEquals(1, results.size());
        assertEquals(100L, results.get(0).id());
    }

    @Test
    void getAll_withPropertyIdFilter_shouldQueryByProperty() {
        when(repository.findByPropertyId(1L)).thenReturn(List.of(sampleEntity));

        List<TurnoverAssignmentResponse> results = service.getAll(1L, null);

        assertEquals(1, results.size());
        verify(repository).findByPropertyId(1L);
        verify(repository, never()).findAll();
    }

    @Test
    void create_shouldSaveAndReturnResponse() {
        when(propertyService.existsById(1L)).thenReturn(true);
        when(userService.existsById(10L)).thenReturn(true);
        when(repository.save(any(TurnoverAssignment.class))).thenReturn(sampleEntity);

        TurnoverAssignmentResponse response = service.create(sampleRequest);

        assertNotNull(response);
        assertEquals(100L, response.id());
        assertEquals(1L, response.propertyId());
        assertEquals(TurnoverStatus.PENDING, response.status());
        verify(repository).save(any(TurnoverAssignment.class));
    }

    @Test
    void create_shouldThrowWhenPropertyNotFound() {
        when(propertyService.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> service.create(sampleRequest));
        verify(repository, never()).save(any());
    }
}
