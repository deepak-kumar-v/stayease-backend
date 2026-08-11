package com.stayease.housekeeping.controller;

import com.stayease.housekeeping.dto.TurnoverChecklistRequest;
import com.stayease.housekeeping.dto.TurnoverChecklistResponse;
import com.stayease.housekeeping.service.TurnoverChecklistService;
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
@RequestMapping("/api/checklists")
public class TurnoverChecklistController {

    private final TurnoverChecklistService service;

    public TurnoverChecklistController(TurnoverChecklistService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TurnoverChecklistResponse create(@Valid @RequestBody TurnoverChecklistRequest request) {
        return service.create(request);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<TurnoverChecklistResponse> getByTurnover(@RequestParam Long turnoverId) {
        return service.getByTurnover(turnoverId);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TurnoverChecklistResponse getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TurnoverChecklistResponse update(
            @PathVariable Long id, @Valid @RequestBody TurnoverChecklistRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}