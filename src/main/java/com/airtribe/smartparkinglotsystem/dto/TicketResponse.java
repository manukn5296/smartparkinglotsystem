package com.airtribe.smartparkinglotsystem.dto;


import com.airtribe.smartparkinglotsystem.entity.ParkingTicket;

import java.time.Instant;
import java.util.UUID;

public record TicketResponse(UUID ticketId,
                             String vehicleNumber,
                             int floor,
                             int spotNumber,
                             Instant entryTime) {

    public static TicketResponse from(ParkingTicket t) {
        return new TicketResponse(t.getId(), t.getVehicleNumber(),
                t.getSpot().getFloorNumber(), t.getSpot().getSpotNumber(), t.getEntryTime());
    }
}
