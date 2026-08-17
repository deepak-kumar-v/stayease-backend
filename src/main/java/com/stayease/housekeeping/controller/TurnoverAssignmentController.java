package com.stayease.housekeeping.controller;

import com.stayease.housekeeping.dto.TurnoverAssignmentRequest;
import com.stayease.housekeeping.dto.TurnoverAssignmentResponse;
import com.stayease.housekeeping.service.TurnoverAssignmentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/turnovers")
public class TurnoverAssignmentController {

    private final TurnoverAssignmentService service;

    public TurnoverAssignmentController(TurnoverAssignmentService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TurnoverAssignmentResponse create(@Valid @RequestBody TurnoverAssignmentRequest request) {
        return service.create(request);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TurnoverAssignmentResponse getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<TurnoverAssignmentResponse> getAll(
            @RequestParam(required = false) Long propertyId,
            @RequestParam(required = false) Long assignedToId) {
        return service.getAll(propertyId, assignedToId);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TurnoverAssignmentResponse update(
            @PathVariable Long id, @Valid @RequestBody TurnoverAssignmentRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}