package com.ticketbooking.dto;

import com.ticketbooking.model.Event;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Response payload representing event details")
public class EventResponse {

    @Schema(description = "Unique ID of the event", example = "1")
    private Long id;

    @Schema(description = "Name of the event", example = "Coldplay Music of the Spheres")
    private String name;

    @Schema(description = "Total capacity / seats for the event", example = "100")
    private Integer totalSeats;

    @Schema(description = "Current available seats for booking", example = "100")
    private Integer availableSeats;

    @Schema(description = "Price per ticket", example = "150.00")
    private BigDecimal ticketPrice;

    public static EventResponse fromEntity(Event event) {
        if (event == null) {
            return null;
        }
        return EventResponse.builder()
                .id(event.getId())
                .name(event.getName())
                .totalSeats(event.getTotalSeats())
                .availableSeats(event.getAvailableSeats())
                .ticketPrice(event.getTicketPrice())
                .build();
    }
}
