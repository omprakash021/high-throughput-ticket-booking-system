package com.ticketbooking.config;

import com.ticketbooking.model.Event;
import com.ticketbooking.repository.EventRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DataInitializerTest {

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private DataInitializer dataInitializer;

    @Test
    void run_WhenRepositoryIsEmpty_ShouldSeedDefaultEvent() {
        when(eventRepository.count()).thenReturn(0L);
        when(eventRepository.save(any(Event.class))).thenAnswer(invocation -> invocation.getArgument(0));

        dataInitializer.run();

        ArgumentCaptor<Event> eventCaptor = ArgumentCaptor.forClass(Event.class);
        verify(eventRepository).save(eventCaptor.capture());

        Event savedEvent = eventCaptor.getValue();
        assertEquals("Coldplay Music of the Spheres", savedEvent.getName());
        assertEquals(100, savedEvent.getTotalSeats());
        assertEquals(100, savedEvent.getAvailableSeats());
        assertEquals(new BigDecimal("150.00"), savedEvent.getTicketPrice());
    }

    @Test
    void run_WhenRepositoryIsNotEmpty_ShouldNotSeedData() {
        when(eventRepository.count()).thenReturn(1L);

        dataInitializer.run();

        verify(eventRepository, never()).save(any(Event.class));
    }
}
