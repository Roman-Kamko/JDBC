package com.kamko.dto;

public record TicketFilter(int limit,
                           int offset,
                           String passengerName,
                           String seatNo) {
}
