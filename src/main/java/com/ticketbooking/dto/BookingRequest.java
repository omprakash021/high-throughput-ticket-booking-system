package com.ticketbooking.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Request payload for booking event tickets")
public class BookingRequest {

    @Schema(description = "ID of the user making the booking", example = "1")
    private Long userId;

    @Schema(description = "ID of the event to book tickets for", example = "1")
    private Long eventId;

    @Schema(description = "Number of tickets to book", example = "2")
    private Integer quantity;
}
