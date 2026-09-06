package com.ticketbooking.controller;

import com.ticketbooking.dto.EventResponse;
import com.ticketbooking.repository.EventRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
@Tag(name = "Events", description = "Endpoints for viewing and querying events")
public class EventController {

    private final EventRepository eventRepository;

    @GetMapping
    @Operation(summary = "Get all events", description = "Retrieves a list of all available events in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved event list")
    })
    public ResponseEntity<List<EventResponse>> getAllEvents() {
        List<EventResponse> events = eventRepository.findAll().stream()
                .map(EventResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(events);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get event by ID", description = "Retrieves details for a specific event by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the event"),
            @ApiResponse(responseCode = "404", description = "Event not found with given ID")
    })
    public ResponseEntity<EventResponse> getEventById(
            @Parameter(description = "ID of the event to retrieve", required = true)
            @PathVariable Long id) {
        return eventRepository.findById(id)
                .map(EventResponse::fromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
