package com.stayease.property.controller;

import com.stayease.property.dto.PropertyRequest;
import com.stayease.property.dto.PropertyResponse;
import com.stayease.property.service.PropertyService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST endpoints for properties under /api/properties.
 */
@RestController
@RequestMapping("/api/properties")
public class PropertyController {

    private final PropertyService propertyService;

    public PropertyController(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    /** POST /api/properties — create. 201 Created. */
    @PostMapping
    public ResponseEntity<PropertyResponse> create(@Valid @RequestBody PropertyRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(propertyService.create(request));
    }

    /**
     * GET /api/properties           -> all properties
     * GET /api/properties?ownerId=5 -> only owner 5's properties
     *
     * @RequestParam(required = false) makes ownerId optional. If it's absent the
     * value is null, and the service returns everything.
     */
    @GetMapping
    public ResponseEntity<List<PropertyResponse>> getAll(
            @RequestParam(required = false) Long ownerId) {
        return ResponseEntity.ok(propertyService.getAll(ownerId));
    }

    /** GET /api/properties/{id} — one property. 200 or 404. */
    @GetMapping("/{id}")
    public ResponseEntity<PropertyResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(propertyService.getById(id));
    }

    /** PUT /api/properties/{id} — update. 200 or 404. */
    @PutMapping("/{id}")
    public ResponseEntity<PropertyResponse> update(
            @PathVariable Long id, @Valid @RequestBody PropertyRequest request) {
        return ResponseEntity.ok(propertyService.update(id, request));
    }

    /** DELETE /api/properties/{id} — delete. 204 No Content. */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        propertyService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
