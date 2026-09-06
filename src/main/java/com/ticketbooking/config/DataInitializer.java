package com.ticketbooking.config;

import com.ticketbooking.model.Event;
import com.ticketbooking.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final EventRepository eventRepository;

    @Override
    public void run(String... args) {
        if (eventRepository.count() == 0) {
            log.info("Event repository is empty. Initializing default sample event...");

            Event defaultEvent = Event.builder()
                    .name("Coldplay Music of the Spheres")
                    .totalSeats(100)
                    .availableSeats(100)
                    .ticketPrice(new BigDecimal("150.00"))
                    .build();

            Event saved = eventRepository.save(defaultEvent);
            log.info("Default event created successfully with ID: {} and name: '{}'", saved.getId(), saved.getName());
        } else {
            log.info("Event repository already contains {} event(s). Skipping initialization.", eventRepository.count());
        }
    }
}
