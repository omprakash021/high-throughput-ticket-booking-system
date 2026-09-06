package com.ticketbooking.service;

import com.ticketbooking.dto.BookingEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingEventProducer {

    public static final String TOPIC_BOOKING_NOTIFICATIONS = "booking-notifications";

    private final KafkaTemplate<String, BookingEvent> kafkaTemplate;

    public void sendBookingNotification(BookingEvent event) {
        log.info("Publishing BookingEvent to topic '{}': {}", TOPIC_BOOKING_NOTIFICATIONS, event);
        kafkaTemplate.send(TOPIC_BOOKING_NOTIFICATIONS, String.valueOf(event.getBookingId()), event);
    }
}
