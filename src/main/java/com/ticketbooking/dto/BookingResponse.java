package com.ticketbooking.dto;

import com.ticketbooking.model.Booking;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Response payload representing a ticket booking")
public class BookingResponse {

    @Schema(description = "Unique ID of the booking", example = "101")
    private Long bookingId;

    @Schema(description = "ID of the user who booked tickets", example = "1")
    private Long userId;

    @Schema(description = "Name of the booked event", example = "Coldplay Music of the Spheres")
    private String eventName;

    @Schema(description = "Number of tickets booked", example = "2")
    private Integer quantity;

    @Schema(description = "Total price charged for the booking", example = "300.00")
    private BigDecimal totalPrice;

    @Schema(description = "Current status of the booking", example = "CONFIRMED")
    private String status;

    @Schema(description = "Timestamp when the booking was created")
    private LocalDateTime createdAt;

    public static BookingResponse fromEntity(Booking booking) {
        if (booking == null) {
            return null;
        }
        return BookingResponse.builder()
                .bookingId(booking.getId())
                .userId(booking.getUserId())
                .eventName(booking.getEvent() != null ? booking.getEvent().getName() : null)
                .quantity(booking.getQuantity())
                .totalPrice(booking.getTotalPrice())
                .status(booking.getStatus() != null ? booking.getStatus().name() : null)
                .createdAt(booking.getCreatedAt())
                .build();
    }
}
