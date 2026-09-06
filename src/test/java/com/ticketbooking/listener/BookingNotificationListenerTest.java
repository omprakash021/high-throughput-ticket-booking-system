package com.ticketbooking.listener;

import com.ticketbooking.dto.BookingEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@ExtendWith(MockitoExtension.class)
public class BookingNotificationListenerTest {

    @InjectMocks
    private BookingNotificationListener bookingNotificationListener;

    @Test
    void handleBookingNotification_ShouldProcessEventWithoutErrors() {
        BookingEvent event = BookingEvent.builder()
                .bookingId(101L)
                .userId(1L)
                .eventName("Coldplay Music of the Spheres")
                .totalPrice(new BigDecimal("150.00"))
                .timestamp(LocalDateTime.now())
                .build();

        assertDoesNotThrow(() -> bookingNotificationListener.handleBookingNotification(event));
    }
}
