package com.ticketbooking.service;

import com.ticketbooking.dto.BookingEvent;
import com.ticketbooking.model.Booking;
import com.ticketbooking.model.BookingStatus;
import com.ticketbooking.model.Event;
import com.ticketbooking.repository.BookingRepository;
import com.ticketbooking.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class TicketBookingService {

    private final RedissonClient redissonClient;
    private final EventRepository eventRepository;
    private final BookingRepository bookingRepository;
    private final BookingEventProducer bookingEventProducer;

    public Booking bookTickets(Long userId, Long eventId, Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }

        String lockKey = "lock:event:" + eventId;
        RLock lock = redissonClient.getLock(lockKey);

        lock.lock();
        try {
            Event event = eventRepository.findById(eventId)
                    .orElseThrow(() -> new IllegalArgumentException("Event not found with ID: " + eventId));

            if (event.getAvailableSeats() == null || event.getAvailableSeats() < quantity) {
                throw new IllegalStateException("Not enough seats available for event ID: " + eventId);
            }

            event.setAvailableSeats(event.getAvailableSeats() - quantity);
            eventRepository.save(event);

            BigDecimal totalPrice = event.getTicketPrice() != null
                    ? event.getTicketPrice().multiply(BigDecimal.valueOf(quantity))
                    : BigDecimal.ZERO;

            Booking booking = Booking.builder()
                    .userId(userId)
                    .event(event)
                    .quantity(quantity)
                    .totalPrice(totalPrice)
                    .status(BookingStatus.CONFIRMED)
                    .build();

            Booking savedBooking = bookingRepository.save(booking);

            BookingEvent bookingEvent = BookingEvent.builder()
                    .bookingId(savedBooking.getId())
                    .userId(savedBooking.getUserId())
                    .eventName(savedBooking.getEvent() != null ? savedBooking.getEvent().getName() : null)
                    .totalPrice(savedBooking.getTotalPrice())
                    .timestamp(LocalDateTime.now())
                    .build();

            bookingEventProducer.sendBookingNotification(bookingEvent);

            return savedBooking;
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }
}
