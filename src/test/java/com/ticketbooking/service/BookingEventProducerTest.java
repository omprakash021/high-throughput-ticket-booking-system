package com.ticketbooking.service;

import com.ticketbooking.dto.BookingEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class BookingEventProducerTest {

    @Mock
    private KafkaTemplate<String, BookingEvent> kafkaTemplate;

    @InjectMocks
    private BookingEventProducer bookingEventProducer;

    @Test
    void sendBookingNotification_ShouldSendToCorrectTopicWithKey() {
        BookingEvent event = BookingEvent.builder()
                .bookingId(101L)
                .userId(1L)
                .eventName("Coldplay Music of the Spheres")
                .totalPrice(new BigDecimal("150.00"))
                .timestamp(LocalDateTime.now())
                .build();

        bookingEventProducer.sendBookingNotification(event);

        verify(kafkaTemplate).send(BookingEventProducer.TOPIC_BOOKING_NOTIFICATIONS, "101", event);
    }
}
