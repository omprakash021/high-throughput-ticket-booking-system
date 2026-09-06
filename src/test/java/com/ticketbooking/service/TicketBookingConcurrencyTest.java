package com.ticketbooking.service;

import com.ticketbooking.model.Event;
import com.ticketbooking.repository.BookingRepository;
import com.ticketbooking.repository.EventRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class TicketBookingConcurrencyTest {

    @Autowired
    private TicketBookingService ticketBookingService;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @org.springframework.boot.test.mock.mockito.MockBean
    private BookingEventProducer bookingEventProducer;

    @Test
    void testConcurrentBookings_PreventsOverbooking() throws InterruptedException {
        // Clean up repositories before running test
        bookingRepository.deleteAll();
        eventRepository.deleteAll();

        // 1. Set up a test Event with 50 total and 50 available seats
        Event event = Event.builder()
                .name("Coldplay Concurrency Test")
                .totalSeats(50)
                .availableSeats(50)
                .ticketPrice(new BigDecimal("150.00"))
                .build();

        Event savedEvent = eventRepository.save(event);
        Long eventId = savedEvent.getId();

        // 2. Set up concurrency controls with 100 threads
        int totalThreads = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(totalThreads);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch endLatch = new CountDownLatch(totalThreads);

        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failureCount = new AtomicInteger(0);

        // 3. Submit 100 tasks waiting on startLatch
        for (int i = 1; i <= totalThreads; i++) {
            final long userId = i;
            executorService.submit(() -> {
                try {
                    startLatch.await();
                    ticketBookingService.bookTickets(userId, eventId, 1);
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    failureCount.incrementAndGet();
                } finally {
                    endLatch.countDown();
                }
            });
        }

        // 4. Release all threads simultaneously
        startLatch.countDown();

        // 5. Wait for all threads to finish execution
        boolean completed = endLatch.await(60, TimeUnit.SECONDS);
        executorService.shutdown();

        // 6. Assertions
        assertEquals(50, successCount.get(), "Only 50 bookings should succeed for 50 available seats");
        assertEquals(50, failureCount.get(), "50 booking requests should fail due to lack of seats");

        Event updatedEvent = eventRepository.findById(eventId).orElseThrow();
        assertEquals(0, updatedEvent.getAvailableSeats(), "Event available seats must be exactly 0");

        long totalBookings = bookingRepository.count();
        assertEquals(50, totalBookings, "Total bookings saved in database must be exactly 50");
    }
}
