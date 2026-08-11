package com.stayease.property.service;

import com.stayease.common.exception.ResourceNotFoundException;
import com.stayease.iam.service.UserService;
import com.stayease.property.dto.PropertyRequest;
import com.stayease.property.dto.PropertyResponse;
import com.stayease.property.entity.Property;
import com.stayease.property.mapper.PropertyMapper;
import com.stayease.property.repository.PropertyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Business logic for properties.
 *
 * This service depends on TWO collaborators, both supplied by constructor
 * injection:
 *  - PropertyRepository  (its own data access)
 *  - UserService         (IAM's public API, to validate owner/manager exist)
 *
 * Depending on iam's UserService — not its entity or repository — is how a
 * module reaches another module without tangling their internals. The
 * dependency points "downhill" toward the foundation (property -> iam), which
 * is exactly the build order in the README.
 */
@Service
@Transactional
public class PropertyServiceImpl implements PropertyService {

    private final PropertyRepository propertyRepository;
    private final UserService userService;

    public PropertyServiceImpl(PropertyRepository propertyRepository, UserService userService) {
        this.propertyRepository = propertyRepository;
        this.userService = userService;
    }

    @Override
    public PropertyResponse create(PropertyRequest request) {
        validateOwnerAndManager(request);
        Property saved = propertyRepository.save(PropertyMapper.toEntity(request));
        return PropertyMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PropertyResponse> getAll(Long ownerId) {
        List<Property> properties = (ownerId == null)
                ? propertyRepository.findAll()
                : propertyRepository.findByOwnerId(ownerId);
        return properties.stream().map(PropertyMapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PropertyResponse getById(Long id) {
        return PropertyMapper.toResponse(findPropertyOrThrow(id));
    }

    @Override
    public PropertyResponse update(Long id, PropertyRequest request) {
        Property property = findPropertyOrThrow(id);
        validateOwnerAndManager(request);
        PropertyMapper.updateEntity(property, request);
        return PropertyMapper.toResponse(propertyRepository.save(property));
    }

    @Override
    public void delete(Long id) {
        propertyRepository.delete(findPropertyOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return id != null && propertyRepository.existsById(id);
    }

    /** 404 if the property id is unknown. */
    private Property findPropertyOrThrow(Long id) {
        return propertyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Property not found with id " + id));
    }

    /**
     * Cross-module rule: the owner must be a real user, and the manager (if one
     * was supplied) must also be real. We report these as 404 ResourceNotFound
     * so the client knows exactly which referenced id is bad.
     */
    private void validateOwnerAndManager(PropertyRequest request) {
        if (!userService.existsById(request.ownerId())) {
            throw new ResourceNotFoundException(
                    "Owner (user) not found with id " + request.ownerId());
        }
        if (request.managerId() != null && !userService.existsById(request.managerId())) {
            throw new ResourceNotFoundException(
                    "Manager (user) not found with id " + request.managerId());
        }
    }
}
