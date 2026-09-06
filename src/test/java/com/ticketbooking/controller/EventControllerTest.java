package com.ticketbooking.controller;

import com.ticketbooking.model.Event;
import com.ticketbooking.repository.EventRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EventController.class)
public class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EventRepository eventRepository;

    @Test
    void getAllEvents_ShouldReturnListOfEvents() throws Exception {
        Event event1 = Event.builder()
                .id(1L)
                .name("Coldplay Music of the Spheres")
                .totalSeats(100)
                .availableSeats(100)
                .ticketPrice(new BigDecimal("150.00"))
                .build();

        Event event2 = Event.builder()
                .id(2L)
                .name("Taylor Swift Eras Tour")
                .totalSeats(200)
                .availableSeats(150)
                .ticketPrice(new BigDecimal("250.00"))
                .build();

        when(eventRepository.findAll()).thenReturn(List.of(event1, event2));

        mockMvc.perform(get("/api/v1/events")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Coldplay Music of the Spheres"))
                .andExpect(jsonPath("$[0].totalSeats").value(100))
                .andExpect(jsonPath("$[0].availableSeats").value(100))
                .andExpect(jsonPath("$[0].ticketPrice").value(150.00))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Taylor Swift Eras Tour"));
    }

    @Test
    void getEventById_WhenEventExists_ShouldReturnEvent() throws Exception {
        Event event = Event.builder()
                .id(1L)
                .name("Coldplay Music of the Spheres")
                .totalSeats(100)
                .availableSeats(100)
                .ticketPrice(new BigDecimal("150.00"))
                .build();

        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));

        mockMvc.perform(get("/api/v1/events/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Coldplay Music of the Spheres"))
                .andExpect(jsonPath("$.totalSeats").value(100))
                .andExpect(jsonPath("$.availableSeats").value(100))
                .andExpect(jsonPath("$.ticketPrice").value(150.00));
    }

    @Test
    void getEventById_WhenEventNotFound_ShouldReturn404() throws Exception {
        when(eventRepository.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/events/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
