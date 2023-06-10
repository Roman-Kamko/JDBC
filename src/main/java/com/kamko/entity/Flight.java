package com.kamko.entity;

import java.time.LocalDateTime;

public record Flight(int id,
                     String flightNo,
                     LocalDateTime departureDate,
                     String departureAirportCode,
                     LocalDateTime arrivalDate,
                     String arrivalAirportCode,
                     int aircraftId,
                     String status) {
}
