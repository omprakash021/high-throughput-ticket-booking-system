package com.ticketbooking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ticketbooking.dto.BookingRequest;
import com.ticketbooking.model.Booking;
import com.ticketbooking.model.BookingStatus;
import com.ticketbooking.model.Event;
import com.ticketbooking.service.TicketBookingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
public class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TicketBookingService ticketBookingService;

    @Test
    void bookTickets_WhenValidRequest_ShouldReturn201Created() throws Exception {
        Event event = Event.builder()
                .id(1L)
                .name("Coldplay Music of the Spheres")
                .totalSeats(100)
                .availableSeats(98)
                .ticketPrice(new BigDecimal("150.00"))
                .build();

        Booking booking = Booking.builder()
                .id(101L)
                .userId(1L)
                .event(event)
                .quantity(2)
                .totalPrice(new BigDecimal("300.00"))
                .status(BookingStatus.CONFIRMED)
                .createdAt(LocalDateTime.of(2026, 9, 3, 20, 0, 0))
                .build();

        when(ticketBookingService.bookTickets(eq(1L), eq(1L), eq(2))).thenReturn(booking);

        BookingRequest request = BookingRequest.builder()
                .userId(1L)
                .eventId(1L)
                .quantity(2)
                .build();

        mockMvc.perform(post("/api/v1/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.bookingId").value(101))
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.eventName").value("Coldplay Music of the Spheres"))
                .andExpect(jsonPath("$.quantity").value(2))
                .andExpect(jsonPath("$.totalPrice").value(300.00))
                .andExpect(jsonPath("$.status").value("CONFIRMED"));
    }

    @Test
    void bookTickets_WhenInvalidQuantity_ShouldReturn400BadRequest() throws Exception {
        when(ticketBookingService.bookTickets(eq(1L), eq(1L), eq(-1)))
                .thenThrow(new IllegalArgumentException("Quantity must be greater than zero"));

        BookingRequest request = BookingRequest.builder()
                .userId(1L)
                .eventId(1L)
                .quantity(-1)
                .build();

        mockMvc.perform(post("/api/v1/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Quantity must be greater than zero"));
    }

    @Test
    void bookTickets_WhenNotEnoughSeats_ShouldReturn409Conflict() throws Exception {
        when(ticketBookingService.bookTickets(eq(1L), eq(1L), eq(50)))
                .thenThrow(new IllegalStateException("Not enough seats available for event ID: 1"));

        BookingRequest request = BookingRequest.builder()
                .userId(1L)
                .eventId(1L)
                .quantity(50)
                .build();

        mockMvc.perform(post("/api/v1/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Not enough seats available for event ID: 1"));
    }
}
