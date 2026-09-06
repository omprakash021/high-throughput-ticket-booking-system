package com.ticketbooking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long bookingId;
    private Long userId;
    private String eventName;
    private BigDecimal totalPrice;
    private LocalDateTime timestamp;
}
