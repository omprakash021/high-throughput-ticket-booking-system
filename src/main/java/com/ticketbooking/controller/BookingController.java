package com.ticketbooking.controller;

import com.ticketbooking.dto.BookingRequest;
import com.ticketbooking.dto.BookingResponse;
import com.ticketbooking.model.Booking;
import com.ticketbooking.service.TicketBookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
@Tag(name = "Bookings", description = "Endpoints for ticket booking operations")
public class BookingController {

    private final TicketBookingService ticketBookingService;

    @PostMapping
    @Operation(summary = "Book event tickets", description = "Processes a ticket booking request using distributed Redis locking to prevent overselling.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Booking created successfully",
                    content = @Content(schema = @Schema(implementation = BookingResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid booking request or event not found"),
            @ApiResponse(responseCode = "409", description = "Not enough seats available / Concurrency conflict")
    })
    public ResponseEntity<BookingResponse> bookTickets(@RequestBody BookingRequest request) {
        Booking booking = ticketBookingService.bookTickets(
                request.getUserId(),
                request.getEventId(),
                request.getQuantity()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(BookingResponse.fromEntity(booking));
    }
}
