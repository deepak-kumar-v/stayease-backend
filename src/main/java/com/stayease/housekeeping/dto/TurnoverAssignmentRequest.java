package com.stayease.housekeeping.dto;

import com.stayease.housekeeping.enums.TurnoverStatus;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record TurnoverAssignmentRequest(

        @NotNull(message = "propertyId is required")
        Long propertyId,

        Long checkOutReservationId,

        Long checkInReservationId,

        Long assignedToId,          // housekeeping user

        LocalDate assignedDate,

        LocalDateTime startByTime,

        LocalDateTime completeByTime,

        TurnoverStatus status
) {
}
