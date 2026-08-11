package com.stayease.property.repository;

import com.stayease.property.entity.AvailabilityCalendar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Data access for per-date availability rows.
 *
 * findByPropertyIdAndCalendarDate lets us enforce the "one row per (property,
 * date)" rule in the service with a friendly 409 instead of relying only on the
 * DB unique constraint to blow up.
 */
@Repository
public interface AvailabilityCalendarRepository extends JpaRepository<AvailabilityCalendar, Long> {

    List<AvailabilityCalendar> findByPropertyId(Long propertyId);

    Optional<AvailabilityCalendar> findByPropertyIdAndCalendarDate(Long propertyId, LocalDate calendarDate);
}
