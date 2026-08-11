package com.stayease.property.service;

import com.stayease.property.dto.PropertyRequest;
import com.stayease.property.dto.PropertyResponse;

import java.util.List;

/**
 * Business operations for properties.
 *
 * getAll takes an optional ownerId filter: null = all properties,
 * a value = only that owner's properties.
 */
public interface PropertyService {

    PropertyResponse create(PropertyRequest request);

    List<PropertyResponse> getAll(Long ownerId);

    PropertyResponse getById(Long id);

    PropertyResponse update(Long id, PropertyRequest request);

    void delete(Long id);

    /** Existence check used by sibling modules (availability, pricing, booking). */
    boolean existsById(Long id);
}
