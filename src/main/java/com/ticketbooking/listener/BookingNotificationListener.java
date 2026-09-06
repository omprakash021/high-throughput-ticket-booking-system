package com.ticketbooking.listener;

import com.ticketbooking.dto.BookingEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class BookingNotificationListener {

    @KafkaListener(topics = "booking-notifications", groupId = "ticket-booking-group")
    public void handleBookingNotification(BookingEvent event) {
        log.info("[Async Kafka] Sending confirmation email to user {} for event '{}' (Booking ID: {}, Total Price: ${}, Booked At: {})",
                event.getUserId(),
                event.getEventName(),
                event.getBookingId(),
                event.getTotalPrice(),
                event.getTimestamp());
    }
}
