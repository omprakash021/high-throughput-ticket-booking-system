package com.ticketbooking.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI ticketBookingOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Ticket Booking System API")
                        .description("REST API for Event Management and Distributed Ticket Booking System with Redis locking.")
                        .version("v1.0")
                        .contact(new Contact()
                                .name("Ticket Booking Support")
                                .email("support@ticketbooking.com")));
    }
}
